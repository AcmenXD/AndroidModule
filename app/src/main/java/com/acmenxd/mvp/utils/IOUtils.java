package com.acmenxd.mvp.utils;

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
public class IOUtils {

    /**
     * 关闭流对象(多个 - 可变参数)
     */
    public static void closeQuietly(final Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (final Closeable closeable : closeables) {
            closeQuietly(closeable);
        }
    }

    /**
     * 关闭流对象(单个)
     */
    public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException pE) {
            Logger.e(pE);
        }
    }
}
