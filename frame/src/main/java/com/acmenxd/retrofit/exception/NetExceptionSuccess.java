package com.acmenxd.retrofit.exception;

import android.support.annotation.NonNull;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/10 10:42
 * @detail Net数据正常返回
 */
public class NetExceptionSuccess extends NetException {
    public NetExceptionSuccess(int pCode, @NonNull String pMsg, @NonNull String pToastMsg) {
        super(pCode, pMsg, pToastMsg);
    }
}
