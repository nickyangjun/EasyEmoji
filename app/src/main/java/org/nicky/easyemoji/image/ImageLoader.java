package org.nicky.easyemoji.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import org.nicky.easyemoji.image.transform.GlideCircleTransform;
import org.nicky.easyemoji.image.transform.GlideRoundTransform;


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
    private DiskCacheStrategy mDiskCacheStrategy = DiskCacheStrategy.SOURCE;

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

    /**---这两个方法用于列表滑动时暂停，停止滑动时恢复加载，使列表滑动流畅-----  */
    //恢复加载  注 : context参数要和调用加载方法传的一致
    public void resume(Context context) {
        Glide.with(context).resumeRequests();
    }
    //暂停加载
    public void pause(Context context) {
        Glide.with(context).pauseRequests();
    }

    /**
     * 核心图片加载函数
     * context : Context/Activity/Fragment 最好传activity 可与生命周期绑定
     * 参数uri : 网络图片url/本地图片path/资源文件ID/byte[]
     * view   : 要展示图片的控件
     * defaultIcon : 默认占位图
     */
    public void displayImage(Context context, String uri, ImageView view , int defaultIcon){
        GenericRequestBuilder req =  Glide.with(context)
                .load(uri)
                .diskCacheStrategy(mDiskCacheStrategy);
                if(defaultIcon >0 ){
                    req.placeholder(defaultIcon)
                            .error(defaultIcon);
                }
        req.into(view);
    }

    /**
     * 重载方法 加载展示圆角图片
     * @param round 圆角矢量 由调用方控制
     */
    public void displayImage(Context context, String uri, ImageView view, int defaultIcon, int round) {
        GenericRequestBuilder req =  Glide.with(context)
                .load(uri)
                .diskCacheStrategy(mDiskCacheStrategy)
                .transform(new GlideRoundTransform(context,round));
                if(defaultIcon >0) {
                    req.placeholder(defaultIcon)
                            .error(defaultIcon);
                }
                req.into(view);
    }

    public void displayImage(final String uri, final ImageView imageView, int defaultIcon, final DisplayListener listener) {
        GenericRequestBuilder req = Glide.with(imageView.getContext().getApplicationContext()).load(uri).asBitmap().diskCacheStrategy(mDiskCacheStrategy);
        if (defaultIcon > 0) {
            req.placeholder(defaultIcon);
        }
        req.into(new BitmapImageViewTarget(imageView) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                super.onResourceReady(resource, glideAnimation);
                if (listener != null) {
                    listener.onLoadCompleted(uri, imageView, resource);
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                if (listener != null) {
                    listener.onLoadFailed(uri, imageView);
                }
            }
        });
    }


    /**
     * 重载方法 加载展示圆形图片
     * @param round 调用方传布尔值即视为要加载圆形图片
     */
    public void displayImage(Context context, String uri, ImageView view , int defaultIcon, boolean round) {
        Glide.with(context)
                .load(uri)
                .diskCacheStrategy(mDiskCacheStrategy)
                .transform(new GlideCircleTransform(context))
                .placeholder(defaultIcon)
                .error(defaultIcon)
                .into(view);
    }

    /**
     * 重载方法 指定图片的宽高
     * @param width  指定宽
     * @param height 指定高
     */
    public void displayImage(Context context, String uri, ImageView view , int defaultIcon, int width , int height) {
        Glide.with(context)
                .load(uri)
                .diskCacheStrategy(mDiskCacheStrategy)
                .placeholder(defaultIcon)
                .error(defaultIcon)
                .centerCrop()
                .override(width, height)
                .into(view);
    }

    //重载方法 指定宽高的圆角图
    public void displayImage(Context context, String uri, ImageView view , int defaultIcon, int width , int height , int round) {
        Glide.with(context)
                .load(uri)
                .transform(new GlideRoundTransform(context,round))
                .diskCacheStrategy(mDiskCacheStrategy)
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
                    .load(uri)
                    .asBitmap()
                    .diskCacheStrategy(mDiskCacheStrategy)
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            return null;
        }
    }

    //同步方法 获取到bitmap对象并且不缓存
    public Bitmap loadImageSyncSkipCache(Context context, String uri) {
        try {
            return Glide.with(context.getApplicationContext())
                    .load(uri)
                    .asBitmap()
                    .diskCacheStrategy(mDiskCacheStrategy)
                    .skipMemoryCache(true)
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            return null;
        }
    }

    //兼容方法
    public void displayImageAsBitmap(String uri, ImageView imageView,int icon) {
        GenericRequestBuilder req =  Glide.with(imageView.getContext().getApplicationContext()).load(uri).asBitmap()
                .diskCacheStrategy(mDiskCacheStrategy);
        if(icon > 0 ) {
            req.error(icon);
        }
        req.into(imageView);
    }
    //兼容方法
    public void loadCircleImage(Context context,String uri, int placeholder, int error, final LoadImageListener listener){
        GenericRequestBuilder req = Glide.with(context.getApplicationContext()).load(uri).asBitmap().diskCacheStrategy(mDiskCacheStrategy).skipMemoryCache(true)
                .transform(new GlideCircleTransform(context));
        if (placeholder > 0) {
            req.placeholder(placeholder);
        }
        if (error > 0) {
            req.error(error);
        }
        req.into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                listener.onLoadingComplete(bitmap);
            }
        });
    }

    public void loadImage(Context context, String uri, int defaultIcon, final LoadImageListener listener) {
        GenericRequestBuilder req = Glide.with(context.getApplicationContext()).load(uri).asBitmap().diskCacheStrategy(mDiskCacheStrategy).skipMemoryCache(true);
        if (defaultIcon > 0) {
            req.placeholder(defaultIcon);
            req.error(defaultIcon);
        }
        req.into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                listener.onLoadingComplete(bitmap);
            }
        });
    }

    public interface LoadImageListener {
        void onLoadingComplete(Bitmap bitmap);
    }

    public interface DisplayListener {
        void onLoadCompleted(String imageUri, ImageView view, Bitmap bitmap);
        void onLoadFailed(String imageUri, ImageView view);
    }

    //清理缓存
    public void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }
}
