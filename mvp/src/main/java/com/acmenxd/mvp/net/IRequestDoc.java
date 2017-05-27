package com.acmenxd.mvp.net;

import com.acmenxd.retrofit.NetEntity;
import com.acmenxd.mvp.model.response.TestEntity;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/9 16:13
 * @detail 服务接口定义  测试用
 */
public interface IRequestDoc {

    /**
     * 解释 -----------------------------start
     * * 请求如果不需要传该参数的话，只需填充null即可
     */
    /**
     * @GET 表示get请求
     * @Url 假如某一个请求不是以base_url开头,此方式传递完整的url地址
     */
    @GET("method")
    Call<NetEntity<TestEntity>> test6(
            @Url String url
    );

    /**
     * @Headers 定义Http请求头参数
     * @Query 表示请求参数, 会以key=value的方式拼接在url后面
     * @Path 如果请求的相对地址也是需要调用方传递
     */
    @Headers("Cache-Control: max-age=640000")
    @GET("method/{id}")
    //http://server.jeasonlzy.com/OkHttpUtils/method/1003078
    Call<NetEntity<TestEntity>> test5(
            @Path("id") String id,
            @Query("name") String name,
            @Query("age") int age
    );

    /**
     * @Headers 定义多个Http请求头参数
     * @QueryMap 将所有的参数集成在一个Map统一传递
     */
    @Headers({
            "Accept: application/vnd.yourapi.v1.full+json",
            "User-Agent: Your-App-Name"
    })
    @GET("method")
    Call<NetEntity<TestEntity>> test4(
            @QueryMap Map<String, String> params
    );

    /**
     * @Header 动态请求头
     * @Query 相同Key值，但是value却有多个的情况
     */
    @GET("method")
    Call<NetEntity<TestEntity>> test3(
            @Header("Content-Range") String contentRange,
            @Query("params") List<String> params
    );

    /**
     * @POST 表示post请求
     * @FormUrlEncoded 将会自动将请求参数的类型调整为application/x-www-form-urlencoded，
     * 假如content传递的参数为Good Luck，那么最后得到的请求体就是 content=Good+Luck
     * FormUrlEncoded不能用于Get请求
     * @Field 将每一个请求参数都存放至请求体中，还可以添加encoded参数，该参数为boolean型
     * encoded参数为true的话，key-value-pair将会被编码，即将中文和特殊字符进行编码转换
     */
    @FormUrlEncoded
    @POST("method")
    Call<NetEntity<TestEntity>> test2(
            @Field(value = "book", encoded = true) String bookId,
            @Field("title") String title
    );

    /**
     * @FieldMap Map形式传递参数
     */
    @FormUrlEncoded
    @POST("method")
    Call<NetEntity<TestEntity>> test1(@FieldMap Map<String, String> fields);

    /**
     * @Body 参数统一封装到类中会更好，这样维护起来会非常方便
     */
    @POST("method")
    Call<NetEntity<TestEntity>> test(@Body TestEntity pTestEntity);

    // 上传部分
    @Multipart
    @POST("url")
    Call<ResponseBody> uploadFlie(
            @Part("description") RequestBody description,
            @Part("files") MultipartBody.Part file);
    @Multipart
    @POST("{url}")
    Call<ResponseBody> uploadFiles(
            @Path("url") String url,
            @PartMap() Map<String, RequestBody> maps);

    //下载部分
    @Streaming
    @POST("{url}")
    Call<ResponseBody> downloadFiles(@Path("url") String url);
    // 解释结束 -----------------------------end
}
