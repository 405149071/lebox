package com.mgc.letobox.happy.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.ledong.lib.leto.config.AppConfig;
import com.leto.game.base.http.SdkConstant;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.config.SdkApi;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;

import static com.umeng.socialize.utils.ContextUtil.getContext;

/**
 * Created by zzh on 2018/3/15.
 */

public class ShareUtil {


    public static void shareToPlatform(Context context, SHARE_MEDIA platform, UMShareListener shareListener, String url, String title, String content, String image){
        UMImage thumb =null;

        if(TextUtils.isEmpty(image)){
            thumb = new UMImage(context, R.mipmap.ic_launcher);
        }else{
            thumb = new UMImage(context, image);
        }

        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(content);//描述

        new ShareAction((Activity) context)
                .setPlatform(platform)
                .withMedia(web)
                .setCallback(shareListener)//回调监听器
                .share();
    }

    public static String getShareUrl(String redPacketId){
        return SdkApi.app_redpacket_share_url_prefix +redPacketId + "&agentgame="+ SdkConstant.MGC_AGENT;
    }

    public static void shareImage(Context context, SHARE_MEDIA platform, UMShareListener shareListener, String filePath, String thumbPath , String content){

        UMImage image = new UMImage(context, BitmapFactory.decodeFile(filePath) );
        //UMImage thumb =  new UMImage(context, R.mipmap.ic_launcher);
        //image.setThumb(thumb);

        new ShareAction((Activity) context)
                .setPlatform(platform)
                .withText(content)
                .withMedia(image)
                .setCallback(shareListener)//回调监听器
                .share();
    }

    public static void gameBoxShare(Activity context, UMShareListener shareListener) {

        UMImage thumb = new UMImage(context, R.mipmap.ic_launcher);

        UMMin umMin = new UMMin("http://www.qq.com");
        //兼容低版本的网页链接
        umMin.setThumb(thumb);
        // 小程序消息封面图片
        umMin.setTitle("梦工厂游戏盒子");
        // 小程序消息title
        umMin.setDescription("MGC盒子创造了全新的去中心性化，公平、透明、自由、 高效的游戏生态系统，旨在提高游戏公平性将平台价值回归到社区、回归给玩家");
        // 小程序消息描述
        umMin.setPath("pages/login/login");
        //小程序页面路径
        umMin.setUserName("gh_e462ad12bab9");
        // 小程序原始id,在微信平台查询
        new ShareAction(context)
                .withMedia(umMin)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(shareListener).share();

    }

//    public static void openWX(){
//        String appId = "wx63b404da489795f6"; // MGC梦工厂游戏盒子-
//        IWXAPI api = WXAPIFactory.createWXAPI(getContext(), appId);
//
//        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
//        req.userName = "gh_e462ad12bab9"; // MGC梦工厂游戏盒子-
//        //req.userName = "gh_9d6ba12a6b8e"; // 梦工厂信息盒子
//        //req.path = "pages/index/index?type=3";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
//        req.path = "/pages/index/index?type_from=3&type=3&agent_id=11889";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
//        //req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
//        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
//        api.sendReq(req);
//    }

}
