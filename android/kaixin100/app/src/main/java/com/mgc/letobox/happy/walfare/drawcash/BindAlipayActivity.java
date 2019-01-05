package com.mgc.letobox.happy.walfare.drawcash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kymjs.rxvolley.RxVolley;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.mgc.letobox.happy.R;
import com.liang530.log.T;
import com.mgc.letobox.happy.base.BaseActivity;
import com.mgc.letobox.happy.config.SdkApi;
import com.mgc.letobox.happy.domain.DrawCashBindAlipayRequestBean;
import com.mgc.letobox.happy.util.GsonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by zhaozhihui on 2018/8/23
 **/
public class BindAlipayActivity  extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.rl_left)
    RelativeLayout rl_left;
    @BindView(R.id.btn_ok)
    Button btn_ok;
    @BindView(R.id.tv_title)
    TextView tv_title;

//    @BindView(R.id.et_account)
//    EditText et_account;

    @BindView(R.id.et_idcard)
    EditText et_idcard;


    public static void start(Context mContext) {
        Intent mIntent = new Intent(mContext, BindAlipayActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_alipay);
        ButterKnife.bind(this);

        initView();
    }


    private void initView(){
        rl_left.setOnClickListener(this);
        btn_ok.setOnClickListener(this);

        tv_title.setText("绑定支付宝账号");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_left:
                finish();
                break;
            case R.id.btn_ok:
                String idno = et_idcard.getText().toString();
                if(TextUtils.isEmpty(idno)){
                    T.s(mContext, "请输入支付宝账号");
                    return;
                }
                bindAlipay(idno);

                break;
        }
    }

    /**
     * 个人任务列表
     */
    private void bindAlipay(final String alipay) {
        DrawCashBindAlipayRequestBean requestBean = new DrawCashBindAlipayRequestBean();
        requestBean.setAccount(alipay);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<String >(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(String data) {
                Intent intent = new Intent();
                intent.putExtra("alipay", alipay);
                setResult(2, intent);
                finish();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                if (!BindAlipayActivity.this.isDestroyed()) {
                    T.s(BindAlipayActivity.this, msg);
                }
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(SdkApi.getDrawCashBindAlipay(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

}