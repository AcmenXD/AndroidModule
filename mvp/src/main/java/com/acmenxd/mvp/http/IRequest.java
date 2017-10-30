package com.acmenxd.mvp.http;

import android.graphics.Bitmap;

import com.acmenxd.mvp.model.response.TestEntity;
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
public interface IRequest {
    //----------------------------------------模拟请求----------------------------------

    /**
     * get请求
     */
    @GET("method")
    Call<TestEntity> get(@Query("param") String param);

    /**
     * get请求 - rx
     */
    @GET("method")
    Observable<TestEntity> getRx(@Query("param") String param);

    /**
     * post请求
     */
    @FormUrlEncoded
    @POST("method")
    Call<HttpGenericityEntity<TestEntity>> post(@Field("param") String param);

    /**
     * post请求 - rx
     */
    @FormUrlEncoded
    @POST("method")
    Observable<HttpGenericityEntity<TestEntity>> postRx(@Field("param") String param);

    /**
     * options请求
     */
    @OPTIONS("method")
    Call<TestEntity> options(@Query("param") String param);

    /**
     * put请求
     */
    @PUT("method")
    Call<HttpGenericityEntity<TestEntity>> put(@Query("param") String param, @Body TestEntity str);

    /**
     * Post请求 -> 图片
     */
    @Streaming
    @POST("image")
    Call<Bitmap> downImage(@Query("param") String param);

    /**
     * post请求 -> 上传图片
     */
    @POST("upload")
    Call<HttpEntity> upImage(@Query("param") String param, @Body Bitmap bitmap);

    //----------------------------------------正式请求----------------------------------


}
