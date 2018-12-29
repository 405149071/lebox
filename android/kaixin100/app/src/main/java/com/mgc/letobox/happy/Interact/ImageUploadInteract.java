package com.mgc.letobox.happy.Interact;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import com.leto.game.base.http.HttpParamsBuild;
import com.mgc.letobox.happy.config.SdkApi;
import com.mgc.letobox.happy.domain.PostImage;
import com.mgc.letobox.happy.util.RxVolleyUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Create by zhaozhihui on 2018/9/10
 **/
public class ImageUploadInteract {

    public interface CallBack{
        public void callBack(List<String> data);
    }

    public static void uploadImage(Context context, String path, int type, final CallBack callBack) {
        HttpParams params = HttpParamsBuild.getCustomHttpParams(context);

        params.put("type", type);

        params.put("portrait[]", new File(path));

        new RxVolleyUtil().post(SdkApi.postImage(), params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                Log.d("MgcUpload","receive result....");
                Gson gson = new Gson();
                PostImage postImage = gson.fromJson(t, PostImage.class);
                if (Integer.valueOf(postImage.getCode()) == 200) {
                    List<String > imageIds = new ArrayList<>();
                    String[] data = postImage.getData();
                    if (data != null && data.length > 0) {
                        imageIds.addAll(Arrays.asList(data));
                    }
                    if(callBack!=null){
                        callBack.callBack(imageIds);
                    }
                }
                Log.d("MgcUpload","finish.");
            }

            @Override
            public void onFailure(VolleyError error) {
                super.onFailure(error);
            }
        });
        Log.d("MgcUpload","begining....");
    }

    public static void uploadImage(Context context, List<String> paths, int type, final CallBack callBack) {
        HttpParams params = HttpParamsBuild.getCustomHttpParams(context);

        params.put("type", type);

        for(String path :paths) {
            params.put("portrait[]", new File(path));
        }

        new RxVolleyUtil().post(SdkApi.postImage(), params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                Log.d("MgcUpload","receive result....");
                Gson gson = new Gson();
                PostImage postImage = gson.fromJson(t, PostImage.class);
                if (Integer.valueOf(postImage.getCode()) == 200) {
                    List<String > imageIds = new ArrayList<>();
                    String[] data = postImage.getData();
                    if (data != null && data.length > 0) {
                        imageIds.addAll(Arrays.asList(data));
                    }
                    if(callBack!=null){
                        callBack.callBack(imageIds);
                    }
                }
                Log.d("MgcUpload","finish.");
            }

            @Override
            public void onFailure(VolleyError error) {
                super.onFailure(error);
            }
        });
        Log.d("MgcUpload","begining....");
    }
}
