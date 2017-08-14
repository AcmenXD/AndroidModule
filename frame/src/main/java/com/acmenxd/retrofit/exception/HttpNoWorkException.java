package com.acmenxd.retrofit.exception;

import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/3 17:01
 * @detail Net响应异常
 */
public final class HttpNoWorkException extends IOException {
    public HttpNoWorkException(@NonNull String errerStr) {
        super(errerStr);
    }
}
