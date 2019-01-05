package com.mgc.letobox.happy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.kymjs.rxvolley.RxVolley;
import com.leto.game.base.bean.BaseRequestBean;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.leto.game.base.util.MResource;
import com.liang530.log.T;
import com.mgc.letobox.happy.R;
import com.liang530.utils.BaseAppUtil;

import com.mgc.letobox.happy.config.SdkApi;
import com.mgc.letobox.happy.domain.DataResponse;
import com.mgc.letobox.happy.domain.ListBaseRequestBean;
import com.mgc.letobox.happy.domain.SignInRequestBean;
import com.mgc.letobox.happy.domain.SignResponse;
import com.mgc.letobox.happy.domain.WalfareBean;
import com.mgc.letobox.happy.ui.SpacesItemDecoration;
import com.mgc.letobox.happy.ui.TranslucentActionBar;
import com.mgc.letobox.happy.ui.TranslucentScrollView;
import com.mgc.letobox.happy.ui.dialog.CircleDialogUtils;
import com.mgc.letobox.happy.ui.recyclerview.BaseRecyclerAdapter;
import com.mgc.letobox.happy.ui.signin.SignInItem;
import com.mgc.letobox.happy.util.GsonUtil;
import com.mgc.letobox.happy.util.Util;
import com.mgc.letobox.happy.walfare.drawcash.BindAlipayActivity;
import com.mgc.letobox.happy.walfare.drawcash.DrawCashActivity;
import com.mgc.letobox.happy.walfare.invite.InviteActivity;
import com.mgc.letobox.happy.walfare.lucky.LotteryActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TabWalfareFragment extends Fragment implements SignInItem.ISignInListener {

    Unbinder unbinder;

    private TranslucentScrollView translucentScrollView;

    private TranslucentActionBar actionBar;


    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_return)
    ImageView iv_return;


    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mList;

    WalfareAdapter mAdapter;

    SignInItem day1;
    SignInItem day2;
    SignInItem day3;
    SignInItem day4;
    SignInItem day5;
    SignInItem day6;
    SignInItem day7;

    ImageView iv_signin;

    View headerView;
    private int sign_day;
    private int is_sign;
    private boolean isTaday;

    private LinearLayout linearView;

    SignResponse signResponse;

    List<WalfareBean> walfareBeanList = new ArrayList();

    @BindView(R.id.tv_no_comment)
    TextView tv_no_comment;

    @BindView(R.id.emptyView)
    View emptyView;

    private int mPage = 1;


    public static TabWalfareFragment newInstance() {
        TabWalfareFragment fragment = new TabWalfareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_walfare, container, false);
        unbinder = ButterKnife.bind(this, view);
//        EventBus.getDefault().register(this);

        iv_return.setVisibility(View.INVISIBLE);
        tv_title.setText("福利");

        setupUI();

        initNet();

        initSignInView();

        getWalfares(1);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    private void setupUI() {

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                mPage++;
                getWalfares(mPage);
            }
        });

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getWalfares(mPage);
            }
        });

