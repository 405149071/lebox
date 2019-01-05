package com.mgc.letobox.happy.walfare.drawcash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.kymjs.rxvolley.RxVolley;
import com.ledong.lib.minigame.view.RoundedImageView;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.mgc.letobox.happy.R;
import com.liang530.log.T;
import com.liang530.utils.BaseAppUtil;

import com.mgc.letobox.happy.HelpActivity;
import com.mgc.letobox.happy.base.BaseActivity;
import com.mgc.letobox.happy.config.IntentContant;
import com.mgc.letobox.happy.config.SdkApi;
import com.mgc.letobox.happy.domain.ChargePoint;
import com.mgc.letobox.happy.domain.DrawCashApplyRequestBean;
import com.mgc.letobox.happy.domain.DrawCashOrderInfoResultBean;
import com.mgc.letobox.happy.domain.DrawCashWeixinInfoResultBean;
import com.mgc.letobox.happy.domain.TaskListReponse;
import com.mgc.letobox.happy.listener.ActionBarClickListener;
import com.mgc.letobox.happy.ui.SpacesItemDecoration;
import com.mgc.letobox.happy.ui.TranslucentActionBar;
import com.mgc.letobox.happy.ui.TranslucentScrollView;
import com.mgc.letobox.happy.util.GsonUtil;
import com.mgc.letobox.happy.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by zhaozhihui on 2018/8/23
 **/
public class DrawCashWeixinActivity extends BaseActivity implements View.OnClickListener, ActionBarClickListener, TranslucentScrollView.TranslucentChangedListener {

    @BindView(R.id.tv_balance)
    TextView tv_balance;

    @BindView(R.id.tv_value)
    TextView tv_value;

    @BindView(R.id.avatar_view)
    RoundedImageView avatar_view;

    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    @BindView(R.id.tv_point_title)
    TextView tv_point_title;
    @BindView(R.id.tv_point_content)
    TextView tv_point_content;


    @BindView(R.id.et_name)
    EditText et_name;

    @BindView(R.id.btn_ok)
    Button btn_ok;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.actionbar)
    TranslucentActionBar actionBar;

    ChargePoint mChargePoint;

    List<ChargePoint.Point> mList = new ArrayList<>();


    DrawCashWeixinInfoResultBean mWeixinInfo;

    AmountAdapter mAdapter;

    int amount;

    public static void start(Context mContext, DrawCashWeixinInfoResultBean data) {
        Intent mIntent = new Intent(mContext, DrawCashWeixinActivity.class);
        mIntent.putExtra(IntentContant.EXTRA_DRAW_CASH_WEIXIN, data);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_cash_weixin);
        ButterKnife.bind(this);

        initActionBar();

        StatusBarUtil.setTransparentForImageView(this, null);

        mWeixinInfo = (DrawCashWeixinInfoResultBean) getIntent().getSerializableExtra(IntentContant.EXTRA_DRAW_CASH_WEIXIN);
        if (mWeixinInfo == null) {
            finish();
        }
        if (null != mWeixinInfo.getChargePoint()) {
            mChargePoint = mWeixinInfo.getChargePoint();
            mList = mChargePoint.getPoints();
            for (ChargePoint.Point point : mList) {
                if (point.getAmount() >= mChargePoint.getBalance_rmb()) {
                    point.setEnable(false);
                } else {
                    point.setEnable(true);
                }
            }
        }

        initView();
    }

    private void initView() {

        tv_balance.setText("" + mWeixinInfo.getChargePoint().getBalance_rmb());
        et_name.setEnabled(false);
        et_name.setText(mWeixinInfo.getTruename());
        et_name.setVisibility(View.GONE);
        tv_nickname.setText(mWeixinInfo.getNickname());
        btn_ok.setOnClickListener(this);

        Glide.with(DrawCashWeixinActivity.this).load(mWeixinInfo.getHeadimgurl()).error(R.mipmap.default_avatar).into(avatar_view);

        if (mChargePoint.getBalance_rmb() < 10) {
            btn_ok.setText("余额不足");
            btn_ok.setEnabled(false);
        }

        mAdapter = new AmountAdapter(DrawCashWeixinActivity.this, mList);
        SpacesItemDecoration decoration = new SpacesItemDecoration(BaseAppUtil.dip2px(mContext, 10) / 2, false);
        recyclerView.addItemDecoration(decoration);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        //设置表格，根据position计算在该position处1列占几格数据
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //计算在哪个position时要显示1列数据，即columnCount / 1列 = 4格，即1列数据占满4格
                if (position == 0 || position == mWeixinInfo.getChargePoint().getPoints().size() + 1) {
                    return 4;
                }
                //return 2即为columnCount / 2 = 2列，一格数据占2列，该行显示2列
                //1格1列，即改行有columnCount / 1 = 4列，该行显示4列
                return 1;
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                float real_amount = mList.get(position).getReal_amount();
                if (mChargePoint.getBalance_rmb() >= real_amount) {
                    tv_value.setText("" + real_amount + "元");
                    amount = (int) mList.get(position).getAmount();

                    tv_point_title.setText(mList.get(position).title);
                    tv_point_content.setText(mList.get(position).content);

                    List<ChargePoint.Point> points = adapter.getData();
                    for (int i = 0; i < points.size(); i++) {
                        if (i == position) {
                            points.get(i).setSelected(true);
                        } else {
                            points.get(i).setSelected(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


    /**
     * 个人任务列表
     */
    private void apply(int amount) {
        DrawCashApplyRequestBean requestBean = new DrawCashApplyRequestBean();
        requestBean.setType(1);
        requestBean.setAmount(amount);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<List<TaskListReponse>>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(List<TaskListReponse> data) {
                T.s(DrawCashWeixinActivity.this, "提现申请成功");
                DrawCashHistoryActivity.start(DrawCashWeixinActivity.this);
                finish();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                T.s(DrawCashWeixinActivity.this, msg);
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(SdkApi.getDrawCashApply(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_left:
                finish();
                break;

            case R.id.btn_ok:
                if (!Util.isFastClick()) return;

                if (amount <= 0) {
                    T.s(DrawCashWeixinActivity.this, " 请选择提现金额");
                    return;
                }
                apply(amount);

                break;

        }
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {
        HelpActivity.start(DrawCashWeixinActivity.this, "常见问题", SdkApi.getDrawCashQR());
    }

    @Override
    public void onRight2Click() {

    }

    @Override
    public void onTranslucentChanged(int transAlpha) {
        actionBar.tvTitle.setVisibility(View.VISIBLE);
    }

    private void initActionBar() {
        actionBar = (TranslucentActionBar) findViewById(R.id.actionbar);
        //初始actionBar
        actionBar.setData("微信提现", R.mipmap.back_normal, null, R.mipmap.ic_question, null, this);
        //开启渐变
        actionBar.setNeedTranslucent();
        //设置状态栏高度
        actionBar.setStatusBarHeight(getStatusBarHeight());

        actionBar.tvTitle.setVisibility(View.VISIBLE);

        TranslucentScrollView sv_content = (TranslucentScrollView) findViewById(R.id.translucentScrollView);
        //设置透明度变化监听
        sv_content.setTranslucentChangedListener(this);
        //关联需要渐变的视图
        sv_content.setTransView(actionBar);
        //设置ActionBar键渐变颜色
        sv_content.setTransColor(getResources().getColor(R.color.mgc_sdk_bg_blue));
    }
}
