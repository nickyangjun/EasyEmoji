package org.nicky.easyemoji.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.Target;

import org.nicky.easyemoji.image.transform.GlideCircleTransform;
import org.nicky.easyemoji.image.transform.GlideRoundTransform;
import org.nicky.easyemoji.utils.CommonUtil;


/**
 * 图片加载 展示工具类
 * 可加载网络 本地 资源 Assets 流图片等
 * 本工程涉及到图片加载展示的需求统一用此类处理
 * Created by Seaky on 2017/3/4.
 */

public class ImageLoader {
    // 单例模式
    private volatile static ImageLoader instance;
    //缓存策略 只缓存原图

    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }


    /**
     * 重载方法 加载展示圆角图片
     *
     * @param round 圆角矢量 由调用方控制
     */
    public void displayImage(Context context, String uri, ImageView view, int defaultIcon, int round) {
        RequestBuilder<Bitmap> req = getRequestBitmapBuilder(uri, view);
        if (defaultIcon > 0) {
            req.placeholder(defaultIcon).error(defaultIcon);
        }

        req.transform(new CenterCrop(), new RoundedCorners(CommonUtil.dip2px(view.getContext(), round)));

        req.into(view);
    }


    /**
     * 重载方法 加载展示圆形图片
     *
     * @param round 调用方传布尔值即视为要加载圆形图片
     */
    public void displayImage(Context context, String uri, ImageView view, int defaultIcon, boolean round) {
        Glide.with(context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(new GlideCircleTransform())
                .placeholder(defaultIcon)
                .error(defaultIcon)
                .into(view);
    }

    /**
     * 重载方法 指定图片的宽高
     *
     * @param width  指定宽
     * @param height 指定高
     */
    public void displayImage(Context context, String uri, ImageView view, int defaultIcon, int width, int height) {
        Glide.with(context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(defaultIcon)
                .error(defaultIcon)
                .centerCrop()
                .override(width, height)
                .into(view);
    }

    //重载方法 指定宽高的圆角图
    public void displayImage(Context context, String uri, ImageView view, int defaultIcon, int width, int height, int round) {
        Glide.with(context)
                .load(uri)
                .transform(new GlideRoundTransform(round))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(defaultIcon)
                .error(defaultIcon)
                .centerCrop()
                .override(width, height)
                .into(view);
    }


    //同步方法 获取到bitmap对象
    public Bitmap loadImageSync(Context context, String uri) {
        try {
            return Glide.with(context.getApplicationContext())
                    .asBitmap()
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            return null;
        }
    }

    static RequestBuilder<Bitmap> getRequestBitmapBuilder(String uri, @NonNull View view) {
        return Glide.with(view.getContext())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .load(uri)
                .dontAnimate()
                .thumbnail(0.06f);
    }

    public interface LoadImageListener {
        void onLoadingComplete(Bitmap bitmap);
    }

    public interface DisplayListener {
        void onLoadCompleted(String imageUri, ImageView view, Bitmap bitmap);

        void onLoadFailed(String imageUri, ImageView view);
    }

}
