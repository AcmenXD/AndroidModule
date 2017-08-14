package com.acmenxd.frame.basis.impl;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.acmenxd.retrofit.callback.IHttpProgress;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/6/26 11:19
 * @detail 支持Retrofit的接口类
 */
public interface IFrameNet {
    /**
     * 根据IRequest类获取Request实例
     */
    <T> T request(@NonNull Class<T> pIRequest);

    /**
     * 创建新的Retrofit实例
     * 根据IRequest类获取Request实例
     */
    <T> T newRequest(@NonNull Class<T> pIRequest);

    /**
     * 创建新的Retrofit实例,并设置超时时间
     * 根据IRequest类获取Request实例
     */
    <T> T newRequest(@NonNull Class<T> pIRequest, @IntRange(from = 0) int connectTimeout, @IntRange(from = 0) int readTimeout, @IntRange(from = 0) int writeTimeout);

    /**
     * 下载Retrofit实例 -> 默认读取超时时间5分钟
     * 根据IRequest类获取Request实例
     */
    <T> T downloadRequest(@NonNull Class<T> pIRequest, @NonNull IHttpProgress pProgress);

    /**
     * 下载Retrofit实例,并设置读取超时时间
     * 根据IRequest类获取Request实例
     */
    <T> T downloadRequest(@NonNull Class<T> pIRequest, @NonNull IHttpProgress pProgress, @IntRange(from = 0) int read_timeout);

    /**
     * 上传Retrofit实例 -> 默认写入超时时间5分钟
     * 根据IRequest类获取Request实例
     */
    <T> T uploadRequest(@NonNull Class<T> pIRequest, @NonNull IHttpProgress pProgress);

    /**
     * 上传Retrofit实例,并设置写入超时时间
     * 根据IRequest类获取Request实例
     */
    <T> T uploadRequest(@NonNull Class<T> pIRequest, @NonNull IHttpProgress pProgress, @IntRange(from = 0) int writeTimeout);
}
