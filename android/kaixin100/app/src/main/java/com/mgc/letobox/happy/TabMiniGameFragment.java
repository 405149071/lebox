package com.mgc.letobox.happy;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.ledong.lib.leto.db.LoginControl;
import com.ledong.lib.leto.main.WebViewActivity;
import com.ledong.lib.minigame.IGameSwitchListener;
import com.ledong.lib.minigame.MiniGameManager;
import com.ledong.lib.minigame.MinigameAdapter;
import com.ledong.lib.minigame.utils.GameUtil;
import com.leto.game.base.bean.MiniGameRequestBean;
import com.leto.game.base.bean.MinigameResultBean;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.leto.game.base.http.SdkApi;
import com.leto.game.base.util.BaseAppUtil;
import com.liang530.log.T;
import com.mgc.letobox.happy.base.AutoLazyFragment;
import com.mgc.letobox.happy.game.HistoryGameActivity;

import java.util.List;

import butterknife.BindView;

/**
 * Created by DELL on 2018/8/4.
 */

public class TabMiniGameFragment extends AutoLazyFragment implements IGameSwitchListener {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.list_view)
    ListView listView;

    @BindView(R.id.tv_clear_cache)
    TextView tv_clear_cache;

    @BindView(R.id.ll_clear_cache)
    LinearLayout ll_clear_cache;


    public static TabMiniGameFragment newInstance() {
        TabMiniGameFragment fragment = new TabMiniGameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_minigame);

        iv_back.setVisibility(View.INVISIBLE);

        tv_clear_cache.setText("历史");

        ll_clear_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryGameActivity.start(getContext());
            }
        });

        loadGame();
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
//        getMinigames();
    }

    private void loadGame() {

        MinigameResultBean gameList = GameUtil.loadGameList(getContext());

        if (gameList != null) {
            updateListView(gameList);
        }
        getMinigames();
    }


    private void getMinigames() {
        MiniGameRequestBean baseBean = new MiniGameRequestBean();
        baseBean.setType(1);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(new Gson().toJson(baseBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<MinigameResultBean>(getContext(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(MinigameResultBean data) {
                if (null != data) {

                    List<MinigameResultBean.GamesModel> gameCategoryList = data.getGames();
                    if (null != gameCategoryList && gameCategoryList.size() > 0) {
                        for (int i = 0; i < gameCategoryList.size(); i++) {
                            List<MinigameResultBean.GameListModel> gameList = gameCategoryList.get(i).getGameList();
                            for (int j = 0; j < gameList.size(); j++) {
                                int number = BaseAppUtil.getNum(80, 100);
                                if (i == 1) {
                                    number = BaseAppUtil.getNum(150, 200);
                                } else if (i == 2) {
                                    number = BaseAppUtil.getNum(60, 80);
                                } else if (i == 3) {
                                    number = BaseAppUtil.getNum(100, 150);
                                }
                                gameList.get(j).setPlay_num("" + number);
                            }
                        }
                    }
                    boolean isLogin = LoginControl.isLogin(getApplicationContext());
                    if (isLogin) {
                        int pos = -1;
                        List<MinigameResultBean.BannerModel> bannerList = data.getBanners();
                        for (int i = 0; i < bannerList.size(); i++) {
                            if (5 == bannerList.get(i).getBanner_type()) {
                                pos = i;
                                break;
                            }
                        }
                        if (pos != -1) {
                            bannerList.remove(pos);
                        }
                    }

                    GameUtil.saveJson(getActivity(), new Gson().toJson(data), GameUtil.MORE_GAME_LIST);
                    updateListView(data);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                if (null != getActivity() && !getActivity().isDestroyed()) {
                    T.s(getActivity(), msg);
                }
            }

            @Override
            public void onFinish() {
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        httpCallbackDecode.setLoadMsg("获取数据...");
        RxVolley.post(SdkApi.getMinigameList(), httpParamsBuild.getHttpParams(), httpCallbackDecode);

    }

    private void updateListView(MinigameResultBean data) {
        if (null == data || null == data.getGames()) {
            return;
        }
        MinigameAdapter adapter = new MinigameAdapter(getActivity(), data, this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onJump(String appId, String appPath) {
        //MiniGameManager.getInstance().startGame(srcAppId, srcAppPath, appId, appPath);

        if (null == MiniGameManager.getInstance()) {
            MiniGameManager.init(this.getApplicationContext());
        }
        MiniGameManager.getInstance().startGame(getContext(), LetoApplication.getInstance().getAppId(), "", appId, appPath);

    }

    @Override
    public void onJump(String appId, String appPath, int isBigGame, String url, String packageName, String gameName, int isCps, String splashPic) {
        //MiniGameManager.getInstance().startGame(srcAppId, srcAppPath, appId, appPath);

        if (null == MiniGameManager.getInstance()) {
            MiniGameManager.init(this.getApplicationContext());
        }
        MiniGameManager.getInstance().startGame(getContext(), LetoApplication.getInstance().getAppId(), "", appId, appPath,
                isBigGame, url, packageName, gameName, isCps, splashPic);

    }

    @Override
    public void onJump(String appId, String appPath, int isBigGame, String url, String packageName, String gameName, int isCps, String splashPic, int is_kp_ad, int is_more, String icon, String share_url, String share_msg, String package_url, int game_type) {

        if (null == MiniGameManager.getInstance()) {
            MiniGameManager.init(this.getApplicationContext());
        }
        if (game_type == 10) {
            WebViewActivity.start(getContext(), "小说", package_url, 4, 1, appId, LetoApplication.getInstance().getAppId());
        } else {
            MiniGameManager.getInstance().startGame(getContext(), LetoApplication.getInstance().getAppId(), "", appId, appPath,
                    isBigGame, url, packageName, gameName, isCps, splashPic, is_kp_ad, is_more, icon, share_url, share_msg);

        }

    }
}
