package com.mgc.letobox.happy.walfare.drawcash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.kymjs.rxvolley.RxVolley;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.mgc.letobox.happy.R;
import com.liang530.utils.BaseAppUtil;
import com.mgc.letobox.happy.HelpActivity;
import com.mgc.letobox.happy.base.BaseActivity;
import com.mgc.letobox.happy.config.SdkApi;
import com.mgc.letobox.happy.domain.DrawCashHistoryResultBean;
import com.mgc.letobox.happy.domain.ListBaseRequestBean;
import com.mgc.letobox.happy.ui.SpacesItemDecoration;
import com.mgc.letobox.happy.util.GsonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by zhaozhihui on 2018/8/23
 **/
public class DrawCashHistoryActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.rl_left)
    RelativeLayout rl_left;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_right)
    ImageView iv_right;


    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    DrawCashHistoryAdapter mAdapter;

    List<DrawCashHistoryResultBean>  mDataList = new ArrayList();

    int mPage;


    public static void start(Context mContext) {
        Intent mIntent = new Intent(mContext, DrawCashHistoryActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_list2);
        ButterKnife.bind(this);

        StatusBarUtil.setTransparentForImageView(this, null);

        initView();

        mPage =1;
        getPageData(mPage);
    }


    private void initView(){
        tv_title.setText("兑换记录");
        rl_left.setOnClickListener(this);

        iv_right.setVisibility(View.VISIBLE);

        iv_right.setImageResource(R.mipmap.ic_question);
        iv_right.setOnClickListener(this);


        mAdapter = new DrawCashHistoryAdapter(DrawCashHistoryActivity.this, mDataList);
        SpacesItemDecoration decoration = new SpacesItemDecoration(BaseAppUtil.dip2px(mContext, 10) / 2,false);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {


            }
        });

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                mPage = 1;
                getPageData(mPage);
            }
        });

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPage++;
                getPageData(mPage);
            }
        });

        View mView = LayoutInflater.from(this).inflate(R.layout.center_no_data, null);
        ((TextView) mView.findViewById(R.id.tv_no_comment)).setText("暂无记录");
        mAdapter.setEmptyView(mView);
    }


    /**
     * 个人任务列表
     */
    private void getPageData(final int page) {
        ListBaseRequestBean requestBean = new ListBaseRequestBean();
        requestBean.setPage(page);
        requestBean.setOffset(10);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<List<DrawCashHistoryResultBean>>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(List<DrawCashHistoryResultBean> data) {

                if (data != null) {
                    Gson gson = new Gson();
                    String str = gson.toJson(data);
                    List<DrawCashHistoryResultBean> responses = gson.fromJson(str, new TypeToken<List<DrawCashHistoryResultBean>>() {}.getType());
                    if (responses != null && responses.size() != 0) {
                        if (page == 1) {
                            mAdapter.setNewData(responses);
                            smartRefreshLayout.finishRefresh();
                            smartRefreshLayout.setNoMoreData(false);
//                            if (responses.size() < 10) {
//                                smartRefreshLayout.finishLoadMoreWithNoMoreData();
//                            }
                        } else {
                            mAdapter.addData(responses);
                            smartRefreshLayout.finishRefresh();
                            smartRefreshLayout.finishLoadMore(0);
                            if (responses.size() < 10) {
                                smartRefreshLayout.setNoMoreData(true);
                            }
                        }
                    } else {
                        smartRefreshLayout.finishRefresh();
                        smartRefreshLayout.finishLoadMore(0);
                        smartRefreshLayout.setNoMoreData(true);
                    }
                }else {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore(0);
                    if (mAdapter.getData() != null && mAdapter.getData().size() == 0) {
                        smartRefreshLayout.setNoMoreData(true);
                    }
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
        RxVolley.post(SdkApi.getDrawCashLog(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_left:
                finish();
                break;
            case R.id.iv_right:
                HelpActivity.start(DrawCashHistoryActivity.this, "常见问题", SdkApi.getDrawCashQR());
                break;
        }
    }
    private class DrawCashHistoryAdapter extends BaseQuickAdapter<DrawCashHistoryResultBean, BaseViewHolder>{

        public DrawCashHistoryAdapter(Context context, List data){
            super(R.layout.draw_cash_item_history, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, DrawCashHistoryResultBean item) {

            SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            long lcc = Long.valueOf(item.getCreate_time());
            String times = sdr.format(new Date(lcc * 1000L));

            ((TextView) helper.getView(R.id.tv_time)).setText(times);
            ((TextView) helper.getView(R.id.tv_amount_desc)) .setText("零钱 "+item.getAmount());
            ((TextView) helper.getView(R.id.tv_amount)) .setText("¥"+item.getReal_amount());

            if(item.getType()==1){
                ((ImageView)helper.getView(R.id.iv_icon)).setImageResource(R.mipmap.drawcash_weixin);
            }else{
                ((ImageView)helper.getView(R.id.iv_icon)).setImageResource(R.mipmap.drawcash_alipay);
            }

            if(item.getStatus()==1){
                ((ImageView)helper.getView(R.id.iv_status)).setImageResource(R.mipmap.drawcash_status_doing);
            }else if (item.getStatus()==2){
                ((ImageView)helper.getView(R.id.iv_status)).setImageResource(R.mipmap.drawcash_status_finish);
            }else if (item.getStatus()==3){
                ((ImageView)helper.getView(R.id.iv_status)).setImageResource(R.mipmap.drawcash_status_unarrive);
            }
        }
    }

}
