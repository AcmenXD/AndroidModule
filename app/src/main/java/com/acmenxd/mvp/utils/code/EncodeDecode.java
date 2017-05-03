package com.acmenxd.mvp.utils.code;

import com.acmenxd.logger.Logger;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/26 17:18
 * @detail 基础的 BASE64+MD5 加解密算法
 */
public class EncodeDecode {
    private static final int SUB_INDEX = 11;
    /**
     * 字符串加密处理
     */
    public static String encode(String data) throws IOException {
        String encodeStr = URLEncoder.encode(data);
        String baseStr = Base64.encodeBytes(encodeStr.getBytes());
        String endStr = MD5(baseStr).substring(0, SUB_INDEX);
        return endStr + baseStr;
    }

    /**
     * 字符串解密处理
     */
    public static String decode(String data)
            throws ClassNotFoundException, IOException {
        String firstStr = data.substring(SUB_INDEX);
        String baseStr = new String(Base64.decode(firstStr));
        return URLDecoder.decode(baseStr);
    }

    // MD5加密，32位
    private static String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception pE) {
            Logger.e(pE);
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

  /*  *//**
     * 可逆的加密算法
     *//*
    private static String encryptmd5(String str) {
        char[] a = str.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 'l');
        }
        String s = new String(a);
        return s;
    }*/

}
