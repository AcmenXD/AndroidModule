package com.acmenxd.sptool;

import android.support.annotation.NonNull;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/27 11:05
 * @detail SP变换的监听事件
 */
public interface SpChangeListener {

    /**
     * 监听回调事件
     *
     * @param pKey
     */
    void onChanged(@NonNull String pKey);
}
