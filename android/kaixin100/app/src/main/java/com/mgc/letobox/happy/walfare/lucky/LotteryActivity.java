package com.mgc.letobox.happy.walfare.lucky;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.kymjs.rxvolley.RxVolley;
import com.leto.game.base.bean.BaseRequestBean;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.mgc.letobox.happy.R;
import com.liang530.log.T;
import com.mgc.letobox.happy.LetoApplication;
import com.mgc.letobox.happy.base.BaseActivity;
import com.mgc.letobox.happy.config.SdkApi;
import com.mgc.letobox.happy.domain.BitMapResponse;
import com.mgc.letobox.happy.domain.ListBaseRequestBean;
import com.mgc.letobox.happy.domain.LuckyListReponse;
import com.mgc.letobox.happy.domain.ShareUrlResultBean;
import com.mgc.letobox.happy.listener.ActionBarClickListener;
import com.mgc.letobox.happy.ui.TranslucentActionBar;
import com.mgc.letobox.happy.ui.TranslucentScrollView;
import com.mgc.letobox.happy.ui.dialog.SharePlatformDialog;
import com.mgc.letobox.happy.ui.dialog.ShareTypeDialog;
import com.mgc.letobox.happy.ui.lottery.listener.RotateListener;
import com.mgc.letobox.happy.ui.lottery.view.WheelSurfView;
import com.mgc.letobox.happy.util.DimensionUtil;
import com.mgc.letobox.happy.util.GsonUtil;
import com.mgc.letobox.happy.util.ShareUtil;
import com.mgc.letobox.happy.walfare.lucky.dialog.LuckyDialog;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LotteryActivity extends BaseActivity implements ActionBarClickListener, TranslucentScrollView.TranslucentChangedListener {

    @BindView(R.id.mLotteryDisk)
    WheelSurfView mLotteryDisk;
    //    @BindView(R.id.goLuck)
//    ImageView goLuck;
//    @BindView(R.id.circle_name)
//    TextView circle_name;
//    @BindView(R.id.imageView_back)
//    ImageView imageView_back;
    @BindView(R.id.smoothScrollLayout)
    SmoothScrollLayout smoothScrollLayout;

    @BindView(R.id.actionbar)
    TranslucentActionBar actionBar;

    @BindView(R.id.tv_my_record)
    TextView tv_my_record;

    @BindView(R.id.tv_share)
    TextView tv_share;

    TranslucentScrollView sv_content;

    private List<LuckyListReponse> LuckyListReponses = new ArrayList<>();
    private List<BitMapResponse> mapResponses = new ArrayList<>();


    LuckyListReponse mReward;
    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);
        ButterKnife.bind(this);
        //StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));

        initActionBar();


        if (LetoApplication.getInstance().getBitmaps() != null && LetoApplication.getInstance().getBitmaps().size() == 6 && LetoApplication.getInstance().getmLuckResponse() != null && LetoApplication.getInstance().getmLuckResponse().size() != 0) {
            List<Bitmap> mBitMap = new ArrayList<>();
            mapResponses = LetoApplication.getInstance().getBitmaps();
            Collections.sort(mapResponses);
            for (int i = 0; i < mapResponses.size(); i++) {
                mBitMap.add(i, mapResponses.get(i).getBitmap());
            }
            mBitMap = WheelSurfView.rotateBitmaps(mBitMap);

            LuckyListReponses = LetoApplication.getInstance().getmLuckResponse();
            List<String> stringList = new ArrayList<>();
            Integer[] colors = new Integer[mapResponses.size()];
            for (int i = 0; i < LuckyListReponses.size(); i++) {
                stringList.add(LuckyListReponses.get(i).getName());
                if (i % 2 == 0) {
                    colors[i] = Color.parseColor("#fdcc7d");
                } else {
                    colors[i] = Color.parseColor("#fcd48c");
                }
            }
            String[] array = stringList.toArray(new String[stringList.size()]);

            WheelSurfView.Builder build = new WheelSurfView.Builder()
                    .setmDeses(array)
                    .setmColors(colors)
                    .setmMinTimes(5)
                    .setmVarTime(100)
                    .setmIcons(mBitMap)
                    .setmType(1)
                    .setmTypeNum(mapResponses.size())
                    .setmTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()))
                    .setmTextColor(Color.parseColor("#ba7414"))
                    .build();

            mLotteryDisk.setConfig(build);

            mLotteryDisk.setRotateListener(new RotateListener() {
                @Override
                public void rotateEnd(int position, String des) {
                    isRunning = false;
                    if (null != mReward) {
                        if (mReward.getStatus() == 3) {
                            new LuckyDialog().showDialog(mContext, true, "谢谢参与！", "换个姿势试试吧", mReward.getPic(),
                                    false, "再抽一次", "溜了溜了",
                                    new LuckyDialog.ConfirmDialogListener() {
                                        @Override
                                        public void ok() {
                                            if (!isRunning) {
                                                isRunning = true;
                                                initNet();
                                            }
                                        }

                                        @Override
                                        public void cancel() {

                                        }

                                        @Override
                                        public void dismiss() {

                                        }
                                    });
                        } else {
                            new LuckyDialog().showDialog(mContext, true, "恭喜您获得", mReward.getName(), mReward.getPic(),
                                    true, "查看奖品", "稍后查看",

                                    new LuckyDialog.ConfirmDialogListener() {
                                        @Override
                                        public void ok() {
                                            LuckyHistoryActivity.start(LotteryActivity.this);
                                        }

                                        @Override
                                        public void cancel() {

                                        }

                                        @Override
                                        public void dismiss() {

                                        }
                                    });
                        }
                    }
                }

                @Override
                public void rotating(ValueAnimator valueAnimator) {

                }

                @Override
                public void rotateBefore(ImageView goImg) {
                    if (!isRunning) {
                        isRunning = true;
                        initNet();
                    }

                }
            });

