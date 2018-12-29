package com.mgc.letobox.happy.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.mgc.letobox.happy.R;
import com.liang530.utils.BaseAppUtil;


public class CommonEditDialog {
    private Dialog dialog;
    private ConfirmDialogListener mlistener;

    TextView tvTitle;
    EditText etContent;

    TextView btok;
    TextView btcancel;

    public void showDialog(Context context, boolean showCancel, String title, String content, String hint, int inputType, ConfirmDialogListener listener){
        dismiss();
        this.mlistener=listener;
        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_common_edit, null);
        dialog = new Dialog(context, R.style.dialog_bg_style);
        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(false);
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
        if(title!=null){
            tvTitle.setText(title);
        }
        etContent = (EditText) dialogview.findViewById(R.id.et_content);
        if(!TextUtils.isEmpty(content)){
            etContent.setText(content);
        }

        etContent.setInputType(inputType);

        if(showCancel){
            btcancel.setVisibility(View.VISIBLE);
        }else{
            btcancel.setVisibility(View.GONE);
        }
        btok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlistener!=null){
                    mlistener.ok(etContent.getText().toString());
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

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {

                etContent.setFocusable(true);
                etContent.setFocusableInTouchMode(true);
                etContent.requestFocus();
                etContent.setSelection(etContent.getText().length());
                InputMethodManager imm = (InputMethodManager) etContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etContent, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){

            @Override
            public void onDismiss(DialogInterface dialog) {
                if(mlistener!=null){
                    mlistener.dismiss();
                }
                dismiss();
            }
        });

        dialog.show();
    }


    public void showDialog(Context context, boolean showCancel, String title, String content, String hint, ConfirmDialogListener listener){
        dismiss();
        this.mlistener=listener;
        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_common_edit, null);
        dialog = new Dialog(context, R.style.dialog_bg_style);
        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(false);
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
        if(title!=null){
            tvTitle.setText(title);
        }
        etContent = (EditText) dialogview.findViewById(R.id.et_content);
        if(!TextUtils.isEmpty(content)){
            etContent.setText(content);
        }
        if(showCancel){
            btcancel.setVisibility(View.VISIBLE);
        }else{
            btcancel.setVisibility(View.GONE);
        }
        btok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlistener!=null){
                    mlistener.ok(etContent.getText().toString());
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

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {

                etContent.setFocusable(true);
                etContent.setFocusableInTouchMode(true);
                etContent.requestFocus();
                etContent.setSelection(etContent.getText().length());
                InputMethodManager imm = (InputMethodManager) etContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etContent, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){

            @Override
            public void onDismiss(DialogInterface dialog) {
                if(mlistener!=null){
                    mlistener.dismiss();
                }
                dismiss();
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
        void ok(String content);
        void cancel();
        void dismiss();
    }
}
