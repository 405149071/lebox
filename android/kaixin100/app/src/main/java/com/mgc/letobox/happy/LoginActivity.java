package com.mgc.letobox.happy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kymjs.rxvolley.RxVolley;
import com.ledong.lib.leto.api.bean.LoginResultBean;
import com.ledong.lib.leto.db.LoginControl;
import com.ledong.lib.leto.model.LoginMobileRequestBean;
import com.ledong.lib.leto.model.SmsSendRequestBean;
import com.ledong.lib.leto.model.SmsSendResultBean;
import com.ledong.lib.leto.utils.RegExpUtil;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.leto.game.base.http.SdkConstant;
import com.liang530.log.T;
import com.mgc.letobox.happy.base.BaseActivity;
import com.mgc.letobox.happy.config.SdkApi;
import com.mgc.letobox.happy.domain.RewardResultBean;
import com.mgc.letobox.happy.util.GsonUtil;
import com.mgc.letobox.happy.util.RxVolleyUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends BaseActivity implements View.OnClickListener {

//    @BindView(R.id.country_code_textview)
//    TextView countryCodeTextview;
    @BindView(R.id.mgc_sdk_et_loginAccount)
    EditText loginMobileField;
    @BindView(R.id.mgc_sdk_et_sms_code)
    EditText loginCodeField;
    @BindView(R.id.mgc_sdk_btn_loginSubmit)
    Button loginOk;


    @BindView(R.id.mgc_sdk_btn_send_sms_code)
    Button getSmsButton;
    RewardResultBean rewardResultBean;

    int count=0;

    public static int sms_time = 120;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        loginOk.setOnClickListener(this);
        getSmsButton.setOnClickListener(this);

    }

    @Override
    public void setStatusBar(){
        //StatusBarUtil.setTransparentForImageView(this, null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mgc_sdk_btn_loginSubmit:
                MobclickAgent.onEvent(mContext,"mgc_wallet_login_click");
                login();
                break;
            case R.id.mgc_sdk_btn_send_sms_code:
                sendSMS();
                break;
        }
    }

    void sendSMS() {
        final String account = loginMobileField.getText().toString().trim();
        if (!RegExpUtil.isMobileNumber(account)) {
            T.s(this, "请输入正确的手机号");
            return;
        }
        SmsSendRequestBean smsSendRequestBean = new SmsSendRequestBean();
        smsSendRequestBean.setMobile(account);
        smsSendRequestBean.setSmstype( SmsSendRequestBean.TYPE_LOGIN);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(smsSendRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<SmsSendResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(SmsSendResultBean data) {
                if (data != null) {
                    //开始计时控件
                    startCodeTime(sms_time);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("发送中...");
        RxVolley.post(SdkApi.getSmsSend(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    Handler handler = new Handler();

    private void startCodeTime(int time) {
        getSmsButton.setTag(time);
        if (time <= 0) {
            getSmsButton.setText("获取验证码");
            getSmsButton.setClickable(true);
            return;
        } else {
            getSmsButton.setClickable(false);
            getSmsButton.setText(time + "秒");
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int delayTime = (int) getSmsButton.getTag();
                startCodeTime(--delayTime);

            }
        }, 1000);
    }

    private void login() {
        final String account = loginMobileField.getText().toString().trim();
        String authCode = loginCodeField.getText().toString().trim();
        if (!RegExpUtil.isMobileNumber(account)) {
            T.s(this, "请输入正确的手机号");
            return;
        }

        if (TextUtils.isEmpty(authCode)) {
            T.s(this, "请先输入验证码");
            return;
        }

        LoginMobileRequestBean loginMobileRequestBean = new LoginMobileRequestBean();
        loginMobileRequestBean.setMobile(account);
        loginMobileRequestBean.setSmscode(authCode);
        loginMobileRequestBean.setSmstype(SmsSendRequestBean.TYPE_LOGIN);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(loginMobileRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<LoginResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(LoginResultBean data) {
                if (data != null) {
//                    T.s(loginActivity,"登陆成功："+data.getCp_user_token());
                    //接口回调通知
//                    LoginControl.saveUserInfo(data.getCp_user_token(), account, data.getPublic_key());
                    LoginControl.setPortrait(data.getPortrait());   //保存头像到本地
                    //LoginControl.setAgentgame(data.getAgentgame());   //保存到本地
                    //SdkConstant.HS_AGENT = data.getAgentgame();
                    LoginControl.setNickname(data.getNickname());   //保存到本地
                    LoginControl.saveUserToken(data.getUser_token());
                    LoginControl.setUserId(account);
//                        LoginControl.setUserId(data.getMem_id());
//                    LoginControl.setSnsAgentId(data.getIs_agent());
//                    LoginControl.setSignature(data.getSignature());
//                    onLoginSuccReward(data.getReward());

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onFailure(String code, String message){
                Log.i(TAG, message);
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("登录中...");
        new RxVolleyUtil().post(SdkApi.getLoginRegister(), httpParamsBuild.getHttpParams(), httpCallbackDecode);

    }

    public void onLoginSucc() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_HIDE_SPLASH, true);
        startActivity(intent);
        finish();
    }

    public void onLoginSuccReward(RewardResultBean data) {
        rewardResultBean = data;
        SdkConstant.MGC_AGENT = LoginControl.getAgentgame();
        SdkConstant.userToken = LoginControl.getUserToken();
    }

    public static void goLogin(Context context){
       Intent intent =  new Intent(context, LoginActivity.class);
       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
