package com.acmenxd.logger;

import android.support.annotation.NonNull;
import android.util.Log;

import com.acmenxd.frame.utils.PinYinUtils;
import com.acmenxd.frame.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:36
 * @detail 顶级Logger类
 */
public abstract class BaseLog {
    // 行分隔
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    // 单行|条输出最大字符数
    private static final int MAX_LENGTH = 3000;

    protected final static void printSub(@NonNull LogType type, @NonNull LogTag tag, @NonNull String msg) {
        switch (type) {
            case V:
                Log.v(tag.gTag(), msg);
                break;
            case D:
                Log.d(tag.gTag(), msg);
                break;
            case I:
                Log.i(tag.gTag(), msg);
                break;
            case W:
                Log.w(tag.gTag(), msg);
                break;
            case E:
                Log.e(tag.gTag(), msg);
                break;
            case A:
                Log.wtf(tag.gTag(), msg);
                break;
            case JSON:
                Log.w(tag.gTag(), msg);
                break;
            case XML:
                Log.w(tag.gTag(), msg);
                break;
            case FILE:
                Log.e(tag.gTag(), msg);
                break;
        }
    }

    protected final static void printLog(@NonNull LogType type, @NonNull LogTag tag, @NonNull String message) {
        List<String> resultList = new ArrayList<>();
        String msgs[] = message.split(LINE_SEPARATOR);
        int maxLength = 0;
        resultList.add("start");
        for (int i = 0; i < msgs.length; i++) {
            String msg = msgs[i].replaceAll("\t", "");
            int index = 0;
            int msgLen = msg.length();
            int countOfSub = msgLen / MAX_LENGTH; //输出条数
            if (countOfSub > 0) {
                countOfSub += msgLen % MAX_LENGTH == 0 ? 0 : 1;
                for (int j = 0; j < countOfSub; j++) {
                    int len = index + MAX_LENGTH;
                    String sub = msg.substring(index, len < msgLen ? len : msgLen);
                    if (!StringUtils.isEmpty(sub)) {
                        resultList.add(sub);
                        int l = PinYinUtils.charNum(sub);
                        maxLength = l > maxLength ? l : maxLength;
                    }
                    index += MAX_LENGTH;
                }
            } else {
                if (!StringUtils.isEmpty(msg)) {
                    resultList.add(msg);
                    int l = PinYinUtils.charNum(msg);
                    maxLength = l > maxLength ? l : maxLength;
                }
            }
        }
        resultList.add("end");

        int blankNum = String.valueOf(resultList.size()).length();
        /**
         * 最大字符数内算一条输出
         */
        StringBuilder sb = new StringBuilder(message.length() >= MAX_LENGTH ? MAX_LENGTH : message.length());
        for (int i = 0, len = resultList.size(); i < len; i++) {
            String msg = resultList.get(i);
            if (i == 0) {
                msg = StringUtils.repeat(" ", blankNum) + "╔" + StringUtils.repeat("=", maxLength + 2) + "╗";
            } else if (i == len - 1) {
                msg = StringUtils.repeat(" ", blankNum) + "╚" + StringUtils.repeat("=", maxLength + 2) + "╝";
            } else {
                if (msg.contains("* Throwable Message Start *") || msg.contains("* Throwable Message End *")) {
                    msg = i + StringUtils.repeat(" ", blankNum - String.valueOf(i).length())
                            + "║ " + msg + StringUtils.repeat("-", maxLength - PinYinUtils.charNum(msg)) + "-║";
                } else {
                    msg = i + StringUtils.repeat(" ", blankNum - String.valueOf(i).length())
                            + "║ " + msg + StringUtils.repeat(" ", maxLength - PinYinUtils.charNum(msg)) + " ║";
                }
            }
            if ((sb.length() > 0 && sb.length() + msg.length() > MAX_LENGTH) || i == 1) {
                printSub(type, tag, sb.toString());
                sb = new StringBuilder(message.length() >= MAX_LENGTH ? MAX_LENGTH : message.length());
            }
            sb.append(msg).append(LINE_SEPARATOR);
        }
        printSub(type, tag, sb.toString());

        /**
         * 每行算一条输出
         */
        /*for (int i = 0, len = resultList.size(); i < len; i++) {
            String msg = resultList.get(i);
            if (i == 0) {
                msg = repeat(" ", blankNum) + "╔" + repeat("=", maxLength + 2) + "╗";
            } else if (i == len - 1) {
                msg = repeat(" ", blankNum) + "╚" + repeat("=", maxLength + 2) + "╝";
            } else {
                if (msg.contains("* Throwable Message Start *") || msg.contains("* Throwable Message End *")) {
                    msg = i + repeat(" ", blankNum - String.valueOf(i).length())
                            + "║ " + msg + repeat("-", maxLength - PinYinUtils.charNum(msg)) + "-║";
                } else {
                    msg = i + repeat(" ", blankNum - String.valueOf(i).length())
                            + "║ " + msg + repeat(" ", maxLength - PinYinUtils.charNum(msg)) + " ║";
                }
            }
            printSub(type, tag, msg);
        }*/
    }
}