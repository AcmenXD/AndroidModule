package com.acmenxd.frame.basis.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/13 15:29
 * @detail 支持Start的接口类
 */
public interface IFrameStart {
    /**
     * * Activity中:获取Intent中数据参数
     * * Fragment中:获取Activity的Intent中数据参数
     */
    Bundle getBundle();

    /**
     * 为了统一启动方式,所以暂作过时处理
     * * 尽量不要使用此函数启动Activity
     */
    @Deprecated
    void startActivity(@NonNull Intent intent);

    /**
     * 为了统一启动方式,所以暂作过时处理
     * * 尽量不要使用此函数启动Activity
     */
    @Deprecated
    void startActivity(@NonNull Intent intent, @NonNull Bundle options);

    /**
     * 启动Activity
     */
    void startActivity(@NonNull Class cls);

    /**
     * 启动Activity
     */
    void startActivity(@NonNull Class cls, @NonNull Bundle bundle);

    /**
     * 启动Activity
     */
    void startActivity(@NonNull Class cls, @NonNull Bundle bundle, int flags);

    /**
     * 为了统一启动方式,所以暂作过时处理
     * * 尽量不要使用此函数启动Activity
     */
    @Deprecated
    ComponentName startService(Intent intent);

    /**
     * 启动Service
     */
    ComponentName startService(@NonNull Class cls);

    /**
     * 启动Service
     */
    ComponentName startService(@NonNull Class cls, @NonNull Bundle bundle);
}
