package com.acmenxd.mvp.http;

import android.graphics.Bitmap;

import com.acmenxd.mvp.model.response.TestEntity;
import com.acmenxd.mvp.model.response.TestHttpEntity;
import com.acmenxd.retrofit.HttpEntity;
import com.acmenxd.retrofit.HttpGenericityEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.OPTIONS;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import rx.Observable;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/3 15:13
 * @detail 服务接口定义
 */
public interface IAllRequest {
    /**
     * get请求
     */
    @GET("method")
    Call<TestHttpEntity> get(@Query("token") String token);

    /**
     * options请求
     */
    @OPTIONS("method")
    Call<TestHttpEntity> options(@Query("token") String token);

    /**
     * post请求
     */
    @FormUrlEncoded
    @POST("method")
    Observable<HttpGenericityEntity<TestEntity>> post(@Field("token") String token);

    /**
     * put请求
     */
    @PUT("method")
    Call<HttpGenericityEntity<TestEntity>> put(@Query("token") String token, @Body TestEntity str);

    /**
     * Post请求 -> 图片
     */
    @Streaming
    @POST("image")
    Call<Bitmap> image(@Query("token") String token);

    /**
     * post请求 -> 上传图片
     */
    @POST("upload")
    Call<HttpEntity> upImage(@Query("token") String token, @Body Bitmap str);

}
