package com.mgc.letobox.happy.walfare.drawcash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.kymjs.rxvolley.RxVolley;
import com.leto.game.base.bean.BaseRequestBean;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.mgc.letobox.happy.R;
import com.liang530.log.T;
import com.mgc.letobox.happy.base.BaseActivity;
import com.mgc.letobox.happy.HelpActivity;
import com.mgc.letobox.happy.config.SdkApi;
import com.mgc.letobox.happy.domain.DrawCashAlipayInfoResultBean;
import com.mgc.letobox.happy.domain.DrawCashBindWeixinRequestBean;
import com.mgc.letobox.happy.domain.DrawCashWeixinInfoResultBean;
import com.mgc.letobox.happy.domain.TTResultBean;
import com.mgc.letobox.happy.util.GsonUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by zhaozhihui on 2018/8/23
 **/
public class DrawCashActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.rl_left)
    RelativeLayout rl_left;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_balance)
    TextView tv_balance;



    @BindView(R.id.fl_alipay)
    FrameLayout fl_alipay;

    @BindView(R.id.fl_weixin)
    FrameLayout fl_weixin;

    @BindView(R.id.fl_record)
    FrameLayout fl_record;

    @BindView(R.id.fl_qr)
    FrameLayout fl_qr;


    DrawCashAlipayInfoResultBean mAlipayInfo;
    DrawCashWeixinInfoResultBean mWeixinInfo;


    public static void start(Context mContext) {
        Intent mIntent = new Intent(mContext, DrawCashActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_cash);
        ButterKnife.bind(this);

        StatusBarUtil.setTransparentForImageView(this, null);

        initView();

        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        tv_title.setText("我要提现");
        rl_left.setOnClickListener(this);
        fl_alipay.setOnClickListener(this);
        fl_weixin.setOnClickListener(this);
        fl_record.setOnClickListener(this);
        fl_qr.setOnClickListener(this);
        //fl_weixin.setVisibility(View.GONE);
    }


    /**
     * 个人任务列表
     */
    private void initData() {
        BaseRequestBean requestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<TTResultBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(TTResultBean data) {
                if (data != null) {
                    tv_balance.setText("" + data.getBalance_rmb());
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
        RxVolley.post(SdkApi.getDrawCashTTBalance(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_left:
                finish();
                break;
            case R.id.fl_alipay:
                getAlipayInfo();
                break;
            case R.id.fl_weixin:
                getWeixinInfo();
                break;
            case R.id.fl_record:
                DrawCashHistoryActivity.start(DrawCashActivity.this);
                break;
            case R.id.fl_qr:
                HelpActivity.start(DrawCashActivity.this, "常见问题", SdkApi.getDrawCashQR());
                break;
        }
    }


    private void getAlipayInfo() {
        BaseRequestBean requestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<DrawCashAlipayInfoResultBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(DrawCashAlipayInfoResultBean data) {
                if (data != null) {
                    mAlipayInfo = data;
                    if (TextUtils.isEmpty(data.getAccount())) {
                        Intent mIntent = new Intent(mContext, BindAlipayActivity.class);
                        startActivityForResult(mIntent, 2);
                    } else {
                        DrawCashAlipayActivity.start(DrawCashActivity.this, data);
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
        RxVolley.post(SdkApi.getDrawCashAlipayInfo(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        // operation succeeded. 默认值是-1
        if (resultCode == 2) {
            String alipay = data.getStringExtra("alipay");
            mAlipayInfo.setAccount(alipay);
            DrawCashAlipayActivity.start(DrawCashActivity.this, mAlipayInfo);

        }
    }


    private void getWeixinInfo() {
        BaseRequestBean requestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<DrawCashWeixinInfoResultBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(DrawCashWeixinInfoResultBean data) {
                if (data != null) {
                    mWeixinInfo = data;
                    if (TextUtils.isEmpty(data.getOpenid())) {
                        getWexiXinAuthInfo();
                    } else if (data.getIs_sub() != 1) {
                        BindWeixinActivity.start(DrawCashActivity.this, mWeixinInfo);
                    } else {
                        DrawCashWeixinActivity.start(DrawCashActivity.this, data);
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
        RxVolley.post(SdkApi.getDrawCashWeixinInfo(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void getWexiXinAuthInfo() {

        UMAuthListener authListener = new UMAuthListener() {
            /**
             * @desc 授权开始的回调
             * @param platform 平台名称
             */
            @Override
            public void onStart(SHARE_MEDIA platform) {

            }

            /**
             * @desc 授权成功的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param data 用户资料返回
             */
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

                mWeixinInfo.setHeadimgurl(data.get("iconurl"));
                mWeixinInfo.setNickname(data.get("name"));
                bindWeiXin(data);

            }

            /**
             * @desc 授权失败的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param t 错误原因
             */
            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {

                Toast.makeText(mContext, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }

            /**
             * @desc 授权取消的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             */
            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                Toast.makeText(mContext, "取消了", Toast.LENGTH_LONG).show();
            }
        };

        UMShareAPI umShareAPI = UMShareAPI.get(DrawCashActivity.this);

        umShareAPI.getPlatformInfo(DrawCashActivity.this, SHARE_MEDIA.WEIXIN, authListener);
    }


    private void bindWeiXin(Map<String, String> data) {
        DrawCashBindWeixinRequestBean requestBean = new DrawCashBindWeixinRequestBean();
        requestBean.setCity(data.get("city"));
        requestBean.setCountry(data.get("country"));
        requestBean.setOpenid(data.get("openid"));
        requestBean.setLanguage(data.get("language"));
        requestBean.setHeadimgurl(data.get("iconurl"));
        requestBean.setNickname(data.get("name"));
        requestBean.setProvince(data.get("province"));
        requestBean.setSex(data.get("gender"));
        requestBean.setUnionid(data.get("unionid"));
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<String>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(String data) {

                if (mWeixinInfo.getIs_sub() != 1) {
                    BindWeixinActivity.start(DrawCashActivity.this, mWeixinInfo);
                } else {
                    DrawCashWeixinActivity.start(DrawCashActivity.this, mWeixinInfo);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                if (!DrawCashActivity.this.isDestroyed()) {
                    T.s(DrawCashActivity.this, msg);
                }
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(SdkApi.getDrawCashBindWeixin(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
}
