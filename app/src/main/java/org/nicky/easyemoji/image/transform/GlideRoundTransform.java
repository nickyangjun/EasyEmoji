package org.nicky.easyemoji.image.transform;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

/**
 * 2022/01/18 13:30.
 * <p>
 * Glide圆角
 */
public class GlideRoundTransform extends BitmapTransformation {

    /**
     * 边框
     */
    public static class Border {

        private final float borderSize;
        private final int borderColor;

        /**
         * 边框属性
         *
         * @param borderSize  宽度(dp为单位)
         * @param borderColor 颜色
         */
        public Border(float borderSize, int borderColor) {
            this(TypedValue.COMPLEX_UNIT_DIP, borderSize, borderColor);
        }

        /**
         * 边框属性
         *
         * @param unit        borderSize单位
         * @param borderSize  宽度
         * @param borderColor 颜色
         */
        public Border(int unit, float borderSize, int borderColor) {
            DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
            this.borderSize = TypedValue.applyDimension(unit, borderSize, displayMetrics);
            this.borderColor = borderColor;
        }
    }

    /**
     * 圆角角度
     */
    private final float mRadius;

    /**
     * 边框
     */
    private final Paint mBorderPaint;

    /**
     * 圆角
     *
     * @param radius 角度(单位dp)
     */
    public GlideRoundTransform(float radius) {
        this(TypedValue.COMPLEX_UNIT_DIP, radius, null);
    }

    /**
     * 圆角
     *
     * @param unit   角度单位
     * @param radius 角度(单位dp)
     */
    public GlideRoundTransform(int unit, float radius) {
        this(unit, radius, null);
    }

    /**
     * 圆角
     *
     * @param unit   角度单位
     * @param radius 角度(单位dp)
     * @param border 边框
     */
    public GlideRoundTransform(int unit, float radius, Border border) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        this.mRadius = TypedValue.applyDimension(unit, radius, displayMetrics);
        if (border != null) {
            mBorderPaint = new Paint();
            mBorderPaint.setDither(true);
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(border.borderColor);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(border.borderSize);
        } else {
            mBorderPaint = null;
        }
    }

    @Override
    protected Bitmap transform( BitmapPool pool,  Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
        return roundCrop(pool, bitmap);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, mRadius, mRadius, paint);

        if (mBorderPaint != null) {
            canvas.drawRoundRect(rectF, mRadius, mRadius, mBorderPaint);
        }
        return result;
    }

    public String getId() {
        return getClass().getName() + Math.round(mRadius);
    }

    @Override
    public void updateDiskCacheKey( MessageDigest messageDigest) {

    }
}

