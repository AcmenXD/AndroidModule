package com.acmenxd.logger;

import android.support.annotation.NonNull;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:36
 * @detail 输出日志Xml
 */
public final class XmlLog {
    public static void printXml(@NonNull LogTag tag, @NonNull String headString, @NonNull String xml) {
        String message;
        if (xml == null) {
            message = headString + BaseLog.LINE_SEPARATOR + " Log with null object";
        } else {
            message = headString + BaseLog.LINE_SEPARATOR + xml;
        }
        BaseLog.printLog(LogType.XML, tag, message);
    }
}
