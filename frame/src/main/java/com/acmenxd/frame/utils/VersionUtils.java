package com.acmenxd.frame.utils;

import android.support.annotation.NonNull;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/15 12:03
 * @detail 版本号工具类
 */
public final class VersionUtils {
    /**
     * 版本号对比
     *
     * @param oldVersion
     * @param newVersion
     * @return error : 返回-2 既传入版本号格式有误
     * oldVersion > newVersion  return -1
     * oldVersion = newVersion  return 0
     * oldVersion < newVersion  return 1
     */
    public static int compareVersions(@NonNull String oldVersion, @NonNull String newVersion) {
        //返回结果: -2 ,-1 ,0 ,1
        int result = 0;
        String matchStr = "[0-9]+(\\.[0-9]+)*";
        oldVersion = oldVersion.trim();
        newVersion = newVersion.trim();
        //非版本号格式,返回error
        if (!oldVersion.matches(matchStr) || !newVersion.matches(matchStr)) {
            return -2;
        }
        String[] oldVs = oldVersion.split("\\.");
        String[] newVs = newVersion.split("\\.");
        int oldLen = oldVs.length;
        int newLen = newVs.length;
        int len = oldLen > newLen ? oldLen : newLen;
        for (int i = 0; i < len; i++) {
            if (i < oldLen && i < newLen) {
                int o = Integer.parseInt(oldVs[i]);
                int n = Integer.parseInt(newVs[i]);
                if (o > n) {
                    result = -1;
                    break;
                } else if (o < n) {
                    result = 1;
                    break;
                }
            } else {
                if (oldLen > newLen) {
                    for (int j = i; j < oldLen; j++) {
                        if (Integer.parseInt(oldVs[j]) > 0) {
                            result = -1;
                        }
                    }
                    break;
                } else if (oldLen < newLen) {
                    for (int j = i; j < newLen; j++) {
                        if (Integer.parseInt(newVs[j]) > 0) {
                            result = 1;
                        }
                    }
                    break;
                }
            }
        }
        return result;
    }
}
