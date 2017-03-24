package org.nicky.libeasyemoji.EasyInput.utils;

import android.content.Context;

/**
 * Created by nicky on 15/3/6.
 * 这是一个用于转换单位的类
 */
public class ConvertUtil {

    /**
     * 将int转换为String
     */
    public static String toString(int i) {
        return i + "";
    }

    /**
     * 将float转换为String
     */
    public static String toString(float i) {
        return i + "";
    }

    /**
     * 将long转换为String
     */
    public static String toString(long l) {
        return l + "";
    }

    /**
     * 将String装换为float
     */
    public static float toFloat(String s) {
        if ((s == null) || s.equals("")) {
            return 0;
        }

        float f = 0;
        try {
            f = Float.parseFloat(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return f;
    }

    /**
     * 将String转换为double
     */
    public static double toDouble(String s) {
        if ((s == null) || s.equals("")) {
            return 0;
        }

        double d = 0;
        try {
            d = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 将String转换为int
     */
    public static int toInt(String s) {
        if ((s==null) || s.equals("")) {
            return 0;
        }
        int i = 0;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 将String转换为long
     */
    public static long toLong(String s) {
        if ((s==null) || s.equals("")) {
            return 0;
        }
        long l = 0;
        try {
            l = Long.parseLong(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return l;
    }

    /**
     * 将String转换为Boolean
     */
    public static Boolean toBoolean(String s) {
        try {
            return Boolean.parseBoolean(s);
        } catch (Exception e) {
            return false;
        }
    }

    // 根据720基准分辨率下的像素，获得该设备的真实像素
    public static float getpx(Context context, float px) {
        // 1080/720 * px
        return getWindowWidthPX(context) / (float)720.0 * px;
    }

    //dip To  px
    public static int dip2px(Context context, int dp) {
        //dp和px的转换关系
        float density = context.getResources().getDisplayMetrics().density;
        //2*1.5+0.5  2*0.75 = 1.5+0.5
        return (int)(dp*density+0.5);
    }

    public static int dp2px(Context context, int dp) {
        return dip2px(context, dp);
    }

    //px  To  dip
    public static int px2dip(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int)(px/density+0.5);
    }

    public static int px2dp(Context context, int px) {
        return px2dip(context, px);
    }

    /**
     * 获得当前手机屏幕宽度的dp值
     * @return 返回DP值
     */
    public static int getWindowWidthDP(Context context) {
        int widthDP = ConvertUtil.px2dip(context, getWindowWidthPX(context));

        return widthDP;
    }

    /**
     * 获得当前手机屏幕高度的px值
     * @return 返回像素值
     */
    public static int getWindowHeightPX(Context context){
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        return heightPixels;
    }

    /**
     * 获得当前手机屏幕高度的px值
     * @return 返回像素值
     */
    public static int getWindowWidthPX(Context context) {
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        return widthPixels;
    }

}
