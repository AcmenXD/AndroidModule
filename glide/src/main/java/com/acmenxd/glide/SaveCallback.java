package com.acmenxd.glide;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/4 15:56
 * @detail 保存图片的回调接口
 */
public interface SaveCallback {
    /**
     * 请注意,回调函数是在子线程中执行的
     * @param file
     */
    void succeed(@NonNull final File file);

    /**
     * 请注意,回调函数是在子线程中执行的
     * @param pE
     */
    void failed(@NonNull final Exception pE);
}
