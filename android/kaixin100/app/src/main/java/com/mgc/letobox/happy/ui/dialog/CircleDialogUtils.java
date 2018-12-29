package com.mgc.letobox.happy.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.ui.KeyboardDetectorRelativeLayout;

/**
 * Created by DELL on 2018/7/3.
 */

public class CircleDialogUtils {

    private static Toast mToast;

    KeyboardDetectorRelativeLayout root_layout;
    EditText et;
    Button btn;


    public static void createToast(Context context, String titleName, String text) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed())
                return;
        }
        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        TextView textView = view.findViewById(R.id.textView);
        TextView textViewTitle = view.findViewById(R.id.title);

        if (titleName != null && !titleName.isEmpty()) {
            textViewTitle.setText(titleName);
        }

        if (text != null && !text.isEmpty()) {
            textView.setText(text);
        }

        if (mToast == null) {
            mToast = new Toast(context);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(view);
        mToast.show();
    }
}
