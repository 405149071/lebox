package com.mgc.letobox.happy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.base.AutoLazyFragment;;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class TabReadFragment extends AutoLazyFragment{


    @BindView(R.id.iv_return)
    ImageView iv_return;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_right)
    TextView tv_right;


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

//        tv_right.setVisibility(View.VISIBLE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

//    @OnClick({R.id.tv_right})
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_right:
//                SettingHeadActivity.start(getActivity());
//                //showChooseImage();
//                break;
//
//    }

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

    public static void start(Context context) {
        Intent intent = new Intent(context, TabReadFragment.class);
        context.startActivity(intent);
    }

}
