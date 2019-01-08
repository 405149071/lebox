package com.mgc.letobox.happy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.kymjs.rxvolley.RxVolley;
import com.ledong.lib.leto.db.LoginControl;
import com.ledong.lib.leto.main.WebViewActivity;
import com.leto.game.base.bean.BaseRequestBean;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.mgc.letobox.happy.base.AutoLazyFragment;
import com.mgc.letobox.happy.config.SdkApi;
import com.mgc.letobox.happy.domain.HomeMgcPropertyResultBean;
import com.mgc.letobox.happy.game.HistoryGameActivity;
import com.mgc.letobox.happy.util.GsonUtil;
import com.mgc.letobox.happy.walfare.drawcash.DrawCashActivity;
import com.mgc.letobox.happy.walfare.invite.InviteActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TabProfileFragment extends AutoLazyFragment implements View.OnClickListener {



    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_golden)
    TextView tv_golden;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_return)
    ImageView iv_return;

    @BindView(R.id.fl_drawcash)
    FrameLayout fl_drawcash;
    @BindView(R.id.fl_invite)
    FrameLayout fl_invite;
    @BindView(R.id.fl_games)
    FrameLayout fl_games;
    @BindView(R.id.fl_read)
    FrameLayout fl_read;
    @BindView(R.id.fl_exit)
    FrameLayout fl_exit;


    public static TabProfileFragment newInstance() {
        TabProfileFragment fragment = new TabProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        tv_title.setText("我的");
        iv_return.setVisibility(View.INVISIBLE);

//        tv_right.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.fl_drawcash, R.id.fl_invite, R.id.fl_games, R.id.fl_read, R.id.fl_exit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_drawcash:
                DrawCashActivity.start(getContext());
                break;
            case R.id.fl_invite:
                InviteActivity.start(getContext());
                break;
            case R.id.fl_games:
                HistoryGameActivity.start(getContext());
                break;
            case R.id.fl_read:

                WebViewActivity.start(getContext(), "小说", SdkApi.novel_url, 4, 1, LetoApplication.getInstance().getAppId(), LetoApplication.getInstance().getAppId());

                break;
            case R.id.fl_exit:
                LoginControl.clearLogin();
                LoginActivity.goLogin(getContext());
                getActivity().finish();
                break;
        }

    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, TabProfileFragment.class);
        context.startActivity(intent);
    }


    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        loadMgcProperty();
    }

    public void loadMgcProperty() {
        if (!LoginControl.isLogin()) {
            return;
        }
        BaseRequestBean requestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<HomeMgcPropertyResultBean>(getActivity(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(HomeMgcPropertyResultBean data) {
                try {
                    if (data != null) {
                        updateProperty(data);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(SdkApi.getMgcProperty(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void updateProperty(HomeMgcPropertyResultBean data) {
        tv_money.setText(String.valueOf(data.getTtAmount()));
        tv_golden.setText(String.valueOf(data.getTtProperty()));
    }

}
