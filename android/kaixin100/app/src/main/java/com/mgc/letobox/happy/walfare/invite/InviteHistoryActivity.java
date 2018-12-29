package com.mgc.letobox.happy.walfare.invite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
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
import com.mgc.letobox.happy.base.BaseActivity;
import com.mgc.letobox.happy.config.SdkApi;
import com.mgc.letobox.happy.domain.InviteHistoryBean;
import com.mgc.letobox.happy.domain.ListBaseRequestBean;
import com.mgc.letobox.happy.util.DateUtil;
import com.mgc.letobox.happy.util.GsonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by zhaozhihui on 2018/8/30
 **/
public class InviteHistoryActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_right)
    TextView tv_right;

    @BindView(R.id.rl_left)
    RelativeLayout rl_left;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    InviteAdapter mAdapter;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    int  mPage =1;

    private List<InviteHistoryBean> mList = new ArrayList<>();



    public static void start(Context context){
        context.startActivity(new Intent(context, InviteHistoryActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invite_history);
        ButterKnife.bind(this);

        StatusBarUtil.setTransparentForImageView(this, null);
        initView();

    }

    private void initView() {
        tv_title.setText("我的邀请");
        tv_title.setTextColor(getResources().getColor(R.color.white));
        tv_right.setOnClickListener(this);
        rl_left.setOnClickListener(this);

        mAdapter = new InviteAdapter(InviteHistoryActivity.this, mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);


//        View header = LayoutInflater.from(InviteHistoryActivity.this).inflate(R.layout.invite_item_history_header, null);
//        mAdapter.addHeaderView(header);

        View mView = LayoutInflater.from(this).inflate(R.layout.center_no_data, null);
        ((TextView) mView.findViewById(R.id.tv_no_comment)).setText("暂无记录");
        ((RelativeLayout) mView.findViewById(R.id.rl_no_data)).setBackgroundColor(getResources().getColor(R.color.transparent));
        mAdapter.setEmptyView(mView);

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPage++;
                getPageData(mPage);
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                mPage = 1;
                getPageData(mPage);
            }
        });
        refreshLayout.autoRefresh();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_left:
                finish();
                break;
        }
    }

    /**
     * 我的中奖纪录
     */
    private void getPageData(int page) {
        ListBaseRequestBean requestBean = new ListBaseRequestBean();
        requestBean.setPage(page);
        requestBean.setOffset(10);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<List<InviteHistoryBean>>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(List<InviteHistoryBean> data) {
                if (data != null) {
                    mList.clear();
                    Gson gson = new Gson();
                    String str = gson.toJson(data);
                    List<InviteHistoryBean> responses = gson.fromJson(str, new TypeToken<List<InviteHistoryBean>>() {
                    }.getType());

                    refreshLayout.finishRefresh();
                    if (responses != null && responses.size() != 0) {
                        if (mPage == 1) {
                            mAdapter.setNewData(responses);
                            mAdapter.notifyDataSetChanged();

                            refreshLayout.setNoMoreData(false);
                        } else {
                            mAdapter.addData(responses);
                            if (responses.size() < 10) {
                                refreshLayout.finishRefresh();
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            }else{
                                refreshLayout.finishRefresh();
                                refreshLayout.setNoMoreData(false);
                            }
                        }
                    } else {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore(0);
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else {
                    refreshLayout.finishRefresh();
                    if (mList != null && mList.size() == 0) {
                        refreshLayout.setNoMoreData(true);
                    }
                    refreshLayout.finishLoadMore(0);
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
//                if (refreshLayout != null) {
//                    refreshLayout.finishRefresh();
//                    refreshLayout.finishLoadMore();
//                    refreshLayout.finishLoadMoreWithNoMoreData();
//                }
            }
        };
        RxVolley.post(SdkApi.getMyInvite(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }




    private class InviteAdapter extends BaseQuickAdapter<InviteHistoryBean, BaseViewHolder> {

        Context mContext;

        public InviteAdapter(Context context, @Nullable List<InviteHistoryBean> data) {
            super(R.layout.invite_item_history, data);
            mContext = context;
        }

        @Override
        protected void convert(BaseViewHolder helper, InviteHistoryBean item) {

            ((TextView) helper.getView(R.id.tv_name)).setText(item.getNickname());
            ((TextView) helper.getView(R.id.tv_statu)).setText(item.getStatus());
            String time = DateUtil.timesTwo(""+item.getTime());
            ((TextView) helper.getView(R.id.tv_time)).setText(time);


        }
    }
}
