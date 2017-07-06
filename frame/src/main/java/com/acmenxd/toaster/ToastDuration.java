package com.acmenxd.toaster;

import android.support.annotation.IntRange;
import android.widget.Toast;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/22 13:27
 * @detail 设置Toast显示时长类 -> 作用ToastDuration
 */
public final class ToastDuration {
    /**
     * 短时间显示 -> 对应Toast.LENGTH_SHORT
     */
    public static final ToastDuration SHORT = ToastDuration.mk(Toast.LENGTH_SHORT);
    /**
     * 长时间显示 -> 对应Toast.LENGTH_LONG
     */
    public static final ToastDuration LONG = ToastDuration.mk(Toast.LENGTH_LONG);

    /**
     * 默认显示时长 -> BuildConfig里配置
     */
    public static final ToastDuration Default() {
        return Toaster.TOAST_DURATION;
    }

    /**
     * 创建一个Toast显示时长
     *
     * @param pDuration 时长(毫秒)
     * @return
     */
    public static ToastDuration mk(@IntRange(from = 0) int pDuration) {
        return new ToastDuration(pDuration);
    }

    // 初始显示时长
    private int mDuration = Toast.LENGTH_SHORT;

    private ToastDuration(@IntRange(from = 0) int pDuration) {
        mDuration = pDuration;
    }

    /**
     * 获取时长
     */
    public int gDuration() {
        return mDuration;
    }
}
