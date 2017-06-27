package com.acmenxd.frame.basis;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.acmenxd.retrofit.callback.NetCallback;
import com.acmenxd.retrofit.callback.NetSubscriber;

import retrofit2.Callback;
import rx.Subscriber;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/6/26 11:19
 * @detail 支持Retrofit的接口类
 */
public interface INet {
    /**
     * 获取IAllRequest实例
     */
    <T> T request();

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
    <T> T newRequest(@IntRange(from = 0) int connectTimeout, @IntRange(from = 0) int readTimeout, @IntRange(from = 0) int writeTimeout, @NonNull Class<T> pIRequest);

    /**
     * 统一处理因异步导致的 Activity|Fragment销毁时发生NullPointerException问题
     *
     * @param pCallback Net请求回调
     * @param setting   数组下标 ->
     *                  0.是否显示LoadingDialog(默认false)
     *                  1.isCancelable(是否可以通过点击Back键取消)(默认true)
     *                  2.isCanceledOnTouchOutside(是否在点击Dialog外部时取消Dialog)(默认false)
     */
    <T> Callback<T> newCallback(@NonNull final NetCallback<T> pCallback, final boolean... setting);

    /**
     * 统一处理因异步导致的 Activity|Fragment销毁时发生NullPointerException问题
     *
     * @param pSubscriber Net请求回调
     * @param setting     数组下标 ->
     *                    0.是否显示LoadingDialog(默认false)
     *                    1.isCancelable(是否可以通过点击Back键取消)(默认true)
     *                    2.isCanceledOnTouchOutside(是否在点击Dialog外部时取消Dialog)(默认false)
     */
    <T> Subscriber<T> newSubscriber(@NonNull final NetSubscriber<T> pSubscriber, final boolean... setting);

}
