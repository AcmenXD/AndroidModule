package com.acmenxd;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.acmenxd.frame.utils.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Mzc on 2016/8/26.
 * style工具
 */

public class StyleUtil {
    /**
     * 设置状态栏透明
     * @param mContext
     */
    public static void setNavTrans(Activity mContext){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mContext.getWindow();
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1){
                window.setStatusBarColor(0x57000000);
            }else{
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window window = mContext.getWindow();
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            Window window = mContext.getWindow();
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }
    }

    /**
     * 设置状态栏文字颜色为浅色或深色
     *  0x00002000这个值在6.0API中为 View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
     * @param activity  上下文
     * @param lightStatusBar    ture为浅色
     */
    public static void setNavTitleLight(Activity activity, boolean lightStatusBar){
        if (Build.VERSION.SDK_INT >= 23) {
            activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|0x00002000);
            View decor = activity.getWindow().getDecorView();
            int ui = decor.getSystemUiVisibility();
            if (lightStatusBar) {
                ui |=0x00002000;
            } else {
                ui &= ~0x00002000;
            }
            decor.setSystemUiVisibility(ui);
            setMiuiStatusBarDarkMode(activity,lightStatusBar);
        }
    }
    //针对小米6.0系统 设置状态栏文字颜色
    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 设置状态栏
     * @param mContext 上下文
     * @param view  顶部控件增加高度
     * @param height    高度值
     * @param lightStatusBar    文字浅色或深色
     */
    public static void setTitleBarHeight(Context mContext,View view,int height,boolean lightStatusBar) {
        int titleBarHeight = (int) Utils.dp2px(mContext, height);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            titleBarHeight = Utils.dp2px(mContext, height) + DeviceInfo.getStateBarHeight(mContext);
//            view.setPadding(0,DeviceInfo.getStateBarHeight(mContext),0,0);
        }
        StyleUtil.setNavTitleLight(((Activity)mContext),lightStatusBar);
        view.getLayoutParams().height = titleBarHeight;
    }

}
