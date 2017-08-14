package com.acmenxd.retrofit;

import android.support.annotation.NonNull;

import com.acmenxd.retrofit.exception.HttpException;
import com.acmenxd.retrofit.exception.HttpExceptionFail;
import com.acmenxd.retrofit.exception.HttpExceptionSuccess;
import com.acmenxd.retrofit.exception.HttpExceptionUnknownCode;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/29 14:28
 * @detail 处理NetCode工具类
 */
public final class HttpCodeParse {
    /**
     * NetCode已经转换为Exception后的处理回调
     */
    public static abstract class NetCodeCallback {
        /**
         * 回调 successData 表示数据正常处理
         */
        public abstract void successData(@NonNull HttpExceptionSuccess pE);

        /**
         * 回调 errorData 表示数据异常处理
         */
        public abstract void errorData(@NonNull HttpExceptionFail pE);

        /**
         * 回调 unknownCode 表示code无匹配处理
         */
        public abstract void unknownCode(@NonNull HttpExceptionUnknownCode pE);
    }

    /**
     * 开始解析NetCode回调
     */
    public static abstract class parseNetCode {
        public abstract HttpException parse(String url, int code, @NonNull String msg);
    }

    /**
     * 回调 successData 表示数据正常处理
     * 回调 errorData 表示数据异常处理
     * 回调 unknownCode 表示code无匹配处理
     */
    public static final HttpException parseNetException(@NonNull final HttpException pHttpException, @NonNull final NetCodeCallback pCallback) {
        if (pHttpException instanceof HttpExceptionSuccess) {
            pCallback.successData((HttpExceptionSuccess) pHttpException);
        } else if (pHttpException instanceof HttpExceptionFail) {
            pCallback.errorData((HttpExceptionFail) pHttpException);
        } else if (pHttpException instanceof HttpExceptionUnknownCode) {
            pCallback.unknownCode((HttpExceptionUnknownCode) pHttpException);
        }
        return pHttpException;
    }
}
