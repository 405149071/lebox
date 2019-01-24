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

import com.ledong.lib.leto.interact.MgcLoginInteract;
import com.ledong.lib.leto.login.SendSmsInteract;
import com.ledong.lib.leto.utils.RegExpUtil;
import com.liang530.log.T;
import com.mgc.letobox.happy.base.BaseActivity;
import com.mgc.letobox.happy.domain.RewardResultBean;
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

        SendSmsInteract.sendSMS(LoginActivity.this, account, new SendSmsInteract.SendSmsListener() {
            @Override
            public void onSuccess() {
                startCodeTime(120);
            }

            @Override
            public void onFail(String code, String message) {

            }
        });
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

        MgcLoginInteract.submitLogin(LoginActivity.this, account, authCode, new MgcLoginInteract.LoginListener() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail(String code, String message) {
                Log.i(TAG, message);
            }
        });
    }

    public static void goLogin(Context context){
       Intent intent =  new Intent(context, LoginActivity.class);
       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
