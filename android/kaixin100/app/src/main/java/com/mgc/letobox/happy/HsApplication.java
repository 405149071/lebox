package com.mgc.letobox.happy;


import android.database.sqlite.SQLiteDatabase;

import com.kymjs.rxvolley.BuildConfig;
import com.kymjs.rxvolley.http.RequestQueue;
import com.kymjs.rxvolley.toolbox.FileUtils;
import com.kymjs.rxvolley.toolbox.HTTPSTrustManager;
import com.mgc.letobox.happy.R;
import com.liang530.application.BaseApplication;
import com.liang530.log.L;
import com.liang530.log.SP;
import com.liang530.rxvolley.NetRequest;
import com.liang530.rxvolley.OkHttpIgnoreHttpsStack;
import com.liang530.system.SystemBarTintManager;
import com.liang530.ui.LoginActivity;

import java.io.File;

import okhttp3.OkHttpClient;

public class HsApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //设置同时最大下载数量
        L.init(BuildConfig.DEBUG);
        SP.init(this);
        //initNet();
        //initDeFaultBg();
        //initStatusColor();
    }
    private void initStatusColor(){
        //设置状态栏变色
        SystemBarTintManager.SystemBarTintConfig config=new SystemBarTintManager.SystemBarTintConfig();
        config.setDarkmode(false).setStatusColor(getResources().getColor(R.color.mgc_sdk_black));
        setStatusColorConfig(config);
    }
    @Override
    public Class getLoginClass() {
        return LoginActivity.class;
    }

    private void initNet() {
        NetRequest.setIsDebug(BuildConfig.DEBUG);
    }

    @Override
    protected void initHttpConfig() {
        HTTPSTrustManager.allowAllSSL();
        try {
            NetRequest.setRequestQueue(RequestQueue.newRequestQueue(getDefaultSaveRootPath("RxVolley"), new OkHttpIgnoreHttpsStack(new OkHttpClient())));
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 获取一个默认的
     * @return
     */
    public File getDefaultSaveRootPath(String name) {
        File sdFile = null;
        try {
            sdFile = FileUtils.getSaveFolder(name);
            if(!sdFile.exists()||!sdFile.isDirectory()){
                sdFile=getCacheDir();
            }
            sdFile=new File(sdFile,name);
            if(!sdFile.exists()){
                sdFile.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
            sdFile=new File(getCacheDir(),name);
            if(!sdFile.exists()){

            }
        }
        return sdFile;
    }

    private void initDeFaultBg(){
        //GlideDisplay.BG_DEF = R.mipmap.ic_launcher;
    }
//    public static UCrop.Options getDefaultOption(){
//        UCrop.Options options=new UCrop.Options();
//        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
//        options.setHideBottomControls(false);
//        options.setToolbarColor(ContextCompat.getColor(BaseApplication.getInstance(), R.color.bg_color_def));
//        options.setStatusBarColor(ContextCompat.getColor(BaseApplication.getInstance(), R.color.bg_color_def));
//        options.setActiveWidgetColor(ContextCompat.getColor(BaseApplication.getInstance(), R.color.white));
//        options.setToolbarWidgetColor(ContextCompat.getColor(BaseApplication.getInstance(), R.color.white));
//        return options;
//    }

    @Override
    public File getDatabasePath(String name) {
        return super.getDatabasePath(name);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return super.openOrCreateDatabase(name, mode, factory);
    }

}
