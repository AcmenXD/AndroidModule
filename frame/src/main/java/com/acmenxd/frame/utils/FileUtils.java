package com.acmenxd.frame.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.acmenxd.frame.basis.FrameApplication;
import com.acmenxd.logger.Logger;

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
 * @date 2016/12/20 18:27
 * @detail File 工具类
 */
public final class FileUtils {
    /**
     * 1KB字节数
     */
    public static final int ONE_KB = 1024;
    /**
     * 1M字节数
     */
    public static final int ONE_MB = 1024 * ONE_KB;
    /**
     * String 路径
     */
    public static String BASE_DIR = FrameApplication.instance().getConfig().BASE_DIR;
    public static String LOG_DIR = FrameApplication.instance().getConfig().LOG_DIR;
    public static String cacheDirPath = BASE_DIR + "Cache/";//缓存路径
    public static String imgCacheDirPath = BASE_DIR + "Image/cache/";//图片缓存路径
    public static String imgSaveDirPath = BASE_DIR + "Image/save/";//图片保存路径
    public static String imgShareDirPath = BASE_DIR + "Image/share/";//图片分享路径 -> 临时
    public static String picSaveDirPath = BASE_DIR + "Picture/save/";//拍照保存路径
    public static String picCutSaveDirPath = BASE_DIR + "/Picture/cut/";//拍照剪裁后保存路径
    public static String apkSaveDirPath = BASE_DIR + "Apk/";//apk保存路径
    /**
     * File 路径 -> 对应上面的String路径
     */
    public static File cacheDir = new File(cacheDirPath);//缓存路径
    public static File imgCacheDir = new File(imgCacheDirPath);//图片缓存路径
    public static File imgSaveDir = new File(imgSaveDirPath);//图片保存路径
    public static File imgShareDir = new File(imgShareDirPath);//分享的图片路径
    public static File picSaveDir = new File(picSaveDirPath);//拍照图片保存路径
    public static File picCutSaveDir = new File(picCutSaveDirPath);//拍照图片剪裁后保存路径
    public static File apkSaveDir = new File(apkSaveDirPath);//apk保存路径

    /**
     * 初始化 -> BaseApplication中调用
     */
    public static void init() {
        /**
         * 创建目录
         */
        //if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
        try {
            createDirectorys(cacheDir);
            createDirectorys(imgCacheDir);
            createDirectorys(imgSaveDir);
            createDirectorys(imgShareDir);
            createDirectorys(picSaveDir);
            createDirectorys(picCutSaveDir);
            createDirectorys(apkSaveDir);
            createDirectorys(new File(LOG_DIR)); //log保存路径
        } catch (IOException pE) {
            pE.printStackTrace();
        }
        //}
    }

    /**
     * 创建临时文件
     *
     * @param dir     文件目录
     * @param suffix  文件前缀
     * @param postfix 文件后缀
     * @return
     */
    public static File createTempFile(@NonNull File dir, @NonNull String suffix, @NonNull String postfix) {
        try {
            return File.createTempFile(suffix, postfix, dir);
        } catch (IOException pE) {
            Logger.e(pE);
        }
        return null;
    }

    /**
     * 判断 文件&文件夹 是否存在
     */
    public static boolean isExists(@NonNull File path) {
        if (path.exists()) {
            return true;
        }
        return false;
    }

    public static boolean isExists(@NonNull String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return isExists(new File(path));
    }

