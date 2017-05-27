package com.acmenxd.frame.configs;

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

    static NetException frameParseNetCode(int code, String msg) {
        if (sParse == null) {
            return new NetExceptionUnknownCode(code, msg, "网络繁忙,请稍后再试!");
        }
        return sParse.parseNetCode(code, msg);
    }

    public static void setNetCode(Parse pParse) {
        sParse = pParse;
    }

    public static abstract class Parse {
        protected abstract NetException parseNetCode(int code, String msg);
    }
}
