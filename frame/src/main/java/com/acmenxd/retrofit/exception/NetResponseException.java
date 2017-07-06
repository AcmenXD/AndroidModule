package com.acmenxd.retrofit.exception;

import android.support.annotation.NonNull;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/3 17:01
 * @detail Net响应异常
 */
public final class NetResponseException extends Exception {
    public NetResponseException(@NonNull String errerStr) {
        super(errerStr);
    }
}
