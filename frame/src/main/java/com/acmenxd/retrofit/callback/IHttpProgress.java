package com.acmenxd.retrofit.callback;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/7/22 13:55
 * @detail 下载上传进度回调
 */
public interface IHttpProgress {
    /**
     * 进度回调
     *
     * @param isDone   是否完成
     * @param total    总字节数
     * @param progress 已经下载或上传字节数
     */
    void progress(boolean isDone, long total, long progress);
}
