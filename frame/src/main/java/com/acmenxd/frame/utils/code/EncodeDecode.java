package com.acmenxd.frame.utils.code;

import android.util.Base64;

import com.acmenxd.frame.utils.Utils;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/26 17:18
 * @detail 基础的 Base64 + MD5 加解密算法
 */
public class EncodeDecode {
    private static final int SUB_INDEX = 11;

    /**
     * 字符串加密处理
     */
    public static String encode(String data) throws IOException {
        String encodeStr = URLEncoder.encode(data, Utils.UTF8);
        String baseStr = Base64.encodeToString(encodeStr.getBytes(), Base64.DEFAULT);
        String endStr = MD5.md5(baseStr).substring(0, SUB_INDEX);
        return endStr + baseStr;
    }

    /**
     * 字符串解密处理
     */
    public static String decode(String data) throws ClassNotFoundException, IOException {
        String firstStr = data.substring(SUB_INDEX);
        String baseStr = new String(Base64.decode(firstStr, Base64.DEFAULT));
        return URLDecoder.decode(baseStr, Utils.UTF8);
    }
}
