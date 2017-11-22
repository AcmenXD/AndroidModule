package com.acmenxd.logger;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:36
 * @detail 输出日志Json
 */
public final class JsonLog {
    private static final int JSON_INDENT = 4; //缩进

    public static void printJson(@NonNull LogTag tag, @NonNull String headString, @NonNull String msg) {
        String message;
        try {
            String str = checkStartChar(msg);
            if (str.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(JSON_INDENT);
                message = BaseLog.LINE_SEPARATOR + message;
            } else if (str.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(JSON_INDENT);
                message = BaseLog.LINE_SEPARATOR + message;
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }
        message = headString + BaseLog.LINE_SEPARATOR + message;
        BaseLog.printLog(LogType.JSON, tag, message);
    }

    private static String checkStartChar(@NonNull String msg) {
        if (msg.startsWith(BaseLog.LINE_SEPARATOR)) {
            return checkStartChar(msg.substring(1, msg.length()));
        }
        return msg;
    }
}