package com.acmenxd.frame.utils;

import android.support.annotation.NonNull;

import com.acmenxd.logger.Logger;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:07
 * @detail IO操作
 */
public final class IOUtils {

    /**
     * 关闭流对象(多个 - 可变参数)
     */
    public static void closeQuietly(@NonNull final Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        try {
            for (final Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        } catch (final IOException pE) {
            Logger.e(pE);
        }
    }
}
