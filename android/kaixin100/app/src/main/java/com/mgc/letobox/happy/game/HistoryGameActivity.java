package com.mgc.letobox.happy.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.ledong.lib.leto.main.WebViewActivity;
import com.ledong.lib.minigame.BaseDownloadActivity;
import com.ledong.lib.minigame.IGameSwitchListener;
import com.ledong.lib.minigame.MiniGameManager;
import com.ledong.lib.minigame.MiniMoreAdapter;
import com.leto.game.base.bean.MiniMoreRequestBean;
import com.leto.game.base.bean.MinigameResultBean;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.leto.game.base.http.SdkApi;
import com.leto.game.base.http.SdkConstant;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.IntentConstant;
import com.leto.game.base.util.MResource;
import com.mgc.letobox.happy.LetoApplication;
import com.mgc.letobox.happy.walfare.invite.InviteActivity;
import com.umeng.analytics.MobclickAgent;

public class HistoryGameActivity extends BaseDownloadActivity implements IGameSwitchListener {
    private String gameType = "";
    private String title = "";
    private MinigameResultBean.GamesModel gamesModel;
    ListView listView;
    TextView tvTitle;
    ImageView ivBack;

    int type = 1;
    String srcAppId;
    String srcAppPath="";

    int category = 1;

    int mPage =1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getIntent()) {
            gameType = (String) getIntent().getSerializableExtra("GameType");
            title = (String) getIntent().getSerializableExtra("GameTitle");
        }
        setContentView(MResource.getIdByName(this, "R.layout.leto_minigame_more_activity"));

        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));

        String orientation = getIntent().getStringExtra(IntentConstant.ACTION_APP_ORIENTATION);
        if ("portrait".equals(orientation)) {
            type = 1;
        } else {
            type = 2;
        }
        srcAppId = LetoApplication.getInstance().getAppId();

        ivBack = findViewById(MResource.getIdByName(this, "R.id.iv_back"));
        tvTitle = findViewById(MResource.getIdByName(this, "R.id.tv_title"));
        listView = findViewById(MResource.getIdByName(this, "R.id.list_view"));
        tvTitle.setText(title);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvTitle.setText("我玩过的");
        getChargeMore();

        MobclickAgent.openActivityDurationTrack(false);
        // 设置为U-APP场景
        MobclickAgent.setScenarioType(HistoryGameActivity.this, MobclickAgent.EScenarioType.E_UM_NORMAL);

    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this); //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPause(this); //统计时长
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getChargeMore() {
        String str = SdkConstant.userToken;
        MiniMoreRequestBean bean = new MiniMoreRequestBean();
        bean.setPage(mPage);
        bean.setOffset(20);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(new Gson().toJson(bean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<MinigameResultBean.GamesModel>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(MinigameResultBean.GamesModel data) {
                if (null != data) {
                    gamesModel = data;
                    if (null != gamesModel.getGameList() && gamesModel.getGameList().size() > 0)
                        for (int j = 0; j < gamesModel.getGameList().size(); j++) {
                            int number = BaseAppUtil.getNum(60, 200);
                            gamesModel.getGameList().get(j).setPlay_num("" + number);
                        }
                    updateListView();
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("获取数据...");
        RxVolley.post(SdkApi.getMyMiniGame(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    public void updateListView() {
        MiniMoreAdapter adapter = new MiniMoreAdapter(this, gamesModel.getGameList(), this);
        listView.setAdapter(adapter);
    }


    @Override
    public void onJump(String appId, String appPath) {
        finish();
        if (null == MiniGameManager.getInstance()) {
            MiniGameManager.init(this.getApplicationContext());
        }
        MiniGameManager.getInstance().startGame(this, srcAppId, srcAppPath, appId, appPath);
    }

    @Override
    public void onJump(String appId, String appPath, int isBigName, String url, String packageName, String gameName, int isCps, String splashUrl) {
        finish();
        if (null == MiniGameManager.getInstance()) {
            MiniGameManager.init(this.getApplicationContext());
        }
        MiniGameManager.getInstance().startGame(this, srcAppId, srcAppPath, appId, appPath, isBigName, url, packageName, gameName, isCps, splashUrl);

    }

    @Override
    public void onJump(String appId, String appPath, int isBigName, String url, String packageName, String gameName, int isCps, String splashUrl, int is_kp_ad, int is_more, String icon, String share_url, String share_msg, String package_url, int game_type) {
        finish();
        if (null == MiniGameManager.getInstance()) {
            MiniGameManager.init(this.getApplicationContext());
        }
        if (game_type == 10) {
            WebViewActivity.start(this, "小说", package_url, 4, 1, appId, srcAppId);
        } else {
            MiniGameManager.getInstance().startGame(this, srcAppId, srcAppPath, appId, appPath, isBigName, url, packageName, gameName, isCps, splashUrl, is_kp_ad, is_more, icon, share_url, share_msg);

        }

    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, HistoryGameActivity.class));
    }
}
