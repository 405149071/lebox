package com.mgc.letobox.happy.ui.signin;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.liang530.utils.BaseAppUtil;

/**
 *
 */
public class FlashView extends View {

    private ObjectAnimator objectAnimator;

    private ValueAnimator mAnimtor;
    /**
     * 从0开始,偶数圆弧的背景色
     */
    private int mEventArcBgColor = Color.parseColor("#E7EAEF");
    /**
     * 奇数圆弧的背景色
     */
    private int mOddArcBgColor = Color.WHITE;
    /**
     * 抽奖转盘中条目的个数.默认12个
     */
    private int mItemCount = 24;
    /**
     * 每个弧形跨过的角度
     */
    private float mSweepAngle;
    /**
     * 圆盘的半径
     */
    private int radius = 0;
    /**
     * 圆弧的绘制区域
     */
    private RectF mRectF;

    //圆环图片
    private Paint mEventPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mOddPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mTextColor = Color.parseColor("#935f64");
    private int mPadding;
    private int mInitAngle = 0;
    private boolean isRotating;
    private int mWidth;
    //绘制扇形的半径 减掉50是为了防止边界溢出  具体效果你自己注释掉-50自己测试
    private int mRadius;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mInitAngle += 15;
            ViewCompat.postInvalidateOnAnimation(FlashView.this);
            mHandler.sendEmptyMessageDelayed(0, 100);
            return false;
        }
    });


    public FlashView(Context context) {
        this(context, null);
    }

    public FlashView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mSweepAngle = 360 / mItemCount;

        init();
    }

    private void init() {
        mEventPaint.setColor(mEventArcBgColor);
        mOddPaint.setColor(mOddArcBgColor);

        mTextPaint.setStrokeWidth(BaseAppUtil.dip2px(getContext(), 1));
        mTextPaint.setStyle(Paint.Style.STROKE);
        // mTextPaint.setColor(Color.parseColor("#E7EAEF"));
        mTextPaint.setColor(Color.parseColor("#8b2af3"));
        mTextPaint.setAlpha(250);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Math.max(getMeasuredWidth(), getMeasuredHeight());
        // padding值.四个padding值保持一致
        mPadding = getPaddingLeft();
        mWidth = width;

        //绘制扇形的半径 减掉50是为了防止边界溢出  具体效果你自己注释掉-50自己测试
        mRadius = mWidth;

        //设置为正方形
        //setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {


        //注意:这里的width和height是实际的绘制区域,减去了padding值
        int width = getWidth() - mPadding - mPadding;
        int height = getHeight() - mPadding - mPadding;

        int minValue = Math.max(width, height);
        //半径
        radius = width + height;

        //圆弧的绘制区域
        mRectF = new RectF(getPaddingLeft() - getMeasuredWidth() / 2, getPaddingTop() - getMeasuredWidth() / 2, getMeasuredWidth() - getPaddingRight() + getMeasuredWidth() / 2, getMeasuredHeight() - getPaddingBottom() + getMeasuredWidth() / 2);

        ViewCompat.postInvalidateOnAnimation(FlashView.this);

    }

    private float[] rids;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0,   -BaseAppUtil.dip2px(getContext(), 4), getWidth(), getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#E7EAEF"));
        paint.setAlpha(220);

        // canvas.drawRoundRect(mRectF, BaseAppUtil.dip2px(getContext(), 4), BaseAppUtil.dip2px(getContext(), 4), mTextPaint);

        float rid = BaseAppUtil.dip2px(getContext(), 4);
        //创建圆角数组
        rids = new float[]{0.0f, 0.0f, 0.0f, 0.0f, rid, rid, rid, rid};
        Path path = new Path();
        path.addRoundRect(rectF, rids, Path.Direction.CW);
        canvas.clipPath(path);

        int angle = mInitAngle;

        // 绘制圆弧
        for (int i = 0; i < mItemCount; i++) {
            if (i % 2 == 0) {
                paint.setColor(mOddArcBgColor);
                //canvas.drawArc(mRectF, angle, mSweepAngle, true, paint);
                canvas.drawArc(mRectF, angle, mSweepAngle, true, paint);
            } else {
                paint.setColor(mEventArcBgColor);
                //canvas.drawArc(mRectF, angle, mSweepAngle, true, paint);
                canvas.drawArc(mRectF, angle, mSweepAngle, true, paint);
            }
            angle += mSweepAngle;
        }
        /**
         * 使用SRC_IN
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawRoundRect(rectF, BaseAppUtil.dip2px(getContext(), 4), BaseAppUtil.dip2px(getContext(), 4), mTextPaint);

    }

    /**
     * 开始转动
     */
    public void startRotate() {
        if (isRotating) return;
        isRotating = true;
        mHandler.sendEmptyMessage(0);

//        objectAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);//添加旋转动画，旋转中心默认为控件中点
//        objectAnimator.setDuration(3000);//设置动画时间
//        objectAnimator.setInterpolator(new LinearInterpolator());//动画时间线性渐变
//        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
//        objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
//        objectAnimator.start();

//        mAnimtor = ValueAnimator.ofInt(15, 15);
//        mAnimtor.setInterpolator(new LinearInterpolator());
//        mAnimtor.setDuration(3000);
//        mAnimtor.setRepeatCount(ValueAnimator.INFINITE);//无限循环
//        mAnimtor.setRepeatMode(ValueAnimator.RESTART);//无限循环
//        mAnimtor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int updateValue = (int) animation.getAnimatedValue();
//                mInitAngle += updateValue;
//                ViewCompat.postInvalidateOnAnimation(FlashView.this);
//            }
//        });
//        mAnimtor.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                isRotating = true;
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                isRotating = false;
//
//
//            }
//        });
//        mAnimtor.start();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startRotate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearAnimation();
        isRotating = false;
        mHandler.removeMessages(0);
    }


}
