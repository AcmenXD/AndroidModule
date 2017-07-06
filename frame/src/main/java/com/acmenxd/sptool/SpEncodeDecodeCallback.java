package com.acmenxd.sptool;

import android.support.annotation.NonNull;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/28 17:39
 * @detail sp加密解密回调
 */
public interface SpEncodeDecodeCallback {
    /**
     * 加密
     *
     * @param pStr 原始数据
     * @return 加密后数据
     */
    String encode(@NonNull String pStr);

    /**
     * 解密
     *
     * @param pStr 加密数据
     * @return 解密后数据
     */
    String decode(@NonNull String pStr);
}
