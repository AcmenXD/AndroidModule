package com.acmenxd.retrofit.load;

import com.acmenxd.retrofit.entity.HttpEntity;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/6 11:41
 * @detail 服务接口定义 -> 上传
 */
public interface IUploadRequest {
    /**
     * 表单上传方式
     */
    @Multipart
    @POST
    Call<HttpEntity> upload(@Url String url, @Part List<MultipartBody.Part> pUploadFiles);

    @Multipart
    @POST
    Observable<HttpEntity> uploadRx(@Url String url, @Part List<MultipartBody.Part> pUploadFiles);

    /**
     * 表单上传方式
     */
    @Multipart
    @POST
    Call<HttpEntity> upload(@Url String url, @Part List<MultipartBody.Part> pUploadFiles, @PartMap() Map<String, RequestBody> pUploadParams);

    @Multipart
    @POST
    Observable<HttpEntity> uploadRx(@Url String url, @Part List<MultipartBody.Part> pUploadFiles, @PartMap() Map<String, RequestBody> pUploadParams);

    /**
     * Body上传方式 -> 不推荐:可能会与Body添加公共属性的设置冲突
     */
    @POST
    Call<HttpEntity> upload(@Url String url, @Body RequestBody pRequestBody);

    @POST
    Observable<HttpEntity> uploadRx(@Url String url, @Body RequestBody pRequestBody);
}
