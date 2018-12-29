package com.mgc.letobox.happy.walfare.invite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.kymjs.rxvolley.RxVolley;
import com.leto.game.base.bean.BaseRequestBean;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.mgc.letobox.happy.R;
import com.liang530.log.T;
import com.mgc.letobox.happy.base.BaseActivity;
import com.mgc.letobox.happy.config.SdkApi;
import com.mgc.letobox.happy.domain.InviteInfoBean;
import com.mgc.letobox.happy.ui.dialog.SharePlatformDialog;
import com.mgc.letobox.happy.ui.dialog.ShareTypeDialog;
import com.mgc.letobox.happy.util.GsonUtil;
import com.mgc.letobox.happy.util.ShareUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by zhaozhihui on 2018/8/30
 **/
public class InviteActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_right)
    TextView tv_right;

    @BindView(R.id.rl_left)
    RelativeLayout rl_left;

    @BindView(R.id.tv_balance)
    TextView tv_balance;

    @BindView(R.id.tv_number)
    TextView tv_number;

    @BindView(R.id.ib_share)
    ImageView ib_share;

    InviteInfoBean mInviteInfoBean;


    public static void start(Context context) {
        context.startActivity(new Intent(context, InviteActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invite);
        ButterKnife.bind(this);

        StatusBarUtil.setTransparentForImageView(this, null);
        initView();
    }

    private void initView() {
        tv_title.setText("邀请好友");
        tv_title.setTextColor(getResources().getColor(R.color.white));
        tv_right.setText("我的邀请");
        tv_right.setTextColor(getResources().getColor(R.color.white));
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);
        rl_left.setOnClickListener(this);
        ib_share.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();

        getInviteInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_left:
                finish();
                break;
            case R.id.tv_right:
                InviteHistoryActivity.start(InviteActivity.this);

                break;
            case R.id.ib_share:
                if(mInviteInfoBean!=null) {
                    new ShareTypeDialog().showDialog(InviteActivity.this, mInviteInfoBean.getShare_url(), new ShareTypeDialog.ConfirmDialogListener() {
                        @Override
                        public void setShareType(int type, String filePath) {
                            showSharePlatform(type, mInviteInfoBean.getShare_url(), filePath);
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                }
                break;
        }
    }

    private void getInviteInfo() {

        BaseRequestBean requestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<InviteInfoBean>(InviteActivity.this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(InviteInfoBean data) {
                if (data != null) {
                    mInviteInfoBean = data;
                    tv_balance.setText("" + data.getAward());
                    tv_number.setText("" + data.getCountMem());
                }
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        httpCallbackDecode.setLoadMsg("加载中...");
        RxVolley.post(SdkApi.getInviteStatistic(), httpParamsBuild.getHttpParams(), httpCallbackDecode);

    }

    private void showSharePlatform(final int type, final String url, final String filePath) {
        new SharePlatformDialog().showDialog(InviteActivity.this, null, new SharePlatformDialog.ConfirmDialogListener() {
            @Override
            public void setPlatform(SHARE_MEDIA platform) {

                String shareImage = null;
                String shareTitle = getResources().getString(R.string.app_name);
                String shareUrl = url;
                String shareContent = getResources().getString(R.string.share_picture_text);

                if (type == 0) {
                    shareImage = filePath;
                    ShareUtil.shareImage(InviteActivity.this, platform, new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {
//                            FloatActionController.getInstance().hide();
                        }

                        @Override
                        public void onResult(SHARE_MEDIA share_media) {
                            T.s(InviteActivity.this, "分享成功");
                        }

                        @Override
                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            T.s(InviteActivity.this, "分享失败：" + throwable.getMessage());
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media) {
                            T.s(InviteActivity.this, "分享取消");
                        }
                    }, shareImage, "", shareContent);

                } else {
                    ShareUtil.shareToPlatform(InviteActivity.this, platform, new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {
//                            FloatActionController.getInstance().hide();
                        }

                        @Override
                        public void onResult(SHARE_MEDIA share_media) {
                            T.s(InviteActivity.this, "分享成功");
                        }

                        @Override
                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            T.s(InviteActivity.this, "分享失败：" + throwable.getMessage());
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media) {
                            T.s(InviteActivity.this, "分享取消");
                        }
                    }, shareUrl, shareTitle, shareContent, shareImage);
                }

            }

            @Override
            public void cancel() {

            }
        });
    }
}
