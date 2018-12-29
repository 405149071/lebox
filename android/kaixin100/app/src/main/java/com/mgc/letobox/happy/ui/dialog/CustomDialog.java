package com.mgc.letobox.happy.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mgc.letobox.happy.R;
import com.liang530.utils.BaseAppUtil;
import com.mgc.letobox.happy.util.DimensionUtil;


public class CustomDialog {
    private static final String TAG = CustomDialog.class.getSimpleName();
    private Dialog dialog;
    TextView tvamount;
    TextView tvsymbol;
    private ConfirmDialogListener mlistener;

    private Handler mHandler = new Handler();

    public void showSweetDialog(Context context, String action, String name, float amount, String symbol, ConfirmDialogListener listener) {
        //synchronized(OpenRedPacketDialog.class){
        if (context == null) {
            return;
        }
        dismiss();
        this.mlistener = listener;

        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_sweets, null);
        dialog = new Dialog(context, R.style.sweet_dialog_bg_style);
        ImageView imageView = dialogview.findViewById(R.id.imageView);

        TextView tv_title = dialogview.findViewById(R.id.tv_title);
        tv_title.setText(action);

        if (amount == 5) {
            imageView.setBackgroundResource(R.mipmap.pop_five);
        } else if (amount == 10) {
            imageView.setBackgroundResource(R.mipmap.pop_ten);
        } else if (amount == 20) {
            imageView.setBackgroundResource(R.mipmap.pop_twenty);
        }

        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(false);
        //dialog默认是环绕内容的
        //通过window来设置位置、高宽
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
        windowparams.width = BaseAppUtil.dip2px(context, 170);
        window.setGravity(Gravity.CENTER);
        window.setDimAmount(0f);

        dialog.show();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1500);

//        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
//        TextView tv_name = (TextView) dialog.findViewById(R.id.tv_name);
//        TextView tv_amount = (TextView) dialog.findViewById(R.id.tv_amount);
//        TextView tv_symbol = (TextView) dialog.findViewById(R.id.tv_symbol);

//        tv_name.setText(name);
//        tv_amount.setText("+"+amount);
//        tv_symbol.setText(symbol);
//        if(TextUtils.isEmpty(name)){
//            tv_name.setVisibility(View.GONE);
//        }
    }

    public void setListener(ConfirmDialogListener listener) {
        this.mlistener = listener;
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void show() {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog.show();
    }

    public interface ConfirmDialogListener {
        void ok();

        void cancel();
    }

    public Dialog showLoginSweetOpenDialog(Context context,float amount, String symbol, final ConfirmDialogListener listener) {
        //synchronized(OpenRedPacketDialog.class){
        dismiss();
        this.mlistener = listener;

        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_login_sweets_open, null);
        dialog = new Dialog(context, R.style.sweet_dialog_bg_style);
        ImageView imageView = dialogview.findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.ok();
            }
        });

        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(true);
        //dialog默认是环绕内容的
        //通过window来设置位置、高宽
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
        //windowparams.width = BaseAppUtil.dip2px(context, 170);
        window.setGravity(Gravity.CENTER);
        window.setDimAmount(0.2f);

        dialog.show();

        return dialog;
    }

    public Dialog showLoginSweetDialog(Context context,float amount, String symbol, final ConfirmDialogListener listener) {
        //synchronized(OpenRedPacketDialog.class){
        dismiss();
        this.mlistener = listener;

        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_login_sweets, null);
        dialog = new Dialog(context, R.style.sweet_dialog_bg_style);
        Button btnShare = dialogview.findViewById(R.id.btn_share);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.ok();
            }
        });

        TextView tv_title = (TextView) dialogview.findViewById(R.id.tv_title);
        TextView tv_amount = (TextView) dialogview.findViewById(R.id.tv_amount);
        TextView tv_amount_value = (TextView) dialogview.findViewById(R.id.tv_amount_value);

        String title = context.getResources().getString(R.string.login_sweets_open_title);

        title = String.format(title, ""+amount);
        tv_title.setText(title);
        tv_amount.setText(""+amount);
        tv_amount_value.setText(""+amount/100);

        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(true);
        //dialog默认是环绕内容的
        //通过window来设置位置、高宽
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
        //windowparams.width = BaseAppUtil.dip2px(context, 170);
        window.setGravity(Gravity.CENTER);
        window.setDimAmount(0.2f);

        dialog.show();

        return dialog;
    }

    public void showLikeSweet(Context context, float amount, View mView, int with) {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_sweets, null);
        final PopupWindow popupWindow = new PopupWindow(view, DimensionUtil.dip2px(context, 100), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.sweet_style);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);


        ImageView imageView = view.findViewById(R.id.imageView);
        if (amount == 5) {
            imageView.setImageResource(R.mipmap.pop_five);
        } else if (amount == 10) {
            imageView.setImageResource(R.mipmap.pop_ten);
        } else if (amount == 20) {
            imageView.setImageResource(R.mipmap.pop_twenty);
        }

        if (Build.VERSION.SDK_INT < 24) {
            popupWindow.showAsDropDown(mView);
        } else {
            int[] location = new int[2];
            mView.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, with, y - mView.getHeight());
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow.dismiss();
            }
        }, 2000);
    }

}
