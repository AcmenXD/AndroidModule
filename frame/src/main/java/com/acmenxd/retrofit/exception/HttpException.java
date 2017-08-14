package com.acmenxd.retrofit.exception;

import android.support.annotation.NonNull;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/3 17:01
 * @detail 网络总异常类
 */
public class HttpException extends Exception {

    // 异常状态码
    private int code;
    // 异常消息
    private String msg;

    public HttpException(@NonNull Throwable pThrowable, int pCode, @NonNull String pMsg) {
        super(pThrowable);
        this.code = pCode;
        this.msg = pMsg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
