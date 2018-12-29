package com.mgc.letobox.happy.walfare.drawcash;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.kymjs.rxvolley.RxVolley;
import com.leto.game.base.bean.BaseRequestBean;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.mgc.letobox.happy.R;
import com.liang530.log.T;
import com.mgc.letobox.happy.base.BaseActivity;
import com.mgc.letobox.happy.config.IntentContant;
import com.mgc.letobox.happy.config.SdkApi;
import com.mgc.letobox.happy.domain.DrawCashWeixinInfoResultBean;
import com.mgc.letobox.happy.util.GsonUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.umeng.socialize.utils.ContextUtil.getContext;

/**
 * Create by zhaozhihui on 2018/8/23
 **/
public class BindWeixinActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.rl_left)
    RelativeLayout rl_left;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_methord2)
    TextView tv_methord2;



//    @BindView(R.id.et_account)
//    EditText et_account;

    @BindView(R.id.iv_qrcode)
    ImageView iv_qrcode;

    @BindView(R.id.ll_qrcode)
    LinearLayout ll_qrcode;


    DrawCashWeixinInfoResultBean data;

    Bitmap qrCodeBitmap;


    public static void start(Context mContext, DrawCashWeixinInfoResultBean data) {
        Intent mIntent = new Intent(mContext, BindWeixinActivity.class);
        mIntent.putExtra(IntentContant.EXTRA_DRAW_CASH_WEIXIN_BIND, data);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_weixin);
        ButterKnife.bind(this);

        StatusBarUtil.setTransparentForImageView(this, null);

        data = (DrawCashWeixinInfoResultBean) getIntent().getSerializableExtra(IntentContant.EXTRA_DRAW_CASH_WEIXIN_BIND);

        initView();
    }


    private void initView() {
        rl_left.setOnClickListener(this);
        iv_qrcode.setOnClickListener(this);
        tv_methord2.setOnClickListener(this);

        tv_title.setText("绑定微信公众号");

        SpannableString spannableString = new SpannableString(tv_methord2.getText().toString());
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 40, 52, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_methord2.setText(spannableString);

//        GlideApp.with(BindWeixinActivity.this).asBitmap().load(data.getPic()).error(R.mipmap.gzh_qrcode).into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
//
//            if (bitmap != null) {
//                iv_qrcode.setImageBitmap(bitmap);
//                qrCodeBitmap = bitmap;
//            }
//            }
//        });

        Glide.with(BindWeixinActivity.this).load(data.getPic()).into(iv_qrcode);
    }

    @Override
    public void onResume() {
        super.onResume();
        getWeixinInfo();

    }

    @Override
    public void onDestroy(){

        super.onDestroy();

//        if(null!=qrCodeBitmap){
//            qrCodeBitmap.recycle();
//            qrCodeBitmap =null;
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_left:
                finish();
                break;
            case R.id.iv_qrcode:
//                boolean isSuc = ImageUtil.saveImageUrlToLocal(BindWeixinActivity.this, data.getPic());
                boolean isSuc = createShareFile(iv_qrcode.getDrawingCache());
                if(isSuc){
                    T.s(BindWeixinActivity.this, "已保存到本地");
                }
                break;
            case R.id.tv_methord2:
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", "jiujiuledong");
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);

                T.s(this, "已复制到粘贴板");

                break;
        }
    }

    public boolean createShareFile(Bitmap bitmap) {

        if (bitmap == null) return false;
        //将生成的Bitmap插入到手机的图片库当中，获取到图片路径
        String filePath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, null, null);
        //及时回收Bitmap对象，防止OOM
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        //转uri之前必须判空，防止保存图片失败
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setData(uri);
        sendBroadcast(intent);
        return true;
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

    private void getWeixinInfo() {
        BaseRequestBean requestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<DrawCashWeixinInfoResultBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(DrawCashWeixinInfoResultBean data) {
                if (data != null) {
                    if (data.getIs_sub() == 1) {
                        DrawCashWeixinActivity.start(BindWeixinActivity.this, data);
                        finish();
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(SdkApi.getDrawCashWeixinInfo(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
}