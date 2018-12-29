package com.mgc.letobox.happy;

import com.jaeger.library.StatusBarUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.RequestQueue;
import com.ledong.lib.leto.db.LoginControl;
import com.ledong.lib.leto.main.BaseActivity;
import com.ledong.lib.leto.utils.ChannelNewUtil;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.leto.game.base.http.SdkConstant;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.DeviceUtil;
import com.leto.game.base.util.RSAUtils;
import com.mgc.letobox.happy.config.SdkApi;
import com.mgc.letobox.happy.domain.StartUpBean;
import com.mgc.letobox.happy.domain.StartupResultBean;
import com.mgc.letobox.happy.util.GsonUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

    private final static  String TAG = SplashActivity.class.getSimpleName();
    private ImageView iv_splash;

    private final int  START_MAIN =0;
    private final int  START_LOGIN =1;
    private final int  CHECK_VERSION =2;
    private final int  GO_APP = 3;
    private String mark = "";

    private static final int REQUEST_CODE_WRITE_PERMISSION = 2003;

    boolean isCheckedVersion =false;

    Context mContext;

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case SdkConstant.ACTION_SESSION_EXPIRED: {
                    callStartup();
                }
                break;
            }
        }
    };

    private Handler mHandler= new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case START_MAIN:
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                    break;

                case START_LOGIN:
                    Intent loginintent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(loginintent);
                    SplashActivity.this.finish();
                    break;
                case GO_APP:
                    if(LoginControl.isLogin()){
                        //启动时初始化agent
                        SdkConstant.MGC_AGENT = LoginControl.getAgentgame();
                        SdkConstant.userToken = LoginControl.getUserToken();
                        mHandler.sendEmptyMessageDelayed(START_MAIN, 1000);
                    }else{
                        mHandler.sendEmptyMessageDelayed(START_LOGIN, 1000);
                    }
                    break;
                default:
                    break;
            }
        };
    };

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        LetoApplication.flag = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = this;

        StatusBarUtil.setTransparentForImageView(this,null);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(SdkConstant.ACTION_SESSION_EXPIRED));

        if (SdkConstant.deviceBean == null) {
            SdkConstant.deviceBean = DeviceUtil.getDeviceBean(this);
        }
        SdkConstant.userToken = LoginControl.getUserToken();
        initAgent();


        initPermission();

        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        Log.i(TAG, "onDestroy");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);

    }


    private void initPermission() {
        //for test, can remove later
        String[] permissions = CheckPermissionUtils.checkPermission(this);
        if (permissions.length == 0) {
            afterPermissionCheck();
        } else {
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }


    public void initAgent(){
        //获取游戏渠道
        String agentgame_en = ChannelNewUtil.getChannel(this);
        String agentgame = "";
        try {
            agentgame =  new String (RSAUtils.decryptByPublicKey(agentgame_en.getBytes(), SdkConstant.RSA_PUBLIC_KEY));
            LoginControl.setAgentgame(agentgame);
            SdkConstant.MGC_AGENT = agentgame;
//            AppApi.agent = agentgame;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @AfterPermissionGranted(100)
    void afterPermissionCheck() {

        try {
            RxVolley.setRequestQueue(RequestQueue.newRequestQueue(BaseAppUtil.getDefaultSaveRootPath(this,"mgc")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        callStartup();

//        SdkConstant.userToken = LoginControl.getUserToken();
//        if (TextUtils.isEmpty(SdkConstant.userToken)) {
//            callStartup();
//        } else {
//            afterStartup();
//        }

    }

    void afterStartup() {

        mHandler.sendEmptyMessage(GO_APP);

    }

    public String getAppVersionName() {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = this.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(this.getPackageName(), 0);
            versionName = pi.versionName;
//			versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    void callStartup () {
        StartUpBean startUpBean = new StartUpBean();
        startUpBean.setUser_token(SdkConstant.userToken);
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

                    if(data.getIs_login()==1) {
                        mHandler.sendEmptyMessage(GO_APP);
                    }else{
                        mHandler.sendEmptyMessage(START_LOGIN);
                    }
                }
            }
            @Override
            public void onFailure(String code, String msg) {
                Toast.makeText(SplashActivity.this, "初始化失败, 请重新打开App", Toast.LENGTH_SHORT).show();
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(SdkApi.getStartup(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.i(TAG, "onPermissionsGranted");

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.i(TAG, "onPermissionsDenied");
        if(requestCode==100) {
            afterStartup();
        }else if(requestCode==2003){
            finish();
        }
    }
}
