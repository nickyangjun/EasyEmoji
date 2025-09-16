package org.nicky.libeasyemoji.EasyInput.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nickyang on 2017/3/21.
 */

public class Utils {
    private static final String FILE_NAME = "emoji_data";   // 文件名 share_data.xml

    public static boolean saveKeyboardHeight(Context context, int value) {
        try {
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            return sp.edit().putInt("keyboardHeight", value).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getKeyboardHeight(Context context,int defaultValue) {
        try {
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            return sp.getInt("keyboardHeight", defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static boolean isHandleByPlaceholder(boolean isFullScreen, boolean isTranslucentStatus,
                                                boolean isFitsSystem) {
        return isFullScreen || (isTranslucentStatus && !isFitsSystem);
    }

    static boolean isHandleByPlaceholder(final Activity activity) {
        return isHandleByPlaceholder(ViewUtil.isFullScreen(activity),
                ViewUtil.isTranslucentStatus(activity), ViewUtil.isFitsSystemWindows(activity));
    }

    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 判断是否应该使用 API 35+ 模式
     * 需要同时满足：系统版本 >= 35 且应用 targetSdkVersion >= 35
     */
    public static boolean shouldUseApi35Mode(Context context) {
        if (android.os.Build.VERSION.SDK_INT < 35) {
            return false;
        }

        try {
            int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
            return targetSdkVersion >= 35;
        } catch (Exception e) {
            // 如果获取失败，默认使用传统模式
            return false;
        }
    }
}
