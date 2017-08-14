package com.acmenxd.retrofit;

import android.support.annotation.NonNull;

import com.acmenxd.retrofit.exception.HttpException;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/29 14:28
 * @detail 所有请求返回时, 统一回调
 */
public abstract class HttpResultCallback {
    /**
     * 所有请求成功,下发前统一回调
     *
     * @return 是否拦截下发
     */
    public abstract boolean success(int code, @NonNull String msg);

    /**
     * 所有请求错误,下发前统一回调
     *
     * @return 是否拦截下发
     */
    public abstract boolean fail(HttpException exception);
}
