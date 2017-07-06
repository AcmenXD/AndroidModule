package com.acmenxd.retrofit.exception;

import android.support.annotation.NonNull;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/3 17:01
 * @detail Net数据解析异常
 */
public final class NetNoDataBodyException extends Exception {
    public NetNoDataBodyException(@NonNull String errerStr) {
        super(errerStr);
    }
}
