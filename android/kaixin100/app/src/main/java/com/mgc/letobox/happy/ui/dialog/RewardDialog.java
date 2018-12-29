package com.mgc.letobox.happy.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.leto.game.base.util.MResource;


public class RewardDialog {
    private static final String TAG = RewardDialog.class.getSimpleName();
    private Dialog updateDialog;
    TextView tvamount;
    TextView tvsymbol;
    private ConfirmDialogListener mlistener;
    public void  showDialog(Context context, String action, String name, String amount, String symbol, ConfirmDialogListener listener){
        //synchronized(OpenRedPacketDialog.class){
            dismiss();
            this.mlistener=listener;
            View dialogview = LayoutInflater.from(context).inflate(MResource.getIdByName(context,"R.layout.dialog_sweet"), null);
            updateDialog = new Dialog(context, MResource.getIdByName(context,"R.style.mgc_sdk_dialog_bg_style"));
            //设置view
            updateDialog.setContentView(dialogview);
            updateDialog.setCanceledOnTouchOutside(true);
            //dialog默认是环绕内容的
            //通过window来设置位置、高宽
            Window window = updateDialog.getWindow();
            WindowManager.LayoutParams windowparams = window.getAttributes();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                windowparams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }else{
                windowparams.type = WindowManager.LayoutParams.TYPE_PHONE;//替换为此类型后,全屏时浮点可移动到最边缘,不在受虚拟按键影响
            }

            TextView btok = (TextView) dialogview.findViewById(MResource.getIdByName(context,"R.id.mgc_sdk_confirm_tv"));
            tvamount= (TextView) dialogview.findViewById(MResource.getIdByName(context,"R.id.mgc_sdk_text_amount"));
            tvsymbol= (TextView) dialogview.findViewById(MResource.getIdByName(context,"R.id.mgc_sdk_text_symbol"));
            if(!TextUtils.isEmpty(amount)){
                tvamount.setText(amount+"");
            }

            if(!TextUtils.isEmpty(symbol)){
                tvsymbol.setText(symbol+"");
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
        updateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(mlistener!=null){
                    mlistener.ok();
                }
                dismiss();
            }
        });
            updateDialog.show();
       // }
    }
    public void setListener(ConfirmDialogListener listener){
        this.mlistener = listener;
    }

    public void dismiss(){
        if(updateDialog !=null){
            updateDialog.dismiss();
            //mlistener=null;
        }
    }

    public void show(){
        if(updateDialog !=null && updateDialog.isShowing()){
            return;
        }
        updateDialog.show();
    }

    public void setRedPacket(String amount, String symbol){
        if(!TextUtils.isEmpty(amount)){
            tvamount.setText(amount+"");
        }

        if(!TextUtils.isEmpty(symbol)){
            tvsymbol.setText(symbol+"");
        }
    }


    public interface ConfirmDialogListener{
        void ok();
        void cancel();

    }
}
