package com.acmenxd.mvp.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/6 11:41
 * @detail 服务接口定义 -> 下载
 */
public interface IDownloadRequest {
    /**
     * 下载文件 - get
     */
    @Streaming
    @GET
    Call<ResponseBody> get(@Url String url);

    @Streaming
    @GET
    Observable<ResponseBody> getRx(@Url String url);

    /**
     * 下载文件 - post
     */
    @Streaming
    @POST
    Call<ResponseBody> post(@Url String url);

    @Streaming
    @POST
    Observable<ResponseBody> postRx(@Url String url);
}