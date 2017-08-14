package com.acmenxd.retrofit.callback;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.acmenxd.retrofit.HttpEntity;
import com.acmenxd.retrofit.HttpError;
import com.acmenxd.retrofit.HttpGenericityEntity;
import com.acmenxd.retrofit.HttpManager;
import com.acmenxd.retrofit.HttpResultCallback;
import com.acmenxd.retrofit.exception.HttpException;
import com.acmenxd.retrofit.exception.HttpNoDataBodyException;
import com.acmenxd.retrofit.exception.HttpNoDataTypeException;

import java.io.Serializable;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/12 14:06
 * @detail Rx网络请求回调
 */
public abstract class HttpSubscriber<T> extends Subscriber<T> implements IHttpProgress {
    /**
     * 是否自行处理返回的数据 或 非已定义的NetEntity和Bitmap 类型 需设置此变量为true
     * * 如为true, 则onResponse会直接回调succeed, 忽略对错误的处理
     * * 所以在succeed函数中需手动调用NetCode.parseNetCode处理服务器返回的异常
     */
    private boolean isAlreadyOperationData = false;

    public HttpSubscriber() {

    }

    public HttpSubscriber(boolean isAlreadyOperationData) {
        this.isAlreadyOperationData = isAlreadyOperationData;
    }
    // ---------------------------- 重写函数 ------------------------

    /**
     * 请求成功回调
     * * 非必须重写,可根据需要自行实现
     */
    public void succeed(@NonNull T pData) {
    }

    /**
     * 请求失败回调
     * * 非必须重写,可根据需要自行实现
     */
    public void failed(@NonNull HttpException pE) {
    }

    /**
     * 请求完成回调
     * * 非必须重写,可根据需要自行实现
     */
    public void finished() {
    }

    /**
     * 进度回调
     *
     * @param isDone   是否完成
     * @param total    总字节数
     * @param progress 已经下载或上传字节数
     */
    @Override
    public void progress(boolean isDone, long total, long progress) {

    }

    // ---------------------------- 统一处理函数,无需关心 ------------------------
    @Override
    public void onNext(T data) {
        onNext2(data);
    }

    @Override
    public void onError(Throwable pE) {
        onError2(pE);
    }

    @Override
    public void onCompleted() {
        onCompleted2();
    }

    public final void onNext2(@NonNull final T data) {
        if (isAlreadyOperationData) {
            success(data);
            return;
        }
        /**
         * 数据处理部分
         */
        if (data != null) {
            // 返回类型NetEntity
            if (data instanceof HttpEntity || data instanceof HttpGenericityEntity) { // 所有请求成功,下发前统一回调
                HttpResultCallback resultCallback = HttpManager.INSTANCE.resultCallback;
                if (resultCallback == null || !resultCallback.success(((HttpEntity) data).getCode(), ((HttpEntity) data).getMsg())) {
                    success(data);
                }
            }
            // 返回类型Serializable
            else if (data instanceof Serializable) {
                success(data);
            }
            // 返回类型ResponseBody
            else if (data instanceof ResponseBody) {
                success(data);
            }
            // 返回类型Bitmap
            else if (data instanceof Bitmap) {
                success(data);
            }
            // 返回类型无定义,统一处理为NetNoDataTypeException异常
            else {
                onError(new HttpNoDataTypeException("http error -> no type error"));
            }
        }
        // data空,统一处理为解析异常:NetNoDataBodyException
        else {
            onError(new HttpNoDataBodyException("http error -> response body null error"));
        }
    }

    public final void onError2(@NonNull Throwable pE) {
        // 解析后的Exception
        HttpException exception = HttpError.parseException(pE);
        // 所有请求失败,下发前统一回调
        HttpResultCallback resultCallback = HttpManager.INSTANCE.resultCallback;
        if (resultCallback == null || !resultCallback.fail(exception)) {
            fail(exception);
        }
    }

    public final void onCompleted2() {
        finish();
    }

    /**
     * 请求成功
     * * 每个回调方法->成功的 都会调用
     */
    private final void success(@NonNull T pData) {
        succeed(pData);
    }

    /**
     * 请求失败
     * * 每个回调方法->失败的 都会调用
     */
    private final void fail(@NonNull HttpException pE) {
        failed(pE);
    }

    /**
     * 请求完成
     * * 每个回调方法->完成的 都会调用
     */
    private final void finish() {
        finished();
    }
}
