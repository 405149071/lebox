package com.mgc.letobox.happy;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.kymjs.rxvolley.toolbox.HTTPSTrustManager;
import com.ledong.lib.leto.LetoManager;
import com.leto.game.base.util.BaseAppUtil;
import com.mgc.letobox.happy.R;
import com.liang530.log.SP;
import com.mgc.letobox.happy.domain.BitMapResponse;
import com.mgc.letobox.happy.domain.LuckyListReponse;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.List;


public class LetoApplication extends HsApplication {

    public String getAppId() {
        return mAppId;
    }

    public void setAppId(String mAppId) {
        this.mAppId = mAppId;
    }

    public String mAppId = "";

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new MaterialHeader(context).setColorSchemeColors(context.getResources().getColor(R.color.refresh_blue), context.getResources().getColor(R.color.refresh_blue), context.getResources().getColor(R.color.refresh_blue), context.getResources().getColor(R.color.refresh_blue), context.getResources().getColor(R.color.refresh_blue));
            }
        });

        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                ClassicsFooter classicsFooter = new ClassicsFooter(context);
                classicsFooter.REFRESH_FOOTER_NOTHING = "";
                return classicsFooter;
            }
        });
    }
    public static LetoApplication getInstance() {
        return sInstance;
    }

    private static LetoApplication sInstance;

    public static int flag = -1;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        MultiDex.install(this);

        HTTPSTrustManager.allowAllSSL();//开启https支持
        SP.init(this);
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        // init
//        LetoManager.init(getApplicationContext(),"test");
        LetoManager.init(getApplicationContext());

        //与Fileprovider作用相同
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            builder.detectFileUriExposure();
        }

        mAppId = "364063";
        Log.i("LetoApplication", mAppId);
    }

    public List<BitMapResponse> bitmaps;

    public List<BitMapResponse> getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(List<BitMapResponse> bitmaps) {
        this.bitmaps = bitmaps;
    }

    public List<LuckyListReponse> mLuckResponse;
    public List<LuckyListReponse> drawttResponse;

    public List<LuckyListReponse> getmLuckResponse() {
        return mLuckResponse;
    }

    public void setmLuckResponse(List<LuckyListReponse> mLuckResponse) {
        this.mLuckResponse = mLuckResponse;
    }

    public List<LuckyListReponse> getDrawttResponse() {
        return drawttResponse;
    }

    public void setDrawttResponse(List<LuckyListReponse> drawttResponse) {
        this.drawttResponse = drawttResponse;
    }

    public List<BitMapResponse> drawttBitmaps;

    public List<BitMapResponse> getDrawttBitmaps() {
        return drawttBitmaps;
    }

    public void setDrawttBitmaps(List<BitMapResponse> drawttBitmaps) {
        this.drawttBitmaps = drawttBitmaps;
    }


}
