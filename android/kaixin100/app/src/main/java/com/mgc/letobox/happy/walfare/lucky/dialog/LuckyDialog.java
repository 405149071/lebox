package com.mgc.letobox.happy.walfare.lucky.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mgc.letobox.happy.R;


public class LuckyDialog {
    private Dialog dialog;
    private ConfirmDialogListener mlistener;

    TextView tvTitle;
    TextView tvContent;

    TextView btok;
    TextView btcancel;

    public void showDialog(Context context, boolean showCancel, String title, String content, String imageUrl, boolean hasReward,
                           String okText, String cancelText,
                           ConfirmDialogListener listener) {
        if(context==null) return;
        if(context instanceof Activity){
            if(((Activity)context).isDestroyed())
                return;
        }
        dismiss();

        this.mlistener = listener;
        View dialogview = LayoutInflater.from(context).inflate(R.layout.lucky_dialog_common, null);
        dialog = new Dialog(context, R.style.dialog_bg_style);
        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(true);
        //dialog默认是环绕内容的
        //通过window来设置位置、高宽
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        windowparams.height = 880;
        windowparams.width = 740;
//        设置背景透明,但是那个标题头还是在的，只是看不见了
        //注意：不设置背景，就不能全屏
//        window.setBackgroundDrawableResource(android.R.color.transparent);

        btok = (TextView) dialogview.findViewById(R.id.confirm_tv);
        btcancel = (TextView) dialogview.findViewById(R.id.cancel_tv);
        tvTitle = (TextView) dialogview.findViewById(R.id.title);
        tvContent = (TextView) dialogview.findViewById(R.id.tv_content);
        TextView tv_hint = (TextView) dialogview.findViewById(R.id.tv_hint);
        ImageView ivReward = (ImageView) dialogview.findViewById(R.id.iv_reward);

        if (null!=context && !((Activity)context).isDestroyed()) {
            Glide.with(context).load(imageUrl).into(ivReward);
        }
        if (hasReward) {
            tv_hint.setVisibility(View.VISIBLE);
            ivReward.setBackgroundResource(R.mipmap.lucky_dialog_reward_bg);
        } else {
            tv_hint.setVisibility(View.GONE);
        }

        if (title != null) {
            tvTitle.setText(title.trim());
        }
        if (content != null) {
            tvContent.setText(content.trim());
        }
        if (showCancel) {
            btcancel.setVisibility(View.VISIBLE);
        } else {
            btcancel.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(cancelText)){
            btcancel.setText(cancelText);
        }
        if(!TextUtils.isEmpty(okText)){
            btok.setText(okText);
        }

        btok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistener != null) {
                    mlistener.ok();
                }
                dismiss();
            }
        });
        btcancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mlistener != null) {
                                                mlistener.cancel();
                                            }
                                            dismiss();
                                        }
                                    }
        );

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mlistener != null) {
                    mlistener.dismiss();
                }
            }
        });

        dialog.show();
    }

    public void showDialog(Context context, boolean showCancel, String title, String content, String imageUrl, boolean hasReward, String wxNo,
                           String okText, String cancelText,
                           ConfirmDialogListener listener) {
        if(context==null) return;
        if(context instanceof Activity){
            if(((Activity)context).isDestroyed())
                return;
        }
        dismiss();
        this.mlistener = listener;
        View dialogview = LayoutInflater.from(context).inflate(R.layout.lucky_dialog_common, null);
        dialog = new Dialog(context, R.style.dialog_bg_style);
        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(true);
        //dialog默认是环绕内容的
        //通过window来设置位置、高宽
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        windowparams.height = 880;
        windowparams.width = 740;
//        设置背景透明,但是那个标题头还是在的，只是看不见了
        //注意：不设置背景，就不能全屏
//        window.setBackgroundDrawableResource(android.R.color.transparent);

        btok = (TextView) dialogview.findViewById(R.id.confirm_tv);
        btcancel = (TextView) dialogview.findViewById(R.id.cancel_tv);
        tvTitle = (TextView) dialogview.findViewById(R.id.title);
        tvContent = (TextView) dialogview.findViewById(R.id.tv_content);
        TextView tv_hint = (TextView) dialogview.findViewById(R.id.tv_hint);
        ImageView ivReward = (ImageView) dialogview.findViewById(R.id.iv_reward);

        tv_hint.setText("请添加客服微信领取奖品");

        TextView tv_wxno = (TextView) dialogview.findViewById(R.id.tv_wxno);
        tv_wxno.setText(wxNo);
        tv_wxno.setVisibility(View.VISIBLE);
        Glide.with(context).load(imageUrl).into(ivReward);

        if (hasReward) {
            tv_hint.setVisibility(View.VISIBLE);
            ivReward.setBackgroundResource(R.mipmap.lucky_dialog_reward_bg);
        } else {
            tv_hint.setVisibility(View.GONE);
        }

        if (title != null) {
            tvTitle.setText(title.trim());
        }
        if (content != null) {
            tvContent.setText(content.trim());
        }
        if (showCancel) {
            btcancel.setVisibility(View.VISIBLE);
        } else {
            btcancel.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(cancelText)){
            btcancel.setText(cancelText);
        }
        if(!TextUtils.isEmpty(okText)){
            btok.setText(okText);
        }

        btok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistener != null) {
                    mlistener.ok();
                }
                dismiss();
            }
        });
        btcancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mlistener != null) {
                                                mlistener.cancel();
                                            }
                                            dismiss();
                                        }
                                    }
        );

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mlistener != null) {
                    mlistener.dismiss();
                }
            }
        });

        dialog.show();
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            mlistener = null;
        }
    }

    public interface ConfirmDialogListener {
        void ok();

        void cancel();

        void dismiss();
    }
}
