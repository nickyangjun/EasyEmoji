package org.nicky.easyemoji.image.transform;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * 2022/01/18 14:52.
 * <p>
 * Glide圆形
 */
public class GlideCircleTransform extends BitmapTransformation {
    private static final String ID = "GlideCircleTransform";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private final Paint mBorderPaint;
    private final float mBorderSize;

    public GlideCircleTransform() {
        this(0, Color.TRANSPARENT);
    }


    /**
     * @param borderSize  边框宽度(px)
     * @param borderColor 边框颜色
     */
    public GlideCircleTransform(float borderSize, int borderColor) {
        mBorderSize = borderSize;
        mBorderPaint = new Paint();
        mBorderPaint.setDither(true);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderSize);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = (int) (Math.min(source.getWidth(), source.getHeight()) - (mBorderSize / 2));
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        if (mBorderPaint != null) {
            float borderRadius = r - mBorderSize / 2;
            canvas.drawCircle(r, r, borderRadius, mBorderPaint);
        }
        return result;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
//        Bitmap b = TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight);
//        Canvas canvas = new Canvas(b);
//        canvas.drawArc(new RectF(mBorderSize/2, mBorderSize/2, (b.getWidth() - mBorderSize/2), (b.getHeight() - mBorderSize/2)), 0f, 360f, true, mBorderPaint);
        return circleCrop(pool, toTransform);
//        return b;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GlideCircleTransform;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}

