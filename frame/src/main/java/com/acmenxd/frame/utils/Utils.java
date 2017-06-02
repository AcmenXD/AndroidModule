package com.acmenxd.frame.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.acmenxd.toaster.Toaster;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:36
 * @detail 工具类
 */
public class Utils {
    public static final String UTF8 = "UTF-8";

    /**
     * 反编码
     */
    public static String decode(String s) {
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
     * 编码
     */
    public static String encode(String s) {
        if (s == null) {
            return "";
        }
        try {
            return URLEncoder.encode(s, UTF8)
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~")
                    .replace("#", "%23");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    //调用文件选择软件来选择文件 -> 返回文件路径  ---------------start
    public static final int showFileChooser_requestCode = 0x0001; //requestCode状体码

    public static void showFileChooser(Activity pActivity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            pActivity.startActivityForResult(Intent.createChooser(intent, "请选择要上传的文件"), showFileChooser_requestCode);
        } catch (android.content.ActivityNotFoundException ex) {
            Toaster.show("请安装文件管理器");
        }
    }

    public static String showFileChooser_onActivityResult(Activity pActivity, int requestCode, int resultCode, Intent data) {
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
            fileName = path.substring(path.lastIndexOf("/") + 1);
            return path;
        }
        return "";
    }
    //调用文件选择软件来选择文件 ---------------end

    //---------------------------------以下函数已被BaseActivity与BaseFragment实现------------------------------

    /**
     * 串拼接
     *
     * @param strs 可变参数类型
     * @return 拼接后的字符串
     */
    public static String appendStrs(Object... strs) {
        StringBuilder sb = new StringBuilder();
        if (strs != null && strs.length > 0) {
            for (Object str : strs) {
                sb.append(String.valueOf(str));
            }
        }
        return sb.toString();
    }

    /**
     * 串变化 -> 大小&颜色
     *
     * @param start 从0开始计数(包含start)
     * @param end   从1开始计数(包含end)
     */
    public static SpannableString changeStr(String str, int start, int end, int dip, int color) {
        SpannableString spannableString = new SpannableString(str);
        return changeStr(spannableString, start, end, dip, color);
    }

    public static SpannableString changeStr(SpannableString spannableString, int start, int end, int dip, int color) {
        if (spannableString == null) {
            return new SpannableString("");
        }
        if (start >= end) {
            return spannableString;
        }
        if (dip > 0) {
            AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(dip, true);
            spannableString.setSpan(sizeSpan, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        if (color > 0) {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
            spannableString.setSpan(colorSpan, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return spannableString;
    }

    /**
     * 根据手机的分辨率从 dp 的单位转成 px(像素)
     */
    public static float dp2px(Context pContext, float dp) {
        return dp2px(pContext.getResources(), dp);
    }

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    /**
     * 根据手机的分辨率从 px(像素)的单位转成 dp
     */
    public static float px2dp(Context pContext, float px) {
        return px2dp(pContext.getResources(), px);
    }

    public static float px2dp(Resources resources, float px) {
        final float scale = resources.getDisplayMetrics().density;
        return px / scale + 0.5f;
    }

    /**
     * 根据手机的分辨率从 sp 的单位转成 px(像素)
     */
    public static float sp2px(Context pContext, float sp) {
        return sp2px(pContext.getResources(), sp);
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale + 0.5f;
    }

    /**
     * 根据手机的分辨率从 px(像素)的单位转成 sp
     */
    public static float px2sp(Context pContext, float px) {
        return px2sp(pContext.getResources(), px);
    }

    public static float px2sp(Resources resources, float px) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return px / scale + 0.5f;
    }

}
