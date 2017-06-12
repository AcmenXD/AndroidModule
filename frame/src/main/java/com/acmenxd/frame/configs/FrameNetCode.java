package com.acmenxd.frame.configs;

import android.support.annotation.NonNull;

import com.acmenxd.retrofit.exception.NetException;
import com.acmenxd.retrofit.exception.NetExceptionUnknownCode;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/24 15:03
 * @detail 支持Retrofit框架用于NetCode回调基类
 */
public abstract class FrameNetCode {
    private static Parse sParse;

    protected static final NetException frameParseNetCode(int code, @NonNull String msg) {
        if (sParse == null) {
            return new NetExceptionUnknownCode(code, msg, "网络繁忙,请稍后再试!");
        }
        return sParse.parseNetCode(code, msg);
    }

    protected static final void setNetCode(@NonNull Parse pParse) {
        sParse = pParse;
    }

    public static abstract class Parse {
        protected abstract NetException parseNetCode(int code, @NonNull String msg);
    }
}
