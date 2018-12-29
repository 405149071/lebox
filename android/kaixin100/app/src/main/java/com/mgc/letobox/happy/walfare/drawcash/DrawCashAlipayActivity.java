package com.mgc.letobox.happy.walfare.drawcash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.kymjs.rxvolley.RxVolley;
import com.leto.game.base.bean.BaseRequestBean;
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
import com.mgc.letobox.happy.domain.DrawCashAlipayInfoResultBean;
import com.mgc.letobox.happy.domain.DrawCashApplyRequestBean;
import com.mgc.letobox.happy.domain.DrawCashOrderInfoResultBean;
import com.mgc.letobox.happy.ui.SpacesItemDecoration;
import com.mgc.letobox.happy.util.GsonUtil;
import com.mgc.letobox.happy.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by zhaozhihui on 2018/8/23
 **/
public class DrawCashAlipayActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.rl_left)
    RelativeLayout rl_left;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_right)
    ImageView iv_right;

    @BindView(R.id.tv_balance)
    TextView tv_balance;

    @BindView(R.id.tv_value)
    TextView tv_value;

    @BindView(R.id.et_account)
    EditText et_account;

    @BindView(R.id.et_name)
    EditText et_name;

    @BindView(R.id.btn_ok)
    Button btn_ok;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ChargePoint mChargePoint;

    List<ChargePoint.Point> mList = new ArrayList<>();


    DrawCashAlipayInfoResultBean mAlipayInfo;

    AmountAdapter mAdapter;

    int amount;

    public static void start(Context mContext, DrawCashAlipayInfoResultBean data) {
        Intent mIntent = new Intent(mContext, DrawCashAlipayActivity.class);
        mIntent.putExtra(IntentContant.EXTRA_DRAW_CASH_ALIPAY,data);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_cash_alipay);
        ButterKnife.bind(this);

        StatusBarUtil.setTransparentForImageView(this, null);

        mAlipayInfo = (DrawCashAlipayInfoResultBean) getIntent().getSerializableExtra(IntentContant.EXTRA_DRAW_CASH_ALIPAY);
        if(mAlipayInfo==null){
            finish();
        }
        if (null!=mAlipayInfo.getChargePoint()) {
            mChargePoint = mAlipayInfo.getChargePoint();
            mList = mChargePoint.getPoints();
            for (ChargePoint.Point point : mList){
                if(point.getAmount()>=mChargePoint.getBalance_rmb()){
                    point.setEnable(false);
                }else {
                    point.setEnable(true);
                }
            }

        }

        initView();
        getOrderStatus();
    }


    private void initView(){
        tv_title.setText("支付宝");
        rl_left.setOnClickListener(this);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setImageResource(R.mipmap.ic_question);
        iv_right.setOnClickListener(this);
        btn_ok.setOnClickListener(this);

        if(null!=mChargePoint) {
            tv_balance.setText("" + mChargePoint.getBalance_rmb());
        }
        et_name.setEnabled(false);
        et_name.setText(mAlipayInfo.getTruename());
        et_account.setText(mAlipayInfo.getAccount());
        et_account.setEnabled(false);

        if(mChargePoint.getBalance_rmb()<10){
            btn_ok.setText("余额不足");
            btn_ok.setEnabled(false);
        }


        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
         //设置表格，根据position计算在该position处1列占几格数据
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                //计算在哪个position时要显示1列数据，即columnCount / 1列 = 4格，即1列数据占满4格
                if (position == 0 || position == mAlipayInfo.getChargePoint().getPoints().size() + 1) {
                    return 4;
                }

                //return 2即为columnCount / 2 = 2列，一格数据占2列，该行显示2列

                //1格1列，即改行有columnCount / 1 = 4列，该行显示4列
                return 1;
            }
        });


        mAdapter = new AmountAdapter(DrawCashAlipayActivity.this, mList);
        SpacesItemDecoration decoration = new SpacesItemDecoration(BaseAppUtil.dip2px(mContext, 10) / 2,false);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                float real_amount =  mList.get(position).getReal_amount();

                if(mChargePoint.getBalance_rmb()>=real_amount) {
                    tv_value.setText("" + real_amount + "元");
                    amount = (int) mList.get(position).getAmount();

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
    private void initData() {
        BaseRequestBean requestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<DrawCashAlipayInfoResultBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(DrawCashAlipayInfoResultBean data) {
                if (data != null) {
                    mChargePoint = data.getChargePoint();
                    if(mList!=null){
                        mList.clear();
                    }
                    mList.addAll(data.getChargePoint().getPoints());
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(SdkApi.getDrawCashAlipayInfo(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_left:
                finish();
                break;
            case R.id.iv_right:
                HelpActivity.start(DrawCashAlipayActivity.this, "常见问题", SdkApi.getDrawCashQR());
                break;

            case R.id.btn_ok:
                if(!Util.isFastClick()) return;

                if(TextUtils.isEmpty(tv_value.getText().toString())){
                    T.s(DrawCashAlipayActivity.this, " 请选择提现金额");
                    return;
                }
                if(amount==0){
                    T.s(DrawCashAlipayActivity.this, " 请选择提现金额");
                    return;
                }
                apply(amount);

                break;

        }
    }

    /**
     * 预下单
     */
    private void apply(int amount) {
        DrawCashApplyRequestBean requestBean = new DrawCashApplyRequestBean();
        requestBean.setType(2);
        requestBean.setAmount(amount);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<String>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(String data) {
                T.s(DrawCashAlipayActivity.this, "提现申请成功");
                btn_ok.setEnabled(false);
                btn_ok.setText("请明日提现");

                DrawCashHistoryActivity.start(DrawCashAlipayActivity.this);
                finish();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                T.s(DrawCashAlipayActivity.this, msg);
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(SdkApi.getDrawCashApply(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }



    /**
     * 获得订单状态
     */
    private void getOrderStatus() {
        DrawCashApplyRequestBean requestBean = new DrawCashApplyRequestBean();
        requestBean.setType(1);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<DrawCashOrderInfoResultBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(DrawCashOrderInfoResultBean data) {
                if(data.getIs_draw()==1){
                    btn_ok.setEnabled(false);
                    btn_ok.setText("待审核");
                }else if(data.getIs_draw()==2){
                    btn_ok.setEnabled(false);
                    btn_ok.setText("请明日提现");
                }else{
                    btn_ok.setEnabled(true);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                T.s(DrawCashAlipayActivity.this, msg);
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(SdkApi.getDrawCashStatus(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


}
