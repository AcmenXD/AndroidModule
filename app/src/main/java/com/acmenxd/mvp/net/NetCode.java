package com.acmenxd.mvp.net;

import com.acmenxd.retrofit.exception.NetException;
import com.acmenxd.retrofit.exception.NetExceptionFail;
import com.acmenxd.retrofit.exception.NetExceptionSuccess;
import com.acmenxd.retrofit.exception.NetExceptionUnknownCode;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/10 9:50
 * @detail 统一处理服务器响应的code和msg
 */
public final class NetCode {
    /**
     * 默认ToastMsg提示消息
     */
    public static final String TOAST_MSG = "网络繁忙,请稍后再试!";
    /**
     * 数据状态码
     */
    public static final int SUCCESS_DATA = 0; // 数据正常
    public static final int FAIL_DATA = 1; // 数据异常

    /**
     * 统一处理服务器响应的code和msg
     *
     * @param code 服务器返回的code码
     * @param msg  服务器返回的msg消息
     * @return 返回生成的Exception
     */
    public static final synchronized NetException parseNetCode(final int code, final String msg) {
        NetException netException = null;
        switch (code) {
            /**
             * 数据正常处理
             */
            case SUCCESS_DATA:
                netException = new NetExceptionSuccess(code, msg, "请求成功!");
                break;
            /**
             * 数据异常处理
             */
            case FAIL_DATA:
                netException = new NetExceptionFail(code, msg, "请求失败!");
                break;
        }
        /**
         * code无匹配处理
         */
        if (netException == null) {
            netException = new NetExceptionUnknownCode(code, msg, TOAST_MSG);
        }
        return netException;
    }
}