//            goLuck.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(Util.isFastClick()) {
//                        initNet();
//                    }
//                }
//            });
        }

        initOnClick();
        initUserLog();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, null);
    }

    private void initOnClick() {
        tv_my_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LuckyHistoryActivity.start(LotteryActivity.this);
            }
        });
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LuckyHistoryActivity.start(LotteryActivity.this);
                getShareUrl();
            }
        });
    }

    //渐变开始默认位置，Y轴，50dp
    private final int DFT_TRANSSTARTY = 0;
    //渐变结束默认位置，Y轴，300dp
    private final int DFT_TRANSENDY = 100;

    private int getTransAlpha(float scrollY) {
        float transStartY = DimensionUtil.dip2px(this, DFT_TRANSSTARTY);
        float transEndY = DimensionUtil.dip2px(this, DFT_TRANSENDY);
        if (transStartY != 0) {
            if (scrollY <= transStartY) {
                return 0;
            } else if (scrollY >= transEndY) {
                return 255;
            } else {
                return (int) ((scrollY - transStartY) / (transEndY - transStartY) * 255);
            }
        } else {
            if (scrollY >= transEndY) {
                return 255;
            }
            return (int) ((transEndY - scrollY) / transEndY * 255);
        }
    }

    /**
     * 所有用户的中奖纪录
     */
    private void initUserLog() {
        ListBaseRequestBean requestBean = new ListBaseRequestBean();
        requestBean.setPage(1);
        requestBean.setOffset(30);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<List<LuckyListReponse>>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(List<LuckyListReponse> data) {
                if (data != null) {
                    Gson gson = new Gson();
                    String str = gson.toJson(data);
                    List<LuckyListReponse> responses = gson.fromJson(str, new TypeToken<List<LuckyListReponse>>() {
                    }.getType());
                    if (responses != null && responses.size() != 0) {
                        smoothScrollLayout.setData(responses);
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
        RxVolley.post(SdkApi.getUsersLog(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    /**
     * 抽奖
     */
    private void initNet() {
        mReward = null;
        BaseRequestBean requestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<LuckyListReponse>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(LuckyListReponse data) {
                if (data != null) {
                    int pos = 0;
                    for (int i = 0; i < mapResponses.size(); i++) {
                        if (mapResponses.get(i).getId() == data.getLottery_id()) {
                            mReward = LuckyListReponses.get(i);
                            mReward.setStatus(data.getStatus());
                            pos = i;
                            break;
                        }
                    }
                    if (null != mLotteryDisk) {
                        mLotteryDisk.startRotate(pos + 1);
                    }
                } else {
                    if (LotteryActivity.this != null) {
                        T.s(LotteryActivity.this, data.getMsg());
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
                if (LotteryActivity.this != null) {
                    T.s(LotteryActivity.this, msg);
                }
                isRunning = false;
            }
        };
        RxVolley.post(SdkApi.getLottery(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onRight2Click() {

    }

    private void initActionBar() {
        actionBar = (TranslucentActionBar) findViewById(R.id.actionbar);
        //初始actionBar
        actionBar.setData("", R.mipmap.back_normal, null, 0, null, this);
        //开启渐变
        actionBar.setNeedTranslucent();
        //设置状态栏高度
        actionBar.setStatusBarHeight(getStatusBarHeight());

        sv_content = (TranslucentScrollView) findViewById(R.id.translucentScrollView);
        //设置透明度变化监听
        sv_content.setTranslucentChangedListener(this);
        //关联需要渐变的视图
        sv_content.setTransView(actionBar);
        //设置ActionBar键渐变颜色
        sv_content.setTransColor(getResources().getColor(R.color.mgc_sdk_bg_blue));

    }

    @Override
    public void onTranslucentChanged(int transAlpha) {

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LotteryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    private void showShareType(final String url) {
        new ShareTypeDialog().showDialog(LotteryActivity.this, url, new ShareTypeDialog.ConfirmDialogListener() {
            @Override
            public void setShareType(int type, String filePath) {
                showSharePlatform(type, url, filePath);
            }

            @Override
            public void cancel() {

            }
        });
    }

    private void showSharePlatform(final int type, final String url, final String filePath) {


        new SharePlatformDialog().showDialog(LotteryActivity.this, null, new SharePlatformDialog.ConfirmDialogListener() {
            @Override
            public void setPlatform(SHARE_MEDIA platform) {

                String shareImage = null;
                String shareTitle = "天天抽大奖，iPhone X等你拿";
                String shareUrl = url;
                String shareContent = "免费抽奖赢取iPhone X、话费券及热门游戏充值券！";

                if (type == 0) {
                    shareImage = filePath;
                    ShareUtil.shareImage(LotteryActivity.this, platform, new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {
//                            FloatActionController.getInstance().hide();
                        }

                        @Override
                        public void onResult(SHARE_MEDIA share_media) {
                            T.s(LotteryActivity.this, "分享成功");
                        }

                        @Override
                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            T.s(LotteryActivity.this, "分享失败：" + throwable.getMessage());
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media) {
                            T.s(LotteryActivity.this, "分享取消");
                        }
                    }, shareImage, "", shareContent);

                } else {
                    ShareUtil.shareToPlatform(mContext, platform, new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {
//                            FloatActionController.getInstance().hide();
                        }

                        @Override
                        public void onResult(SHARE_MEDIA share_media) {
                            T.s(LotteryActivity.this, "分享成功");
                        }

                        @Override
                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            T.s(LotteryActivity.this, "分享失败：" + throwable.getMessage());
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media) {
                            T.s(LotteryActivity.this, "分享取消");
                        }
                    }, shareUrl, shareTitle, shareContent, shareImage);
                }

            }

            @Override
            public void cancel() {

            }
        });
    }


    private void getShareUrl() {
        BaseRequestBean baseRequestBean = new BaseRequestBean();

        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<ShareUrlResultBean>(LotteryActivity.this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(ShareUrlResultBean data) {
                if (data != null) {
                    try {
                        //String url = data.getString("url");
                        String url = data.getUrl();
                        showShareType(url);

                    } catch (Exception e) {

                    }
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);//当前Splash显示中
        RxVolley.post(SdkApi.getWalletAddress(), httpParamsBuild.getHttpParams(), httpCallbackDecode);

    }
}
