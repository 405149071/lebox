package com.mgc.letobox.happy.walfare.drawtt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.kymjs.rxvolley.RxVolley;
import com.ledong.lib.leto.db.LoginControl;
import com.leto.game.base.bean.BaseRequestBean;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.mgc.letobox.happy.R;
import com.liang530.log.T;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;
import com.mgc.letobox.happy.LetoApplication;
import com.mgc.letobox.happy.base.BaseActivity;
import com.mgc.letobox.happy.config.SdkApi;
import com.mgc.letobox.happy.domain.BitMapResponse;
import com.mgc.letobox.happy.domain.DrawttRequestBean;
import com.mgc.letobox.happy.domain.HomeMgcPropertyResultBean;
import com.mgc.letobox.happy.domain.LuckyListReponse;
import com.mgc.letobox.happy.domain.ShareUrlResultBean;
import com.mgc.letobox.happy.listener.ActionBarClickListener;
import com.mgc.letobox.happy.ui.TranslucentActionBar;
import com.mgc.letobox.happy.ui.TranslucentScrollView;
import com.mgc.letobox.happy.ui.dialog.SharePlatformDialog;
import com.mgc.letobox.happy.ui.dialog.ShareTypeDialog;
import com.mgc.letobox.happy.util.GsonUtil;
import com.mgc.letobox.happy.util.ShareUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawttActivity extends BaseActivity implements ActionBarClickListener, TranslucentScrollView.TranslucentChangedListener {

    @BindView(R.id.actionbar) TranslucentActionBar actionBar;

    TranslucentScrollView sv_content;

    @BindView(R.id.drawtt_light)
    ImageView lightImageView;
    @BindView(R.id.drawtt_middle)
    RelativeLayout middleLayout;
    @BindView(R.id.tt_property)
    TextView propertyTextView;
    @BindView(R.id.iv_avatar)
    RoundedImageView avatarImageView;
    @BindView(R.id.grid_view)
    GridView gridView;
    @BindView(R.id.myprize_btn)
    Button myprizeBtn;
    @BindView(R.id.btn_share) Button tv_share;

    Timer timer = new Timer();
    private int index = 0;
    private DrawttAdapter ttAdapter;
    private boolean isDraw = false;
    Timer drawTimer = new Timer();
    TimerTask drawTask = null;
    private int drawIndex = 0;
    private int circle = 0;
    private int stopNum = 0;
    LuckyListReponse mReward;
    private List<BitMapResponse> mapResponses = new ArrayList<>();
    private List<LuckyListReponse> luckyListReponses = new ArrayList<>();
    private List<LuckyListReponse> recordReponses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawtt);
