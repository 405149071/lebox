package com.mgc.letobox.happy;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ledong.lib.leto.api.payment.IMgcListener;
import com.ledong.lib.leto.api.payment.MgcJsForWeb;
import com.ledong.lib.leto.main.WebViewActivity;
import com.ledong.lib.leto.utils.WebLoadByAssertUtil;
import com.leto.game.base.bean.WebLoadAssert;
import com.leto.game.base.util.DialogUtil;
import com.leto.game.base.util.NetUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.base.AutoLazyFragment;
import com.mgc.letobox.happy.config.SdkApi;;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;

public class TabReadFragment extends AutoLazyFragment implements IMgcListener {


    @BindView(R.id.iv_return)
    ImageView iv_return;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_right)
    TextView tv_right;

    @BindView(R.id.mgc_sdk_wv_content)
    WebView wv;


    private String mUrl= SdkApi.novel_url;

    private MgcJsForWeb commonJsForWeb;

    List<WebLoadAssert> webLoadAssertList = WebLoadByAssertUtil.getWebLoadAssertList();


    public static TabReadFragment newInstance() {
        TabReadFragment fragment = new TabReadFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_read);

        tv_title.setText("阅读");
        iv_return.setVisibility(View.INVISIBLE);

        webviewInit();

        wv.loadUrl(mUrl);


        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = wv.getUrl();
                Log.e(TAG, "当前页面的url=" + url);
                if(wv.canGoBack()){
                    wv.goBack();
                }else{
                    iv_return.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, TabReadFragment.class);
        context.startActivity(intent);
    }

    private void webviewInit() {
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setDefaultTextEncodingName("UTF-8");
        wv.getSettings().setAllowFileAccess(true);
        wv.getSettings().setAllowFileAccessFromFileURLs(true);
        wv.getSettings().setAllowUniversalAccessFromFileURLs(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setDisplayZoomControls(false);
        wv.getSettings().setSupportMultipleWindows(false);
        wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setDatabaseEnabled(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setAppCacheMaxSize(Long.MAX_VALUE);
        wv.getSettings().setGeolocationEnabled(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setMediaPlaybackRequiresUserGesture(false);


        //浮点里面的是没有回调的
        commonJsForWeb = new MgcJsForWeb(getActivity(), "", this);
        commonJsForWeb.setAppId(LetoApplication.getInstance().getAppId());
        commonJsForWeb.setFromAppId(LetoApplication.getInstance().getAppId());
        wv.addJavascriptInterface(commonJsForWeb, "mgc");
//		wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!DialogUtil.isShowing()) {
                    DialogUtil.showDialog(getContext(), "正在加载...");
                }
                Log.e("Webview onPageStarted", "url=" + url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "url=" + url);
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp")) {
                    return false;
                } else {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        view.getContext().startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(view.getContext(), "手机还没有安装支持打开此网页的应用！", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webviewCompat(wv);
                try {
                    DialogUtil.dismissDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
                resend.sendToTarget();
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                WebResourceResponse response;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

                    for (WebLoadAssert webLoadAssert : webLoadAssertList) {
                        if (url.contains(webLoadAssert.getName())) {
                            try {
                                response = new WebResourceResponse(webLoadAssert.getMimeType(), "UTF-8", getActivity().getAssets().open(webLoadAssert.getName()));
                                Log.e("hongliang", "加载了：" + webLoadAssert.getName());
                                return response;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if(wv.canGoBack()){
                            iv_return.setVisibility(View.VISIBLE);
                        }else{
                            iv_return.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                return super.shouldInterceptRequest(view, url);
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                WebResourceResponse response;

                for (WebLoadAssert webLoadAssert : webLoadAssertList) {
                    if (request.getUrl().getPath().contains(webLoadAssert.getName())) {
                        try {
                            response = new WebResourceResponse(webLoadAssert.getMimeType(), "UTF-8", getActivity().getAssets().open(webLoadAssert.getName()));
                            Log.e("hongliang", "加载了：" + webLoadAssert.getName());
                            return response;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return super.shouldInterceptRequest(view, request);
            }
        });
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    tv_title.setText(title);
                } else {
                    tv_title.setText("阅读");

                }
            }
        });
        webviewCompat(wv);
    }

    /**
     * 一些版本特性操作，需要适配、
     *
     * @param mWebView webview
     * @date 6/3
     * @reason 在微蓝项目的时候遇到了 返回键 之后 wv显示错误信息
     */
    private void webviewCompat(WebView mWebView) {
        if (NetUtil.isNetWorkConneted(mWebView.getContext())) {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            mWebView.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
    }

    @Override
    public void paySuccess(String s, float v) {

    }

    @Override
    public void payFail(int i, float v, String s) {

    }

    @Override
    public void downloadApk(String s) {

    }

    @Override
    public void checkUser() {

    }

    @Override
    public void syncUserInfo() {

    }

    @Override
    public void verificationUser() {

    }
}
