package com.acmenxd.mvp.http;

import android.text.format.Formatter;

import com.acmenxd.frame.basis.FrameApplication;
import com.acmenxd.frame.utils.FileUtils;
import com.acmenxd.frame.utils.IOUtils;
import com.acmenxd.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/21 15:21
 * @detail Net工具类
 */
public class LoadHelper {

    //----------------------------------下载----------------------------

    /**
     * 下载文件保存
     */
    public static boolean saveDownLoadFile(final ResponseBody pResponseBody, final String savePath) {
        return saveDownLoadFile(pResponseBody, new File(savePath));
    }

    /**
     * 下载文件保存
     */
    public static boolean saveDownLoadFile(final ResponseBody pResponseBody, final File saveFile) {
        try {
            // 创建目录
            FileUtils.createFileWithDelete(saveFile);
            // 每次读取大小
            byte[] fileReader = new byte[FileUtils.ONE_MB * 2];
            long fileSize = pResponseBody.contentLength();
            int read = 0;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = pResponseBody.byteStream();
                outputStream = new FileOutputStream(saveFile);
                while ((read = inputStream.read(fileReader)) != -1) {
                    outputStream.write(fileReader, 0, read);
                }
                outputStream.flush();
                Logger.v("下载完成: 保存路径->" + saveFile.getAbsolutePath()
                        + "  总大小->" + Formatter.formatFileSize(FrameApplication.instance(), fileSize));
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                IOUtils.closeQuietly(inputStream, outputStream);
            }
        } catch (IOException e) {
            return false;
        }
    }
    //----------------------------------上传的第一种方法----------------------------

    /**
     * 获取String类型RequestBody集合,多用于上传数据时的描述
     *
     * @param pParamStrs 上传的参数数据
     */
    public static Map<String, RequestBody> getUploadParams(final HashMap<String, String> pParamStrs) {
        Map<String, RequestBody> result = new HashMap<>();
        if (pParamStrs != null && pParamStrs.size() > 0) {
            for (Map.Entry<String, String> entry : pParamStrs.entrySet()) {
                result.put(entry.getKey(), RequestBody.create(MediaType.parse("multipart/form-data"), entry.getValue()));
            }
        }
        return result;
    }

    /**
     * 获取MultipartBody.Part集合,上传的文件集合
     *
     * @param pUploadFiles 上传的文件数据
     */
    public static List<MultipartBody.Part> getUploadFiles(final File... pUploadFiles) {
        List<MultipartBody.Part> result = new ArrayList<>();
        if (pUploadFiles != null && pUploadFiles.length > 0) {
            for (int i = 0, len = pUploadFiles.length; i < len; i++) {
                File file = pUploadFiles[i];
                result.add(MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("application/otcet-stream"), file)));
            }
        }
        return result;
    }

    //----------------------------------上传的第二种方法----------------------------

    /**
     * 第二种上传文件的方法 -> 不推荐:可能会与Body添加公共属性的设置冲突
     *
     * @param pUploadFiles 上传的文件数据
     */
    public static RequestBody getRequestBody(final File... pUploadFiles) {
        return getRequestBody(null, pUploadFiles);
    }

    /**
     * 第二种上传文件的方法 -> 不推荐:可能会与Body添加公共属性的设置冲突
     *
     * @param pParamStrs   上传的参数数据
     * @param pUploadFiles 上传的文件数据
     */
    public static RequestBody getRequestBody(final HashMap<String, String> pParamStrs, final File... pUploadFiles) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (pParamStrs != null && pParamStrs.size() > 0) {
            for (Map.Entry<String, String> entry : pParamStrs.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        if (pUploadFiles != null && pUploadFiles.length > 0) {
            for (int i = 0, len = pUploadFiles.length; i < len; i++) {
                File file = pUploadFiles[i];
                builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/otcet-stream"), file));
            }
        }
        return builder.build();
    }
}
