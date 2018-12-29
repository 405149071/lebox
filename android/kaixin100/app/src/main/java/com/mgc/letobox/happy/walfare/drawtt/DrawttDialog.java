package com.mgc.letobox.happy.walfare.drawtt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.domain.LuckyListReponse;
import com.mgc.letobox.happy.util.DateUtil;

import java.util.ArrayList;
import java.util.List;


public class DrawttDialog {
    private Dialog dialog;
    private ConfirmDialogListener mlistener;

    TextView tvContent;

    TextView btok;
    TextView btcancel;
    ImageView rewardImageView;
    GridView recordGridView;
    DrawttRecordAdapter recordAdapter;

    public void showThanksDialog(Context context, ConfirmDialogListener listener) {
        if (null == context) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity)context).isDestroyed())
                return;
        }
        dismiss();

        this.mlistener = listener;
        View dialogview = LayoutInflater.from(context).inflate(R.layout.drawtt_dialog_thanks, null);
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

    public void showCandyDialog(Context context, String hint, String imageUrl, ConfirmDialogListener listener) {
        if (null == context) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity)context).isDestroyed())
                return;
        }
        dismiss();

        this.mlistener = listener;
        View dialogview = LayoutInflater.from(context).inflate(R.layout.drawtt_dialog_candy, null);
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
        tvContent = (TextView)dialogview.findViewById(R.id.tv_content);
        rewardImageView = (ImageView)dialogview.findViewById(R.id.iv_reward);
        tvContent.setText(hint);
        Glide.with(context).load(imageUrl).into(rewardImageView);

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

    public void showRecordDialog(Context context, List<LuckyListReponse> recordReponses) {
        if (null == context) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity)context).isDestroyed())
                return;
        }
        dismiss();

        View dialogview = LayoutInflater.from(context).inflate(R.layout.drawtt_dialog_record, null);
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

        List<String> recordStringList = new ArrayList<> ();
        int showIndex = 0;
        for (int i = 0; i < recordReponses.size(); ++i) {
            LuckyListReponse response = recordReponses.get(i);
            if (3 == response.getStatus()) {
                continue;
            }
            showIndex++;
            recordStringList.add("" + showIndex);
            recordStringList.add(DateUtil.timeDotData(response.getCreate_time()));
            recordStringList.add(response.getLottery_name());
            if (1 == response.getStatus()) {
                recordStringList.add("已领取");
            } else if (2 == response.getStatus()) {
                recordStringList.add("未领取");
            } else {
                recordStringList.add("谢谢参与");
            }
        }
        recordGridView = (GridView) dialogview.findViewById(R.id.record_grid_view);
        if (null == recordAdapter) {
            recordAdapter = new DrawttRecordAdapter(context, recordStringList);
            recordGridView.setAdapter(recordAdapter);
        } else {
            recordAdapter.setRecordStringList(recordStringList);
            recordAdapter.notifyDataSetChanged();
        }

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
