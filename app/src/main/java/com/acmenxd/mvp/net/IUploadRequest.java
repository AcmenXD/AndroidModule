package com.acmenxd.mvp.net;

import com.acmenxd.retrofit.NetEntity;

import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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
     *
     * @param pDataFiles
     * @return
     */
    @Multipart
    @POST("upload")
    Observable<NetEntity> upload(@Part List<MultipartBody.Part> pDataFiles);

    /**
     * 表单上传方式
     *
     * @param pDataStrs
     * @param pDataFiles
     * @return
     */
    @Multipart
    @POST("upload")
    Observable<NetEntity> upload(@PartMap() Map<String, RequestBody> pDataStrs, @Part List<MultipartBody.Part> pDataFiles);

    /**
     * Body上传方式
     */
    @POST("upload")
    Observable<NetEntity> upload(@Body RequestBody pRequestBody);
}
