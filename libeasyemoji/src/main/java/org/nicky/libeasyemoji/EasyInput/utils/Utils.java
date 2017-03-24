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
}
