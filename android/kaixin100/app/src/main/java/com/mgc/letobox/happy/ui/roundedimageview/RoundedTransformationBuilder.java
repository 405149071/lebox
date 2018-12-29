package com.mgc.letobox.happy.ui.roundedimageview;

/**
 * Create by zhaozhihui on 2018/12/28
 **/
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ImageView.ScaleType;

public final class RoundedTransformationBuilder {
    private final DisplayMetrics mDisplayMetrics;
    private float[] mCornerRadii = new float[]{0.0F, 0.0F, 0.0F, 0.0F};
    private boolean mOval = false;
    private float mBorderWidth = 0.0F;
    private ColorStateList mBorderColor = ColorStateList.valueOf(-16777216);
    private ScaleType mScaleType;

    public RoundedTransformationBuilder() {
        this.mScaleType = ScaleType.FIT_CENTER;
        this.mDisplayMetrics = Resources.getSystem().getDisplayMetrics();
    }

    public RoundedTransformationBuilder scaleType(ScaleType scaleType) {
        this.mScaleType = scaleType;
        return this;
    }

    public RoundedTransformationBuilder cornerRadius(float radius) {
        this.mCornerRadii[0] = radius;
        this.mCornerRadii[1] = radius;
        this.mCornerRadii[2] = radius;
        this.mCornerRadii[3] = radius;
        return this;
    }

    public RoundedTransformationBuilder cornerRadius(int corner, float radius) {
        this.mCornerRadii[corner] = radius;
        return this;
    }

    public RoundedTransformationBuilder cornerRadiusDp(float radius) {
        return this.cornerRadius(TypedValue.applyDimension(1, radius, this.mDisplayMetrics));
    }

    public RoundedTransformationBuilder cornerRadiusDp(int corner, float radius) {
        return this.cornerRadius(corner, TypedValue.applyDimension(1, radius, this.mDisplayMetrics));
    }

    public RoundedTransformationBuilder borderWidth(float width) {
        this.mBorderWidth = width;
        return this;
    }

    public RoundedTransformationBuilder borderWidthDp(float width) {
        this.mBorderWidth = TypedValue.applyDimension(1, width, this.mDisplayMetrics);
        return this;
    }

    public RoundedTransformationBuilder borderColor(int color) {
        this.mBorderColor = ColorStateList.valueOf(color);
        return this;
    }

    public RoundedTransformationBuilder borderColor(ColorStateList colors) {
        this.mBorderColor = colors;
        return this;
    }

    public RoundedTransformationBuilder oval(boolean oval) {
        this.mOval = oval;
        return this;
    }
}
