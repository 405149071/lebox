package com.mgc.letobox.happy.base;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.mgc.letobox.happy.R;
import com.liang530.log.L;
import com.liang530.log.T;
import com.mgc.letobox.happy.LetoApplication;
import com.mgc.letobox.happy.SplashActivity;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by ytian on 01/02/2018.
 */

@SuppressLint("Registered")
public class BaseActivity extends com.liang530.application.BaseActivity {
    private ProgressDialog mProgressDialog;

    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.i(TAG,"onCreate");
        mContext = this;
        if (LetoApplication.flag == -1) {//flag为-1说明程序被杀掉
            protectApp();
        }else{
            Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                @Override
                public boolean queueIdle() {
                    Log.i("IdleHandler","queueIdle");
                    onInit();
                    return false; //false 表示只监听一次IDLE事件,之后就不会再执行这个函数了.
                }
            });
        }
    }

    //子类重写此函数即可,而不需要在onCreate()中去初始化.
    protected void onInit() {
        Log.e("BaseActivity", "onInit");
    }
    protected void protectApp() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra("action", "force_kill");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//清空栈里MainActivity之上的所有activty
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        L.i(TAG,"onResume");
        MobclickAgent.onResume(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        L.i(TAG,"onPause");
        MobclickAgent.onPause(this);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        L.i(TAG,"onRestart");
    }
    @Override
    public void onStart() {
        super.onStart();
        L.i(TAG,"onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        L.i(TAG,"onStop");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i(TAG,"onDestroy");
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }
    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    protected void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        mProgressDialog.show();
    }

    protected void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void showToast(int resId) {
        T.s(this, resId);
    }

    protected void showToast(String msg) {
        T.s(this, msg);
    }


    private View titleView;
    /**
     * 改变标题栏显示状态
     * @param show
     */
    public void changeTitleStatus(boolean show){
        if(titleView==null){
            Log.e("BaseActivity","没有设置titleView");
            return;
        }
        if(show){
            titleView.setVisibility(View.VISIBLE);
        }else{
            titleView.setVisibility(View.GONE);
        }
    }
    /**
     * 设置标题栏view
     * @param titleView
     */
    public void setTitleView(View titleView){
        this.titleView=titleView;
    }


}