    /**
     * 列出目录中的所有目录&文件
     */
    public static File[] getFiles(@NonNull File dir) throws IOException {
        if (dir == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (!dir.exists()) {
            throw new NullPointerException("Source '" + dir.getAbsolutePath() + "' does not exist");
        }
        if (!dir.isDirectory()) {
            throw new IOException("Source '" + dir.getAbsolutePath() + "' is not a directory");
        }
        File[] files = dir.listFiles();
        if (files == null) {
            throw new IOException("Failed to list contents of " + dir);
        }
        return files;
    }

    /**
     * 创建目录 -> 单层&多层 目录都可以
     *
     * @return 创建成功返回true
     */
    public static boolean createDirectorys(@NonNull File dir) throws IOException {
        if (!dir.exists()) {
            dir.mkdirs();
            if (dir.exists()) {
                return true;
            } else {
                throw new IOException("Source '" + dir.getAbsolutePath() + "' can't create");
            }
        }
        return false;
    }

    public static boolean createDirectorys(@NonNull String dir) throws IOException {
        if (TextUtils.isEmpty(dir)) {
            throw new NullPointerException("Source must not be null");
        }
        return createDirectorys(new File(dir));
    }

    /**
     * 创建文件 -> 父目录不存在会自动创建 & 如文件存在的话不会删除重新创建
     *
     * @return 创建成功返回true
     */
    public static boolean createFile(@NonNull File targetFile) throws IOException {
        return createFile(targetFile, false);
    }

    public static boolean createFile(@NonNull String targetPath) throws IOException {
        if (TextUtils.isEmpty(targetPath)) {
            throw new NullPointerException("Source must not be null");
        }
        return createFile(new File(targetPath), false);
    }

    /**
     * 创建文件 -> 父目录不存在会自动创建 & 如文件存在的话会删除重新创建
     *
     * @return 创建成功返回true
     */
    public static boolean createFileWithDelete(@NonNull File targetFile) throws IOException {
        return createFile(targetFile, true);
    }

    public static boolean createFileWithDelete(@NonNull String targetFile) throws IOException {
        if (TextUtils.isEmpty(targetFile)) {
            throw new NullPointerException("Source must not be null");
        }
        return createFile(new File(targetFile), true);
    }

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

    public static boolean copyFile(@NonNull String srcFile, @NonNull String targetFile, boolean isDeleteMoveFile) throws IOException {
        if (TextUtils.isEmpty(srcFile)) {
            throw new NullPointerException("Source must not be null");
        }
        if (TextUtils.isEmpty(targetFile)) {
            throw new NullPointerException("targetFile must not be null");
        }
        return copyFile(new File(srcFile), new File(targetFile), isDeleteMoveFile);
    }

    /**
     * 拷贝目录下所有文件夹及文件 -> 不会删除源目录及文件
     * * 操作属耗时任务,请在异步下调用
     *
     * @param srcDir
     * @param targetDir
     * @param isDeleteMoveFile 如果 目标文件 存在,判断是否删除并 -> 重新创建
     * @return
     * @throws IOException
     */
    public static boolean copyDir(@NonNull File srcDir, @NonNull File targetDir, boolean isDeleteMoveFile) throws IOException {
        if (srcDir.isFile()) {
            throw new RuntimeException("源目录不能为文件格式!");
        }
        boolean result = true;
        File[] files = getFiles(srcDir);
        for (int i = 0, len = files.length; i < len; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                result = copyDir(file, new File(targetDir, file.getName()), isDeleteMoveFile);
            } else {
                result = copyFile(file, new File(targetDir, file.getName()), isDeleteMoveFile);
            }
            if (result == false) {
                throw new RuntimeException("拷贝文件错误!");
            }
        }
        return result;
    }

    public static boolean copyDir(@NonNull String srcDir, @NonNull String targetDir, boolean isDeleteMoveFile) throws IOException {
        if (TextUtils.isEmpty(srcDir)) {
            throw new NullPointerException("Source must not be null");
        }
        if (TextUtils.isEmpty(targetDir)) {
            throw new NullPointerException("targetDir must not be null");
        }
        return copyDir(new File(srcDir), new File(targetDir), isDeleteMoveFile);
    }

    /**
     * 移动一个文件 -> 会删除源文件
     * * 操作文件属耗时任务,请在异步下调用
     *
     * @param srcFile
     * @param targetFile
     * @param isDeleteMoveFile 如果 目标文件 存在,判断是否删除并 -> 重新创建
     * @return
     * @throws IOException
     */
    public static boolean moveFile(@NonNull File srcFile, @NonNull File targetFile, boolean isDeleteMoveFile) throws IOException {
        return moveFile(srcFile, targetFile, true, isDeleteMoveFile, true);
    }

    public static boolean moveFile(@NonNull String srcFile, @NonNull String targetFile, boolean isDeleteMoveFile) throws IOException {
        if (TextUtils.isEmpty(srcFile)) {
            throw new NullPointerException("Source must not be null");
        }
        if (TextUtils.isEmpty(targetFile)) {
            throw new NullPointerException("targetFile must not be null");
        }
        return moveFile(new File(srcFile), new File(targetFile), true, isDeleteMoveFile, true);
    }

    /**
     * 删除目录下所有文件夹及文件
     * * 操作属耗时任务,请在异步下调用
     *
     * @param isDeleteSrcDir 是否删除目录本身
     */
    public static boolean deleteDir(@NonNull File srcDir, boolean isDeleteSrcDir) {
        if (srcDir.isFile()) {
            throw new RuntimeException("目录不能为文件格式!");
        }
        boolean result = true;
        if (srcDir.isDirectory()) {
            String[] childs = srcDir.list();
            //递归删除目录中的子目录下
            for (int i = 0, len = childs.length; i < len; i++) {
                result = deleteDir(new File(srcDir, childs[i]), true);
                if (result == false) {
                    throw new RuntimeException("删除文件错误!");
                }
            }
        }
        if (isDeleteSrcDir) {
            return srcDir.delete();
        }
        return result;
    }

    public static boolean deleteDir(@NonNull String srcDir, boolean isDeleteSrcDir) throws IOException {
        if (TextUtils.isEmpty(srcDir)) {
            throw new NullPointerException("Source must not be null");
        }
        return deleteDir(new File(srcDir), isDeleteSrcDir);
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
            IOUtils.closeQuietly(bIn, bOut);
        }
        // 变更文件修改日期
        if (preserveFileDate) {
            outFile.setLastModified(inFile.lastModified());
        }
        return result;
    }

}