//        StatusBarUtil.setTransparentForImageView(this,null);
        ButterKnife.bind(this);
        initActionBar();
        initLight();
        initAvatar();
        loadMgcProperty();
        initMapResponse();
        initGridView();
        initMyprizeBtn();
        initShare();
    }

    @Override
    public void onDestroy() {
        if (null != timer) {
            timer.cancel();
        }
        if (null != drawTimer) {
            drawTimer.cancel();
        }
        super.onDestroy();
    }

    private void initShare() {
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LuckyHistoryActivity.start(LotteryActivity.this);
                getShareUrl();
            }
        });
    }

    private void initMyprizeBtn() {
        myprizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestRecordListData();
            }
        });
    }

    private void initMapResponse() {
        if (LetoApplication.getInstance().getDrawttBitmaps() != null && LetoApplication.getInstance().getDrawttResponse() != null) {
            List<Bitmap> mBitMap = new ArrayList<>();
            mapResponses = LetoApplication.getInstance().getDrawttBitmaps();
            Collections.sort(mapResponses);
            luckyListReponses = LetoApplication.getInstance().getDrawttResponse();
        }
    }

    private void initGridView() {
        ttAdapter = new DrawttAdapter(this, luckyListReponses);
        gridView.setAdapter(ttAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (4 == position && !isDraw) {
                    // 检查余额
                    isDraw = true;
                    requestDraw();
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int curr = msg.what;
            if (0 == curr % 2) {
                lightImageView.setImageDrawable(getResources().getDrawable(R.mipmap.drawtt_light2));
            } else {
                lightImageView.setImageDrawable(getResources().getDrawable(R.mipmap.drawtt_light1));
            }
            middleLayout.invalidate();
            super.handleMessage(msg);
        }
    };

    private Handler drawHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int curIndex = msg.what % 8;
            int position = 0;
            if (curIndex <= 2 || 6 == curIndex) {
                position = curIndex;
            } else if (3 == curIndex) {
                position = 5;
            } else if (4 == curIndex) {
                position = 8;
            } else if (5 == curIndex) {
                position = 7;
            } else if (7 == curIndex) {
                position = 3;
                circle++;
            }
            if (3 == circle && curIndex == stopNum) {
                DrawttActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopDraw();
                    }
                });
            }
            ttAdapter.setSeclection(position);
            DrawttActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ttAdapter.notifyDataSetChanged();
                }
            });
            super.handleMessage(msg);
        }
    };

    private void beginDraw() {
        if (null == drawTimer) {
            drawTimer = new Timer();
        }
        if (null == drawTask) {
            drawTask = new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = drawIndex;
                    drawIndex++;
                    drawHandler.handleMessage(message);
                }
            };
        }
        if (null != drawTimer && null != drawTask) {
            drawTimer.schedule(drawTask, 0, 100);
        }
    }

    private void initLight() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                index++;
                Message message = new Message();
                message.what = index;
                handler.sendMessage(message);

            }
        }, 0, 1000);
    }

    /**
     * 抽奖
     */
    private void requestDraw() {
        mReward = null;
        DrawttRequestBean requestBean = new DrawttRequestBean();
        requestBean.setType(2);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<LuckyListReponse>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(LuckyListReponse data) {
                if (data != null) {
                    for (int i = 0; i < luckyListReponses.size(); i++) {
                        if (luckyListReponses.get(i).getId() == data.getLottery_id()) {
                            mReward = luckyListReponses.get(i);
                            mReward.setStatus(data.getStatus());
                            stopNum = i;
                            break;
                        }
                    }
                    if (null != mReward) {
                        beginDraw();
                    }
                } else {
                    if (DrawttActivity.this != null) {
                        T.s(DrawttActivity.this, data.getMsg());
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
                if (DrawttActivity.this != null) {
                    T.s(DrawttActivity.this, msg);
                }
            }
        };
        RxVolley.post(SdkApi.getLottery(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    /**
     * 我的中奖纪录
     */
    private void requestRecordListData() {
        DrawttRequestBean requestBean = new DrawttRequestBean();
        requestBean.setPage(0);
        requestBean.setOffset(100);
        requestBean.setType(2);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<List<LuckyListReponse>>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(List<LuckyListReponse> data) {
                if (data != null) {
                    Gson gson = new Gson();
                    String str = gson.toJson(data);
                    List<LuckyListReponse> responses = gson.fromJson(str, new TypeToken<List<LuckyListReponse>>() {
                    }.getType());
                    recordReponses = responses;
                    new DrawttDialog().showRecordDialog(mContext, recordReponses);
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
        RxVolley.post(SdkApi.getMyLog(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void stopDraw() {
        isDraw = false;
        drawIndex = 0;
        circle = 0;
        stopNum = 0;
        if (null != drawTimer) {
            drawTimer.cancel();
            drawTimer = null;
            drawTask = null;
        }
        showDraw();
    }

    private void showDraw() {
        if (mReward.getStatus() == 3) {
            new DrawttDialog().showThanksDialog(mContext, new DrawttDialog.ConfirmDialogListener() {
                    @Override
                    public void ok() {
                        if (!isDraw) {
                            isDraw = true;
                            requestDraw();
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
            new DrawttDialog().showCandyDialog(mContext, mReward.getName(), mReward.getPic(), new DrawttDialog.ConfirmDialogListener() {
                @Override
                public void ok() {
                    requestRecordListData();
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

    public static void start(Context context) {
        Intent intent = new Intent(context, DrawttActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
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
    protected void setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, null);
    }

    @Override
    public void onTranslucentChanged(int transAlpha) {

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

    private void initAvatar() {
        if (!LoginControl.isLogin()) {
            return ;
        }
        String avatarUrl = LoginControl.getPortrait();
        Glide.with(this).load(avatarUrl)
                .placeholder(R.mipmap.default_avatar)
                .error(R.mipmap.default_avatar)
                .into(avatarImageView);
        propertyTextView.setText(String.valueOf(LoginControl.getNickname()));
    }

    private void loadMgcProperty() {
        if (!LoginControl.isLogin()) {
            return;
        }
        BaseRequestBean requestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<HomeMgcPropertyResultBean>(this, httpParamsBuild.getAuthkey()) {
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
        propertyTextView.setText(String.valueOf(LoginControl.getNickname()) + "       TT糖果：" + String.valueOf(data.getTtProperty()));
    }

    private void getShareUrl() {
        BaseRequestBean baseRequestBean = new BaseRequestBean();

        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<ShareUrlResultBean>(DrawttActivity.this, httpParamsBuild.getAuthkey()) {
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

    private void showShareType(final String url) {
        new ShareTypeDialog().showDialog(DrawttActivity.this, url, new ShareTypeDialog.ConfirmDialogListener() {
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


        new SharePlatformDialog().showDialog(DrawttActivity.this, null, new SharePlatformDialog.ConfirmDialogListener() {
            @Override
            public void setPlatform(SHARE_MEDIA platform) {

                String shareImage = null;
                String shareTitle = "天天抽大奖，iPhone X等你拿";
                String shareUrl = url;
                String shareContent = "免费抽奖赢取iPhone X、话费券及热门游戏充值券！";

                if (type == 0) {
                    shareImage = filePath;
                    ShareUtil.shareImage(DrawttActivity.this, platform, new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {
//                            FloatActionController.getInstance().hide();
                        }

                        @Override
                        public void onResult(SHARE_MEDIA share_media) {
                            T.s(DrawttActivity.this, "分享成功");
                        }

                        @Override
                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                           T.s(DrawttActivity.this, "分享失败：" + throwable.getMessage());
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media) {
                            T.s(DrawttActivity.this, "分享取消");
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
                            T.s(DrawttActivity.this, "分享成功");
                        }

                        @Override
                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            T.s(DrawttActivity.this, "分享失败：" + throwable.getMessage());
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media) {
                            T.s(DrawttActivity.this, "分享取消");
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
