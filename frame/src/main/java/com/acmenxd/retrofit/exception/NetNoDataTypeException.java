package com.acmenxd.retrofit.exception;

import android.support.annotation.NonNull;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/3 17:01
 * @detail Net数据无匹配类型异常
 */
public final class NetNoDataTypeException extends Exception {
    public NetNoDataTypeException(@NonNull String errerStr) {
        super(errerStr);
    }
}
