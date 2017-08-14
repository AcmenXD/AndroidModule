package com.acmenxd.retrofit.exception;

import android.support.annotation.NonNull;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/3 17:01
 * @detail Net响应异常
 */
public final class HttpResponseException extends Exception {
    // 异常状态码
    private int code;

    public HttpResponseException(int pCode, @NonNull String errerStr) {
        super(errerStr);
        this.code = pCode;
    }

    public int getCode() {
        return code;
    }
}
