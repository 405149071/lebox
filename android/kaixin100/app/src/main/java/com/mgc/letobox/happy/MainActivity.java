package com.mgc.letobox.happy;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.RequestQueue;
import com.ledong.lib.leto.db.LoginControl;
import com.leto.game.base.bean.BaseRequestBean;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.leto.game.base.http.SdkConstant;
import com.leto.game.base.util.BaseAppUtil;

import com.liang530.log.L;
import com.liang530.log.T;
import com.mgc.letobox.happy.base.BaseActivity;
import com.mgc.letobox.happy.config.SdkApi;
import com.mgc.letobox.happy.domain.BitMapResponse;
import com.mgc.letobox.happy.domain.LuckyListReponse;
import com.mgc.letobox.happy.domain.RewardResultBean;
import com.mgc.letobox.happy.domain.StartUpBean;
import com.mgc.letobox.happy.domain.StartupResultBean;
import com.mgc.letobox.happy.receiver.AppInstallReceiver;
import com.mgc.letobox.happy.util.GsonUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,EasyPermissions.PermissionCallbacks{

    private final static  String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.container)
    FrameLayout mViewPager;
    @BindView(R.id.tab_btn1)
    RadioButton tabBtn1;
    @BindView(R.id.tab_btn2)
    RadioButton tabBtn2;
    @BindView(R.id.tab_btn3)
    RadioButton tabBtn3;
    @BindView(R.id.tab_btn4)
    RadioButton tabBtn5;
    @BindView(R.id.tab_group)
    RadioGroup tabGroup;

    Fragment  curFragment;
    TabReadFragment readFragment;
    TabWalfareFragment walfareFragment;
    TabMiniGameFragment miniGameFragment;
    TabProfileFragment profileFragment;


    public static final String EXTRA_HIDE_SPLASH = "hide_splash";
    public static final String EXTRA_LOGIN_REWARD = "login_reward";

    public Context mContext;

    boolean isCheckedVersion =false;

    Dialog openDialog;

    private AppInstallReceiver mAppInstallReceiver;


    RewardResultBean loginReward;

    public static Activity sInstance = null;

    public int mTabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.happy_activity_main);
        L.i(TAG, "onCreate");
        ButterKnife.bind(this);
        mContext = this;
        sInstance = this;

//        EventBus.getDefault().register(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tabGroup.setOnCheckedChangeListener(this);
        tabGroup.check(R.id.tab_btn1);

        //8.0之后禁止使用静态注册
        mAppInstallReceiver = new AppInstallReceiver();
        mAppInstallReceiver.register(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(SdkConstant.ACTION_SESSION_EXPIRED));

        if(null!=getIntent()){
            loginReward = (RewardResultBean) getIntent().getSerializableExtra(EXTRA_LOGIN_REWARD);
        }

        initLottery();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getStringExtra("action");
        if ("force_kill".equals(action)) {
            // 在 ProfileActivity 被强杀了就重新走应用的流程
            protectApp();
        }
    }

    @Override
    protected void protectApp() {
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");

//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this);
//        }

        mAppInstallReceiver.unregister(this);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);

//        FloatActionController.getInstance().stopMonkServer(MainActivity.this);

