package com.acmenxd.frame.basis.impl;

import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/7/28 16:23
 * @detail 支持Utils的接口类
 */
public interface IFrameUtils {
    /**
     * 退出应用程序
     */
    void exit();

    /**
     * 字符串是否为空
     */
    boolean isEmpty(@Nullable CharSequence str);

    /**
     * 串拼接
     *
     * @param strs 可变参数类型
     * @return 拼接后的字符串
     */
    String appendStrs(@NonNull Object... strs);

    /**
     * 串变化 -> 大小&颜色
     *
     * @param start 从0开始计数(包含start)
     * @param end   从1开始计数(包含end)
     */
    SpannableString changeStr(@NonNull String str, @IntRange(from = 0) int start, @IntRange(from = 0) int end, @IntRange(from = 0) int dip, @ColorInt int color);

    SpannableString changeStr(@NonNull SpannableString spannableString, @IntRange(from = 0) int start, @IntRange(from = 0) int end, @IntRange(from = 0) int dip, @ColorInt int color);

    /**
     * 根据手机的分辨率从 dp 的单位转成 px(像素)
     */
    float dp2px(@FloatRange(from = 0) float dp);

    /**
     * 根据手机的分辨率从 px(像素)的单位转成 dp
     */
    float px2dp(@FloatRange(from = 0) float px);

    /**
     * 根据手机的分辨率从 sp 的单位转成 px(像素)
     */
    float sp2px(@FloatRange(from = 0) float sp);

    /**
     * 根据手机的分辨率从 px(像素)的单位转成 sp
     */
    float px2sp(@FloatRange(from = 0) float px);
}
