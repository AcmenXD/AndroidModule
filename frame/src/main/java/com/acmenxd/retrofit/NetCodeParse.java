package com.acmenxd.retrofit;

import android.support.annotation.NonNull;

import com.acmenxd.retrofit.exception.NetException;
import com.acmenxd.retrofit.exception.NetExceptionFail;
import com.acmenxd.retrofit.exception.NetExceptionSuccess;
import com.acmenxd.retrofit.exception.NetExceptionUnknownCode;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/29 14:28
 * @detail 处理NetCode工具类
 */
public final class NetCodeParse {
    /**
     * NetCode已经转换为Exception后的处理回调
     */
    public static abstract class NetCodeCallback {
        /**
         * 回调 successData 表示数据正常处理
         */
        public abstract void successData(@NonNull NetExceptionSuccess pE);

        /**
         * 回调 errorData 表示数据异常处理
         */
        public abstract void errorData(@NonNull NetExceptionFail pE);

        /**
         * 回调 unknownCode 表示code无匹配处理
         */
        public abstract void unknownCode(@NonNull NetExceptionUnknownCode pE);
    }

    /**
     * 开始解析NetCode回调
     */
    public static abstract class parseNetCode {
        public abstract NetException parse(int code, @NonNull String msg);
    }

    /**
     * 回调 successData 表示数据正常处理
     * 回调 errorData 表示数据异常处理
     * 回调 unknownCode 表示code无匹配处理
     */
    public static final NetException parseNetException(@NonNull final NetException netException, @NonNull final NetCodeCallback pCallback) {
        if (netException instanceof NetExceptionSuccess) {
            pCallback.successData((NetExceptionSuccess) netException);
        } else if (netException instanceof NetExceptionFail) {
            pCallback.errorData((NetExceptionFail) netException);
        } else if (netException instanceof NetExceptionUnknownCode) {
            pCallback.unknownCode((NetExceptionUnknownCode) netException);
        }
        return netException;
    }
}