//        if(downModelIntent!=null)
//        stopService(downModelIntent);

        sInstance = null;

    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case SdkConstant.ACTION_SESSION_EXPIRED: {
                    callStartup();
                    finish();
                }
                break;
            }
        }
    };

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        Fragment fragment = null;
        switch (i) {
            case R.id.tab_btn1:
                //StatusBarUtil.setTransparentForImageView(MainActivity.this, null);
                StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
                if(null == miniGameFragment) {
                    miniGameFragment = TabMiniGameFragment.newInstance();
                }
                fragment = miniGameFragment;
                mTabIndex = 0;
                break;
            case R.id.tab_btn2:
                MobclickAgent.onEvent(mContext,"mgc_wallet_game_store_enter_click");
                StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
                if(null == readFragment) {
                    readFragment = TabReadFragment.newInstance();
                }
                fragment = readFragment;
                mTabIndex = 1;
                break;
            case R.id.tab_btn3:
                StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
                MobclickAgent.onEvent(mContext,"mgc_wallet_property_enter_click");
                if(null == walfareFragment) {
                    walfareFragment = TabWalfareFragment.newInstance();
                }
                fragment = walfareFragment;
                mTabIndex = 2;
                break;
            case R.id.tab_btn4:
                MobclickAgent.onEvent(mContext,"mgc_wallet_setting_enter_click");
                //StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
                if(null == profileFragment) {
                    profileFragment = TabProfileFragment.newInstance();
                }
                fragment = profileFragment;
                mTabIndex = 3;
                break;

        }

        FragmentTransaction  fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(!fragment.isAdded()){
            fragmentTransaction.add(R.id.container, fragment);
        }
        if(curFragment==fragment){
            return;
        }

        if(null!=curFragment){
            //fragmentTransaction.hide(curFragment).show(fragment).replace(R.id.container, fragment).commit();
            fragmentTransaction.hide(curFragment).show(fragment).commit();
        }else{
            fragmentTransaction.commit();
        }

        curFragment = fragment;
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //Toast.makeText(this, "没有权限， 无法进入", Toast.LENGTH_SHORT).show();

        afterStartup();

    }

    @AfterPermissionGranted(100)
    void afterPermissionCheck() {

        try {
            RxVolley.setRequestQueue(RequestQueue.newRequestQueue(BaseAppUtil.getDefaultSaveRootPath(this,"mgc")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        SdkConstant.userToken = LoginControl.getUserToken();
        if (TextUtils.isEmpty(SdkConstant.userToken)) {
            callStartup();
        } else {
            afterStartup();
        }
        //启动时初始化agent
        SdkConstant.MGC_AGENT = LoginControl.getAgentgame();

    }

    void afterStartup() {

        onCheckedChanged(tabGroup, R.id.tab_btn1);

    }

    void callStartup () {
        StartUpBean startUpBean = new StartUpBean();
        int open_cnt = 1;
        startUpBean.setOpen_cnt(open_cnt + "");
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(startUpBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<StartupResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(StartupResultBean data) {
                if (data != null) {
                    LoginControl.saveUserToken(data.getUser_token());
                    SdkConstant.userToken = data.getUser_token();
                    SdkConstant.SERVER_TIME_INTERVAL = data.getTimestamp() - System.currentTimeMillis();
                    LoginActivity.goLogin(MainActivity.this);
                }
            }
            @Override
            public void onFailure(String code, String msg) {
                T.s(MainActivity.this, "初始化失败, 请重新打开App");
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);//当前Splash显示中
        RxVolley.post(SdkApi.getStartup(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    //仅提供给外部类使用
    public void gotoSetting(){
        if(tabGroup!=null) {
            tabGroup.check(R.id.tab_btn4);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        L.i(TAG, "onResume");
        if(!LoginControl.isLogin()) {
           return;
        }

//        if (tabGroup != null) {
//
//            if(mTabIndex==3){
//                tabGroup.check(R.id.tab_btn4);
//            }
//
//            switch (SignInActivity.SignInClick) {
//                case SignInActivity.SignInOne:
//                    SignInActivity.SignInClick = 0;
//                    tabGroup.check(R.id.tab_btn1);
//                    break;
//                case SignInActivity.SignInTwo:
//                    tabGroup.check(R.id.tab_btn2);
//                    break;
//                case SignInActivity.SignInThree:
//                    tabGroup.check(R.id.tab_btn2);
//                    break;
//                case SignInActivity.SignInFour:
//                    tabGroup.check(R.id.tab_btn2);
//                    break;
//                case SignInActivity.SignInFive:
//                    tabGroup.check(R.id.tab_btn2);
//                    break;
//            }
//        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        L.i(TAG, "onRestart");
    }

    @Override
    public void onStop() {
        super.onStop();
        L.i(TAG, "onStop");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }


    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_MENU) {
            // TODO: 处理菜单键的点击事件
            Toast.makeText(getApplicationContext(), "菜单键", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTrimMemory(int level){
        super.onTrimMemory(level);
        if(level==TRIM_MEMORY_UI_HIDDEN){
            Glide.get(this).clearMemory();
        }
//        Glide.get(this).onTrimMemory(level);
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }


    private void initLottery() {
        BaseRequestBean requestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<List<LuckyListReponse>>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(List<LuckyListReponse> data) {
                if (data != null) {
                    Gson gson = new Gson();
                    String str = gson.toJson(data);
                    final List<LuckyListReponse> responses = gson.fromJson(str, new TypeToken<List<LuckyListReponse>>() {
                    }.getType());
                    LetoApplication.getInstance().setmLuckResponse(responses);
                    final List<BitMapResponse> mBitmaps = new ArrayList<>();
                    int size = responses.size();
                    for (int i = 0; i < size; i++) {
                        final int finalI = i;
                        Glide.with(mContext).load(responses.get(i).getPic()).asBitmap().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                BitMapResponse bitMapResponse = new BitMapResponse();
                                bitMapResponse.setBitmap(resource);
                                bitMapResponse.setId(responses.get(finalI).getId());
                                mBitmaps.add(bitMapResponse);
                                LetoApplication.getInstance().setBitmaps(mBitmaps);
                            }
                        });
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
        RxVolley.post(SdkApi.getLotteryList(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
}
