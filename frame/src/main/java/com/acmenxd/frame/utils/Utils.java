package com.acmenxd.frame.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.view.View;

import com.acmenxd.toaster.Toaster;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:36
 * @detail 工具类
 */
public final class Utils {
    public static final String UTF8 = "UTF-8";

    /**
     * 编码
     */
    public static String encode(@NonNull String s) {
        if (s == null) {
            return "";
        }
        try {
            return URLEncoder.encode(s, "UTF-8").replace("+", "%20")
                    .replace("*", "%2A").replace("%7E", "~")
                    .replace("#", "%23");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 反编码
     */
    public static String decode(@NonNull String s) {
        if (s == null) {
            return "";
        }
        try {
            return URLDecoder.decode(s, UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 设置单击间隔事件Listener
     */
    public static abstract class OnClickListener implements View.OnClickListener {
        /**
         * 经过处理后的onClick事件
         */
        public abstract void onClick2(View v);

        private int mDelayTime = 1000;
        private long mLastTime = 0;

        public OnClickListener() {

        }

        public OnClickListener(int delayTime) {
            mDelayTime = delayTime;
        }

        @Override
        public void onClick(View v) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - mLastTime >= mDelayTime) {
                mLastTime = currentTime;
                this.onClick2(v);
            }
        }
    }

    //调用文件选择软件来选择文件 -> 返回文件路径  ---------------start
    public static final int showFileChooser_requestCode = 0x0001; //requestCode状体码

    public static void showFileChooser(@NonNull Activity pActivity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            pActivity.startActivityForResult(Intent.createChooser(intent, "请选择要上传的文件"), showFileChooser_requestCode);
        } catch (android.content.ActivityNotFoundException ex) {
            Toaster.show("请安装文件管理器");
        }
    }

    public static String showFileChooser_onActivityResult(@NonNull Activity pActivity, int requestCode, int resultCode, @NonNull Intent data) {
        if (requestCode == showFileChooser_requestCode && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String path = "";
            String fileName;
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                String[] projection = {"_data"};
                Cursor cursor = null;
                try {
                    cursor = pActivity.getContentResolver().query(uri, projection, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow("_data");
                    if (cursor.moveToFirst()) {
                        path = cursor.getString(column_index);
                    }
                } catch (Exception e) {
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                path = uri.getPath();
            }
            if (!StringUtils.isEmpty(path)) {
                fileName = path.substring(path.lastIndexOf("/") + 1);
            }
            return path;
        }
        return "";
    }
    //调用文件选择软件来选择文件 ---------------end

    //---------------------------------以下函数已被IFrameUtils实现------------------------------

    /**
     * 根据手机的分辨率从 dp 的单位转成 px(像素)
     */
    public static float dp2px(@NonNull Context pContext, @FloatRange(from = 0) float dp) {
        return dp2px(pContext.getResources(), dp);
    }

    public static float dp2px(@NonNull Resources resources, @FloatRange(from = 0) float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    /**
     * 根据手机的分辨率从 px(像素)的单位转成 dp
     */
    public static float px2dp(@NonNull Context pContext, @FloatRange(from = 0) float px) {
        return px2dp(pContext.getResources(), px);
    }

    public static float px2dp(@NonNull Resources resources, @FloatRange(from = 0) float px) {
        final float scale = resources.getDisplayMetrics().density;
        return px / scale + 0.5f;
    }

    /**
     * 根据手机的分辨率从 sp 的单位转成 px(像素)
     */
    public static float sp2px(@NonNull Context pContext, @FloatRange(from = 0) float sp) {
        return sp2px(pContext.getResources(), sp);
    }

    public static float sp2px(@NonNull Resources resources, @FloatRange(from = 0) float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale + 0.5f;
    }

    /**
     * 根据手机的分辨率从 px(像素)的单位转成 sp
     */
    public static float px2sp(@NonNull Context pContext, @FloatRange(from = 0) float px) {
        return px2sp(pContext.getResources(), px);
    }

    public static float px2sp(@NonNull Resources resources, @FloatRange(from = 0) float px) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return px / scale + 0.5f;
    }

}
