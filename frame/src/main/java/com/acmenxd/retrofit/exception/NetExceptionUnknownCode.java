package com.acmenxd.retrofit.exception;

import android.support.annotation.NonNull;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/3 17:01
 * @detail Net数据异常 -> code无匹配
 */
public final class NetExceptionUnknownCode extends NetException {
    public NetExceptionUnknownCode(int pCode, @NonNull String pMsg, @NonNull String pToastMsg) {
        super(pCode, pMsg, pToastMsg);
    }
}