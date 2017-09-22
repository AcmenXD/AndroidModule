package com.acmenxd.frame.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/9/21 18:19
 * @detail 状态栏工具类
 */
public class StatusBarUtils {
    /**
     * 设置状态栏全透明
     */
    public static void setStatusBarTranslucent(Activity pActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 4.4 以上(含)
            Window window = pActivity.getWindow();
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 5.0 以上(含)
                window.setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 4.4 以上(含)
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        } else {
            // 4.4 以下(不含)
            Window window = pActivity.getWindow();
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }
    }

    /**
     * 设置改变状态栏模式 - 深浅色
     *
     * @param pActivity
     * @param pIsDark
     */
    public static boolean setModeStatusBar(Activity pActivity, boolean pIsDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (setModeStatusBar_MIUI(pActivity, pIsDark) || setModeStatusBar_Flyme(pActivity, pIsDark)) {
                return true;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                pActivity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | (pIsDark ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : View.SYSTEM_UI_FLAG_VISIBLE));
                return true;
            }
        }
        return false;
    }

    /**
     * (MIUI)设置改变状态栏模式 - 深浅色
     *
     * @param pActivity
     * @param pIsDark
     */
    public static boolean setModeStatusBar_MIUI(Activity pActivity, boolean pIsDark) {
        boolean result = false;
        Window window = pActivity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(window, (pIsDark ? darkModeFlag : 0), darkModeFlag);
                result = true;
                //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pActivity.getWindow().getDecorView().setSystemUiVisibility(
                            pIsDark ? View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : View.SYSTEM_UI_FLAG_VISIBLE);
                }
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * (魅族)设置改变状态栏模式 - 深浅色
     *
     * @param pActivity
     * @param pIsDark
     */
    public static boolean setModeStatusBar_Flyme(Activity pActivity, boolean pIsDark) {
        boolean result = false;
        Window window = pActivity.getWindow();
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (pIsDark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }
}
