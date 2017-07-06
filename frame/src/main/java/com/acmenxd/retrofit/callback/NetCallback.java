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
import com.acmenxd.retrofit.exception.NetResponseException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/3 15:30
 * @detail 普通方式的网络请求回调
 */
public abstract class NetCallback<T> {
    /**
     * 是否自行处理返回的数据 或 非已定义的NetEntity和Bitmap 类型 需设置此变量为true
     * * 如为true, 则onResponse会直接回调succeed, 忽略对错误的处理
     * * 所以在succeed函数中需手动调用NetCode.parseNetCode处理服务器返回的异常
     */
    private boolean isAlreadyOperationData = false;

    public NetCallback() {

    }

    public NetCallback(boolean isAlreadyOperationData) {
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
    public void succeed(@NonNull Response pResponse, @NonNull T pData) {
    }

    /**
     * 请求失败回调
     * * 非必须重写,可根据需要自行实现
     */
    public void succeed(@NonNull Call pCall, @NonNull Response pResponse, @NonNull T pData) {
    }

    /**
     * 请求失败回调
     * * 非必须重写,可根据需要自行实现
     */
    public void failed(@NonNull NetException pE) {
        Logger.w(NetManager.INSTANCE.net_log_tag, "net failed : code -> " + pE.getCode() + " , msg -> " + pE.getMsg());
    }

    /*public void onProgress(long allBytes,long nowBytes){
        NetLogPrint.print("loading : allBytes -> " + allBytes + " , nowBytes -> " + nowBytes);
    }*/

    // ---------------------------- 统一处理函数,无需关心 ------------------------
    public final void onResponse(@NonNull final Call<T> call, @NonNull final Response<T> response) {
        // 服务器合理响应
        if (response.raw().code() == NetError.SUCCESS_RESPONSE) {
            final T data = response.body();
            if (isAlreadyOperationData) {
                success(call, response, data);
                return;
            }
            /**
             * 数据处理部分
             */
            if (data != null) {
                // 返回类型BaseEntity
                if (data instanceof BaseEntity || data instanceof NetEntity) {
                    // 服务器响应的code和msg统一交给NetCode处理
                    NetCodeParse.parseNetCode parseNetCode = NetManager.INSTANCE.parseNetCode;
                    if (parseNetCode == null) {
                        success(call, response, data);
                    } else {
                        NetException exception = parseNetCode.parse(((BaseEntity) data).getCode(), ((BaseEntity) data).getMsg());
                        NetCodeParse.parseNetException(exception, new NetCodeParse.NetCodeCallback() {
                            @Override
                            public void successData(NetExceptionSuccess pE) {
                                success(call, response, data);
                            }

                            @Override
                            public void errorData(NetExceptionFail pE) {
                                onFailure(call, pE);
                            }

                            @Override
                            public void unknownCode(NetExceptionUnknownCode pE) {
                                onFailure(call, pE);
                            }
                        });
                    }
                }
                // 返回类型ResponseBody
                else if (data instanceof ResponseBody) {
                    success(call, response, data);
                }
                // 返回类型Bitmap
                else if (data instanceof Bitmap) {
                    success(call, response, data);
                }
                // 返回类型无定义,统一处理为NetNoDataTypeException异常
                else {
                    onFailure(call, new NetNoDataTypeException("net error -> no type error"));
                }
            }
            // data空,统一处理为解析异常:NetNoDataBodyException
            else {
                onFailure(call, new NetNoDataBodyException("net error -> response body null error"));
            }
        }
        // 服务器或请求过程没有正常响应,统一处理为响应异常:NetResponseException
        else {
            onFailure(call, new NetResponseException("net response error : " + response.raw().toString()));
        }
    }

    public final void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        fail(NetError.parseException(t));
    }

    /**
     * 请求成功
     * * 每个回调方法->成功的 都会调用
     */
    private final void success(@NonNull Call pCall, @NonNull Response pResponse, @NonNull T pData) {
        succeed(pData);
        succeed(pResponse, pData);
        succeed(pCall, pResponse, pData);
    }

    /**
     * 请求失败
     * * 每个回调方法->失败的 都会调用
     */
    private final void fail(@NonNull NetException pE) {
        failed(pE);
    }

}
