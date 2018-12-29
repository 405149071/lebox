package com.mgc.letobox.happy.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mgc.letobox.happy.R;
import com.liang530.utils.BaseAppUtil;


public class VersionDialog{
    private Dialog dialog;
    private ConfirmDialogListener mlistener;

    TextView tvTitle;
    TextView tvContent;

    TextView btok;
    TextView btcancel;


    public void showDialog(Context context, boolean showCancel, String title, String content,String cancelText, String confirmText, ConfirmDialogListener listener, Boolean isCanceledOnTouchOutSide){

        dismiss();
        this.mlistener=listener;
        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_common_version, null);
        dialog = new Dialog(context,R.style.dialog_bg_style);
        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutSide);
        //dialog默认是环绕内容的
        //通过window来设置位置、高宽
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
////        window.setGravity(Gravity.BOTTOM);
//        windowparams.height = ;
        windowparams.width = BaseAppUtil.getDeviceWidth(context)-2* BaseAppUtil.dip2px(context, 16);
//        设置背景透明,但是那个标题头还是在的，只是看不见了
        //注意：不设置背景，就不能全屏
//        window.setBackgroundDrawableResource(android.R.color.transparent);

        btok = (TextView) dialogview.findViewById(R.id.confirm_tv);
        btcancel = (TextView) dialogview.findViewById(R.id.cancel_tv);
        tvTitle = (TextView) dialogview.findViewById(R.id.title);
        tvContent = (TextView) dialogview.findViewById(R.id.tv_content);
        if(title!=null){
            tvTitle.setText(title);
        }
        if(content!=null){
            tvContent.setText(content);
        }
        if(showCancel){
            btcancel.setVisibility(View.VISIBLE);
        }else{
            btcancel.setVisibility(View.GONE);
        }
        if(TextUtils.isEmpty(cancelText)){
            btcancel.setText(cancelText);
        }
        if(TextUtils.isEmpty(confirmText)){
            btok.setText(confirmText);
        }

        btok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlistener!=null){
                    mlistener.ok();
                }
                dismiss();
            }
        });
        btcancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(mlistener!=null){
                                                mlistener.cancel();
                                            }
                                            dismiss();
                                        }
                                    }
        );

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){

            @Override
            public void onDismiss(DialogInterface dialog) {
                if(mlistener!=null){
                    mlistener.dismiss();
                }
            }
        });

        dialog.show();
    }



    public void dismiss(){
        if(dialog !=null){
            dialog.dismiss();
            mlistener=null;
        }
    }
    public interface ConfirmDialogListener{
        void ok();
        void cancel();
        void dismiss();
    }
}
