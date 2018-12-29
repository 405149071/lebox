package com.mgc.letobox.happy.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mgc.letobox.happy.R;
import com.liang530.utils.BaseAppUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import static com.umeng.socialize.utils.ContextUtil.getContext;

/**
 * Created by zzh on 2018/3/14.
 */

/**
 * Created by zzh on 2018/3/14.
 */
public class ShareTypeDialog {
    private Dialog dialog;
    private ConfirmDialogListener mlistener;

    private Context mContext;

    ImageView ivQRCode;
    Bitmap mQRCodeBitmap;

    LinearLayout ll_share_content;

    public void showDialog(Context context, String content, ConfirmDialogListener listener) {
        if (content == null) return;
        dismiss();
        mContext = context;
        this.mlistener = listener;
        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_share_type, null);
        dialog = new Dialog(context, R.style.dialog_bg_style);
        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(true);
        //dialog默认是环绕内容的
        //通过window来设置位置、高宽
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        windowparams.width = BaseAppUtil.getDeviceWidth(context);
//        设置背景透明,但是那个标题头还是在的，只是看不见了


        LinearLayout ll_close = (LinearLayout) dialogview.findViewById(R.id.ll_close);
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistener != null) {
                    mlistener.cancel();
                }
                dismiss();
            }
        });

        LinearLayout ll_image = (LinearLayout) dialogview.findViewById(R.id.ll_image);

        ll_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistener != null) {


                    String filepath = createShareFile();

                    mlistener.setShareType(0, filepath);

//                    ll.setDrawingCacheEnabled(true);
//                    ll.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//                    ll.layout(0, 0, ll.getMeasuredWidth(), ll.getMeasuredHeight());
//                    Bitmap bitmap = Bitmap.createBitmap(ll.getDrawingCache());
//                    ll.setDrawingCacheEnabled(false);
//                    iv.setImageBitmap(bitmap);


                }
                dismiss();
            }
        });
        LinearLayout ll_link = (LinearLayout) dialogview.findViewById(R.id.ll_link);
        ll_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistener != null) {
                    mlistener.setShareType(1, null);
                }
                dismiss();
            }
        });

        ll_share_content = dialogview.findViewById(R.id.ll_share_content);
        ivQRCode = dialogview.findViewById(R.id.iv_share_qrcode);
        generateQRCode(content);


        dialog.show();
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            mlistener = null;
        }
    }

    public interface ConfirmDialogListener {
        void setShareType(int type, String filepath);

        void cancel();
    }

    private void generateQRCode(String url) {
        int width = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, mContext.getResources().getDisplayMetrics()));

        mQRCodeBitmap = CodeUtils.createImage(url, width, width, null);
        ivQRCode.setImageBitmap(mQRCodeBitmap);

    }


    /**
     * 创建分享的图片文件
     */
    public String createShareFile() {
        Bitmap bitmap = createBitmap();
        //将生成的Bitmap插入到手机的图片库当中，获取到图片路径
        String filePath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, null, null);
        //及时回收Bitmap对象，防止OOM
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        //转uri之前必须判空，防止保存图片失败
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        return getRealPathFromURI(getContext(), Uri.parse(filePath));
    }

    /**
     * 创建分享Bitmap
     */
    private Bitmap createBitmap() {

        ll_share_content.setDrawingCacheEnabled(true);
        ll_share_content.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        ll_share_content.layout(0, 0, ll_share_content.getMeasuredWidth(), ll_share_content.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(ll_share_content.getDrawingCache());
        ll_share_content.setDrawingCacheEnabled(false);

//
//        //自定义ViewGroup，一定要手动调用测量，布局的方法
//        measure(getLayoutParams().width, getLayoutParams().height);
//        layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
//        //如果图片对透明度无要求，可以设置为RGB_565
//        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        draw(canvas);
        return bitmap;
    }

    private static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor == null) {
                return "";
            }
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


}
