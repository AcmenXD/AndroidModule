package com.acmenxd.retrofit.callback;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.acmenxd.logger.Logger;
import com.acmenxd.retrofit.BaseEntity;
import com.acmenxd.retrofit.NetCodeParse;
import com.acmenxd.retrofit.NetEntity;
import com.acmenxd.retrofit.NetError;
import com.acmenxd.retrofit.NetManager;
import com.acmenxd.retrofit.exception.NetException;
import com.acmenxd.retrofit.exception.NetExceptionFail;
import com.acmenxd.retrofit.exception.NetExceptionSuccess;
import com.acmenxd.retrofit.exception.NetExceptionUnknownCode;
import com.acmenxd.retrofit.exception.NetNoDataBodyException;
import com.acmenxd.retrofit.exception.NetNoDataTypeException;

import okhttp3.ResponseBody;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/12 14:06
 * @detail Rx网络请求回调
 */
public abstract class NetSubscriber<T> {
    /**
     * 是否自行处理返回的数据 或 非已定义的NetEntity和Bitmap 类型 需设置此变量为true
     * * 如为true, 则onResponse会直接回调succeed, 忽略对错误的处理
     * * 所以在succeed函数中需手动调用NetCode.parseNetCode处理服务器返回的异常
     */
    private boolean isAlreadyOperationData = false;

    public NetSubscriber() {

    }

    public NetSubscriber(boolean isAlreadyOperationData) {
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
    public void failed(@NonNull NetException pE) {
        Logger.w(NetManager.INSTANCE.net_log_tag, "net failed : code -> " + pE.getCode() + " , msg -> " + pE.getMsg());
    }

    /**
     * 请求完成回调
     * * 非必须重写,可根据需要自行实现
     */
    public void finished() {
    }

    /*public void onProgress(long allBytes,long nowBytes){
        NetLogPrint.print("loading : allBytes -> " + allBytes + " , nowBytes -> " + nowBytes);
    }*/

    // ---------------------------- 统一处理函数,无需关心 ------------------------
    public final void onNext(@NonNull final T data) {
        if (isAlreadyOperationData) {
            success(data);
            return;
        }
        /**
         * 数据处理部分
         */
        if (data != null) {
            // 返回类型NetEntity
            if (data instanceof BaseEntity || data instanceof NetEntity) {
                // 服务器响应的code和msg统一交给NetCode处理
                NetCodeParse.parseNetCode parseNetCode = NetManager.INSTANCE.parseNetCode;
                if (parseNetCode == null) {
                    success(data);
                } else {
                    NetException exception = parseNetCode.parse(((BaseEntity) data).getCode(), ((BaseEntity) data).getMsg());
                    NetCodeParse.parseNetException(exception, new NetCodeParse.NetCodeCallback() {
                        @Override
                        public void successData(NetExceptionSuccess pE) {
                            success(data);
                        }

                        @Override
                        public void errorData(NetExceptionFail pE) {
                            onError(pE);
                        }

                        @Override
                        public void unknownCode(NetExceptionUnknownCode pE) {
                            onError(pE);
                        }
                    });
                }
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
                onError(new NetNoDataTypeException("net error -> no type error"));
            }
        }
        // data空,统一处理为解析异常:NetNoDataBodyException
        else {
            onError(new NetNoDataBodyException("net error -> response body null error"));
        }
    }

    public final void onError(@NonNull Throwable pE) {
        fail(NetError.parseException(pE));
    }

    public final void onCompleted() {
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
    private final void fail(@NonNull NetException pE) {
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