//
//        SpacesItemDecoration decoration = new SpacesItemDecoration(BaseAppUtil.dip2px(getContext(), 6) / 2,true);
//        mList.addItemDecoration(decoration);


        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new WalfareAdapter(walfareBeanList);

        mList.setHasFixedSize(true);
        mList.setNestedScrollingEnabled(false);
        mList.setFocusable(false);
        mList.setAdapter(mAdapter);

        headerView = LayoutInflater.from(getContext()).inflate(R.layout.sign_in_layout, null);
        day1 = headerView.findViewById(R.id.day_1);
        day2 = headerView.findViewById(R.id.day_2);
        day3 = headerView.findViewById(R.id.day_3);
        day4 = headerView.findViewById(R.id.day_4);
        day5 = headerView.findViewById(R.id.day_5);
        day6 = headerView.findViewById(R.id.day_6);
        day7 = headerView.findViewById(R.id.day_7);
        iv_signin = headerView.findViewById(R.id.iv_signin);

        mAdapter.addHeaderView(headerView);
    }

    /**
     * 获取今日福利
     */
    private void getWalfares(int page) {
        ListBaseRequestBean requestBean = new ListBaseRequestBean();
        requestBean.setPage(page);
        requestBean.setOffset(30);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<List<WalfareBean>>(getContext(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(List<WalfareBean> data) {
                if (data != null) {
                    Gson gson = new Gson();
                    String str = gson.toJson(data);
                    List<WalfareBean> responses = gson.fromJson(str, new TypeToken<List<WalfareBean>>() {
                    }.getType());
                    if (responses != null && responses.size() != 0) {
                        if (mPage == 1) {
                            walfareBeanList.clear();
                            walfareBeanList.addAll(responses);
                            mAdapter.notifyDataSetChanged();
                            smartRefreshLayout.finishRefresh();
                            smartRefreshLayout.setNoMoreData(true);
                            smartRefreshLayout.finishLoadMoreWithNoMoreData();
                        } else {
                            walfareBeanList.addAll(responses);
                            mAdapter.notifyDataSetChanged();
                            smartRefreshLayout.finishRefresh();
                            smartRefreshLayout.finishLoadMore(0);
                            if (responses.size() < 10) {
                                smartRefreshLayout.finishLoadMoreWithNoMoreData();
                            }
                        }
                    } else {
                        smartRefreshLayout.finishRefresh();
                        smartRefreshLayout.finishLoadMore(0);
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else {
                    smartRefreshLayout.finishRefresh();
                    if (walfareBeanList != null && walfareBeanList.size() == 0) {
                        smartRefreshLayout.setNoMoreData(true);
                    }
                    smartRefreshLayout.finishLoadMore(0);
                    smartRefreshLayout.finishLoadMoreWithNoMoreData();
                }
                if (walfareBeanList.size() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    emptyView.setVisibility(View.GONE);
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
        RxVolley.post(SdkApi.getWalfares(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, TabWalfareFragment.class);
        context.startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void initSignInView() {

        int screen_width = BaseAppUtil.getDeviceWidth(getContext());

        int item_width = (screen_width - 3 * BaseAppUtil.dip2px(getContext(), 8) - 2 * BaseAppUtil.dip2px(getContext(), 14)) / 4;

        int item_height = item_width * 260 / 228;

        day1.setSize(item_width, item_height);
        day1.setRewardImage(R.mipmap.ic_sweet2);
        day1.setListener(this);
        day2.setSize(item_width, item_height);
        day2.setRewardImage(R.mipmap.ic_sweet2);
        day2.setListener(this);
        day3.setSize(item_width, item_height);
        day3.setRewardImage(R.mipmap.ic_sweet3);
        day3.setListener(this);
        day4.setSize(item_width, item_height);
        day4.setRewardImage(R.mipmap.ic_sweet3);
        day4.setListener(this);
        day5.setSize(item_width, item_height);
        day5.setRewardImage(R.mipmap.ic_sweet3);
        day5.setListener(this);
        day6.setSize(item_width, item_height);
        day6.setRewardImage(R.mipmap.ic_sweet3);
        day6.setListener(this);
        int big_width = 2 * item_width + BaseAppUtil.dip2px(getContext(), 8);

        day7.setSize(big_width, item_height);
        day7.setRewardImage(R.mipmap.ic_sweet4);
        day7.setListener(this);
    }

    private void initSignInData(SignResponse data) {
        int signDay = data.getSign_day();

        for (int i = 0; i < 7; i++) {
            if (i < signDay - 1) {
                data.getRules().get(i).setSign(true);
            } else if (i == signDay - 1) {
                isTaday = true;
                if (is_sign == 1) {
                    data.getRules().get(i).setSign(false);
                } else {
                    data.getRules().get(i).setSign(true);
                }
                data.getRules().get(i).setTaday(true);
            } else {
                data.getRules().get(i).setSign(false);
            }
            int imgId = MResource.getIdByName(getContext(), "id", "day_" + (i + 1));
            ((SignInItem) headerView.findViewById(imgId)).setSignInDay(data.getRules().get(i));
        }
    }

    /**
     * 获取签到信息
     */
    private void initNet() {
        BaseRequestBean requestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<SignResponse>(getContext(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(SignResponse data) {
                if (data != null) {
                    sign_day = data.getSign_day();
                    is_sign = data.getIs_sign();
                    signResponse = data;
//                    Glide.with(getActivity()).load(data.getPic()).into(iv_signin);

                }
                initSignInData(data);
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
        RxVolley.post(SdkApi.getSignList(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    /**
     * 签到
     */
    private void initSign(final int sign_day) {
        SignInRequestBean requestBean = new SignInRequestBean();
        requestBean.setSign_day(sign_day);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<DataResponse>(getContext(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(DataResponse data) {
                CircleDialogUtils.createToast(getContext(), null, "+" + signResponse.getRules().get(sign_day - 1).getAward() + "TT糖果");

                initNet();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                if (null != getActivity() && !getActivity().isDestroyed()) {
                    T.s(getActivity(), msg);
                }

            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(SdkApi.getSignIn(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    @Override
    public void onSignIn(int day) {
        if (day == sign_day && is_sign == 1) {
            initSign(day);
        }
    }


    private class WalfareAdapter extends BaseQuickAdapter<WalfareBean, BaseViewHolder> {
        private List<WalfareBean> datas;

        private Context mContext;

        public WalfareAdapter(@Nullable List<WalfareBean> data) {
            super(R.layout.item_walfare, data);
        }

        public List<WalfareBean> getDatas() {
            return datas;
        }

        @Override
        protected void convert(final BaseViewHolder helper, WalfareBean item) {


            View view = helper.getView(R.id.rl_root);
            ImageView iv_img1 = helper.getView(R.id.iv_img1);
            TextView tv_title_1 = helper.getView(R.id.tv_title_1);
            TextView tv_title_2 = helper.getView(R.id.tv_title_2);


            int screen_width = BaseAppUtil.getDeviceWidth(getContext());
            int width = screen_width - 2 * BaseAppUtil.dip2px(getContext(), 10);
            int height = width / 2;

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            params.height = height;
            params.width = width;
            view.setLayoutParams(params);

            tv_title_1.setText(item.getTitle());
            tv_title_2.setText(item.getContent());


            Glide.with(getContext()).load(item.getPic()).into(iv_img1);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (helper.getPosition() == 1) {
                        // LuckyActivity.start(WalfareActivity.this);
                        LotteryActivity.start(getContext());
                    } else if (helper.getPosition() == 2) {
                        DrawCashActivity.start(getContext());
//                    } else if (helper.getPosition() == 3) {
//                        //DrawCashActivity.start(WalfareActivity.this);
//                        //if(mGradeResultBean!=null)
//                        startActivity(new Intent(getContext(), RedpacketSendActivity.class));
                    } else if (helper.getPosition() == 3) {
                        //DrawCashActivity.start(WalfareActivity.this);
                        //if(mGradeResultBean!=null)
                        InviteActivity.start(getContext());
                    }
                }
            });
        }

    }
}
