package com.acmenxd.frame.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/7/26 14:11
 * @detail 设备相关Utils
 */
public class DeviceUtils {
    private static int screenX = 0; // 屏幕宽（px）
    private static int screenY = 0; // 屏幕高（px）
    private static int screenDipX = 0; // 屏幕宽（dip）
    private static int screenDipY = 0; // 屏幕高（dip）
    private static int densityDPI = 0; // 屏幕密度（每寸像素：120/160/240/320）
    private static float density = 0; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
    private static float scaledDensity = 0;
    private static float xdpi = 0;
    private static float ydpi = 0;

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context pContext) {
        int statusBarHeight = 0;
        int resourceId = pContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = pContext.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 屏幕宽（px）
     */
    public static int getScreenX(Context pContext) {
        if (screenX <= 0) {
            initDisplay(pContext);
        }
        return screenX;
    }

    /**
     * 屏幕高（px）
     */
    public static int getScreenY(Context pContext) {
        if (screenY <= 0) {
            initDisplay(pContext);
        }
        return screenY;
    }

    /**
     * 屏幕宽（dip）
     */
    public static int getScreenDipX(Context pContext) {
        if (screenDipX <= 0) {
            initDisplay(pContext);
        }
        return screenDipX;
    }

    /**
     * 屏幕高（dip）
     */
    public static int getScreenDipY(Context pContext) {
        if (screenDipY <= 0) {
            initDisplay(pContext);
        }
        return screenDipY;
    }

    /**
     * 屏幕密度（每寸像素：120/160/240/320）
     */
    public static int getDensityDPI(Context pContext) {
        if (densityDPI <= 0) {
            initDisplay(pContext);
        }
        return densityDPI;
    }

    /**
     * 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
     */
    public static float getDensity(Context pContext) {
        if (density <= 0) {
            initDisplay(pContext);
        }
        return density;
    }

    public static float getScaledDensity(Context pContext) {
        if (scaledDensity <= 0) {
            initDisplay(pContext);
        }
        return scaledDensity;
    }

    public static float getXdpi(Context pContext) {
        if (xdpi <= 0) {
            initDisplay(pContext);
        }
        return xdpi;
    }

    public static float getYdpi(Context pContext) {
        if (ydpi <= 0) {
            initDisplay(pContext);
        }
        return ydpi;
    }

    private static void initDisplay(Context pContext) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) pContext.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(dm);
        screenX = dm.widthPixels;
        screenY = dm.heightPixels;
        density = dm.density;
        densityDPI = dm.densityDpi;
        scaledDensity = dm.scaledDensity;
        xdpi = dm.xdpi;
        ydpi = dm.ydpi;
        screenDipX = (int) (screenX / density);
        screenDipY = (int) (screenY / density);
    }
}
