package com.acmenxd.glide;

import android.support.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2018/5/22 17:48
 * @detail Glide工具类
 */
public class GlideUtils {
    /**
     * 1KB字节数
     */
    public static final int ONE_KB = 1024;
    /**
     * 1M字节数
     */
    public static final int ONE_MB = 1024 * ONE_KB;

    /**
     * 拷贝一个文件 -> 不会删除源文件
     * * 操作属耗时任务,请在异步下调用
     *
     * @param srcFile
     * @param targetFile
     * @param isDeleteMoveFile 如果 目标文件 存在,判断是否删除并 -> 重新创建
     * @return
     * @throws IOException
     */
    public static boolean copyFile(@NonNull File srcFile, @NonNull File targetFile, boolean isDeleteMoveFile) throws IOException {
        return moveFile(srcFile, targetFile, false, isDeleteMoveFile, true);
    }

    /**
     * 移动一个文件 -> 源文件会删除
     *
     * @param srcFile          源文件
     * @param targetFile       目标文件
     * @param isDeleteSrcFile  判断是否删除源文件
     * @param isDeleteMoveFile 如果 目标文件 存在,判断是否删除并 -> 重新创建
     * @param preserveFileDate 是否保存文件日期
     * @throws IOException
     */
    private static boolean moveFile(@NonNull File srcFile, @NonNull File targetFile, boolean isDeleteSrcFile, boolean isDeleteMoveFile, boolean preserveFileDate) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (targetFile == null) {
            throw new NullPointerException("targetFile must not be null");
        }
        if (!srcFile.exists()) {
            throw new IOException("Source '" + srcFile + "' does not exist");
        }
        if (targetFile.exists()) {
            if (isDeleteMoveFile) {
                createFileWithDelete(targetFile);
            } else {
                throw new IOException("targetFile '" + targetFile + "' already exists");
            }
        } else {
            createFile(targetFile);
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' is a directory");
        }
        if (targetFile.isDirectory()) {
            throw new IOException("targetFile '" + targetFile + "' is a directory");
        }
        // 拷贝文件
        boolean result = doCopyFile(srcFile, targetFile, preserveFileDate);
        if (isDeleteSrcFile && srcFile.exists()) {
            // 删除源文件
            srcFile.delete();
        }
        return result;
    }

    /**
     * 创建文件 -> 父目录不存在会自动创建 & 如文件存在的话会删除重新创建
     *
     * @return 创建成功返回true
     */
    public static boolean createFileWithDelete(@NonNull File targetFile) throws IOException {
        return createFile(targetFile, true);
    }

    /**
     * 创建文件 -> 父目录不存在会自动创建 & 如文件存在的话不会删除重新创建
     *
     * @return 创建成功返回true
     */
    public static boolean createFile(@NonNull File targetFile) throws IOException {
        return createFile(targetFile, false);
    }

    /**
     * 创建文件
     *
     * @param targetFile 路径
     * @param isDelete   如果文件存在,是否删除重新创建
     * @return
     */
    private static boolean createFile(@NonNull File targetFile, boolean isDelete) throws IOException {
        File parentFile = targetFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
            if (!parentFile.exists()) {
                throw new IOException("Source '" + parentFile.getAbsolutePath() + "' can't create");
            }
        }
        if (isDelete) {
            if (targetFile.exists()) {
                targetFile.delete();
            }
        }
        if (!targetFile.exists()) {
            try {
                targetFile.createNewFile();
            } catch (IOException pE) {
                throw new IOException("Source '" + targetFile.getAbsolutePath() + "' create fail");
            }
            if (targetFile.exists()) {
                return true;
            } else {
                throw new IOException("Source '" + targetFile.getAbsolutePath() + "' can't create");
            }
        }
        return false;
    }

    /**
     * 拷贝一个文件
     *
     * @param inFile
     * @param outFile
     * @param preserveFileDate 是否保存文件日期
     */
    private static boolean doCopyFile(@NonNull File inFile, @NonNull File outFile, boolean preserveFileDate) throws IOException {
        boolean result = false;
        BufferedInputStream bIn = null;
        BufferedOutputStream bOut = null;
        try {
            bIn = new BufferedInputStream(new FileInputStream(inFile));
            bOut = new BufferedOutputStream(new FileOutputStream(outFile));
            int index = 0;
            byte[] buffer = new byte[ONE_MB];
            while ((index = bIn.read(buffer)) != -1) {
                bOut.write(buffer, 0, index);
            }
            bOut.flush();
            result = true;
        } finally {
            // 关闭文件流
            bIn.close();
            bOut.close();
        }
        // 变更文件修改日期
        if (preserveFileDate) {
            outFile.setLastModified(inFile.lastModified());
        }
        return result;
    }

}
