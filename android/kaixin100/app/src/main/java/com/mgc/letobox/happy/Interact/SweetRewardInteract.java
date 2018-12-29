package com.mgc.letobox.happy.Interact;

import android.content.Context;
import android.text.TextUtils;

import com.kymjs.rxvolley.RxVolley;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.mgc.letobox.happy.config.SdkApi;
import com.mgc.letobox.happy.domain.ActionRewardRequestBean;
import com.mgc.letobox.happy.domain.RewardResultBean;
import com.mgc.letobox.happy.util.GsonUtil;

/**
 * Create by zhaozhihui on 2018/9/7
 **/
public class SweetRewardInteract {

    public interface SweetCallBack{
        public void getReward(RewardResultBean data);
    }

    public static void getStimulate(Context context, int actionValue, final SweetCallBack callBack) {
        ActionRewardRequestBean requestBean = new ActionRewardRequestBean();
        requestBean.setAction(actionValue);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<RewardResultBean>(context, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(RewardResultBean data) {
                if(callBack!=null){
                    callBack.getReward(data);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {

            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(SdkApi.getStimulate(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    public static void getStimulate(Context context, int actionValue, String gameId,final SweetCallBack callBack) {
        ActionRewardRequestBean requestBean = new ActionRewardRequestBean();
        requestBean.setAction(actionValue);
        if (!TextUtils.isEmpty(gameId)) {
            requestBean.setAction_object(Integer.parseInt(gameId));
        }
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<RewardResultBean>(context, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(RewardResultBean data) {
                if(callBack!=null){
                    callBack.getReward(data);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {

            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(SdkApi.getStimulate(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
}
