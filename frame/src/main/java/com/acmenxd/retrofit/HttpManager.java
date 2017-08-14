package com.acmenxd.retrofit;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.acmenxd.logger.LogTag;
import com.acmenxd.retrofit.callback.IHttpProgress;
import com.acmenxd.retrofit.callback.ProgressRequestBody;
import com.acmenxd.retrofit.callback.ProgressResponseBody;
import com.acmenxd.retrofit.converter.CustomConverterFactory;
import com.acmenxd.retrofit.cookie.HttpCookieJar;
import com.acmenxd.retrofit.interceptor.BodyInterceptor;
import com.acmenxd.retrofit.interceptor.HeaderInterceptor;
import com.acmenxd.retrofit.interceptor.LoggerInterceptor;
import com.acmenxd.retrofit.interceptor.NetworkInterceptor;
import com.acmenxd.retrofit.interceptor.ParameterInterceptor;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/28 11:46
 * @detail 网络请求总类
 */
public enum HttpManager {
    INSTANCE;
    /**
     * 初始化配置
     */
    // 上下文对象
    public Context context;
    // 基础URL地址
    public String base_url = "www.baidu.com";
    // Net Log 的日志Tag
    public LogTag net_log_tag = LogTag.mk("NetLog");
    // Net Log 的日志显示形式 -> 是否显示 "请求头 请求体 响应头 错误日志" 等详情
    public boolean net_log_details = true;
    // Net Log 的日志显示形式 -> 是否显示请求过程中的日志,包含详细的请求头日志
    public boolean net_log_details_all = false;
    // 非Form表单形式的请求体,是否加入公共Body
    public boolean noformbody_canaddbody = false;
    // 网络缓存默认存储路径
    public File net_cache_dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/NetCache/");
    // 网络缓存策略: 0->不启用缓存  1->遵从服务器缓存配置
    public int net_cache_type = 1;
    // 网络缓存大小(MB)
    public int net_cache_size = 10;
    // 网络连接超时时间(秒)
    public int connect_timeout = 30;
    // 读取超时时间(秒)
    public int read_timeout = 30;
    // 写入超时时间(秒)
    public int write_timeout = 30;
    // 请求返回时回调
    public HttpResultCallback resultCallback;
    // 请求公共参数回调
    public HttpMutualCallback mutualCallback;

    private Retrofit mRetrofit;

    /**
     * 根据IRequest类获取Request实例
     */
    public <T> T request(@NonNull Class<T> pIRequest) {
        if (mRetrofit == null) {
            return newRequest(pIRequest);
        }
        return mRetrofit.create(pIRequest);
    }

    /**
     * 创建新的Retrofit实例
     * 根据IRequest类获取Request实例
     */
    public <T> T newRequest(@NonNull Class<T> pIRequest) {
        return newRequest(pIRequest, connect_timeout, read_timeout, write_timeout);
    }

    /**
     * 创建新的Retrofit实例,并设置超时时间
     * 根据IRequest类获取Request实例
     */
    public <T> T newRequest(@NonNull Class<T> pIRequest, @IntRange(from = 0) int connectTimeout, @IntRange(from = 0) int readTimeout, @IntRange(from = 0) int writeTimeout) {
        mRetrofit = createRetrofit(createClientBuilder(connectTimeout, readTimeout, writeTimeout).build());
        return mRetrofit.create(pIRequest);
    }

    /**
     * 下载Retrofit实例 -> 默认读取超时时间5分钟
     * 根据IRequest类获取Request实例
     *
     * @param pProgress 下载进度回调
     */
    public <T> T downloadRequest(@NonNull Class<T> pIRequest, @NonNull IHttpProgress pProgress) {
        return downloadRequest(pIRequest, pProgress, 60 * 5);
    }

    public <T> T downloadRequest(@NonNull Class<T> pIRequest, @NonNull IHttpProgress pProgress, @IntRange(from = 0) int read_timeout) {
        if (pProgress == null) {
            return request(pIRequest);
        }
        final IHttpProgress progress = pProgress;
        OkHttpClient.Builder builder = createClientBuilder(connect_timeout, read_timeout, write_timeout);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                response = response.newBuilder().body(new ProgressResponseBody(response.body(), progress)).build();
                response.body().source().request(Long.MAX_VALUE);
                return response;
            }
        });
        return createRetrofit(builder.build()).create(pIRequest);
    }

    /**
     * 上传Retrofit实例 -> 默认写入超时时间5分钟
     * 根据IRequest类获取Request实例
     *
     * @param pProgress 上传进度回调
     */
    public <T> T uploadRequest(@NonNull Class<T> pIRequest, @NonNull IHttpProgress pProgress) {
        return uploadRequest(pIRequest, pProgress, 60 * 5);
    }

    public <T> T uploadRequest(@NonNull Class<T> pIRequest, @NonNull IHttpProgress pProgress, @IntRange(from = 0) int writeTimeout) {
        if (pProgress == null) {
            return request(pIRequest);
        }
        final IHttpProgress progress = pProgress;
        OkHttpClient.Builder builder = createClientBuilder(connect_timeout, read_timeout, writeTimeout);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder()
                        .method(request.method(), new ProgressRequestBody(request.body(), progress));
                return chain.proceed(requestBuilder.build());
            }
        });
        return createRetrofit(builder.build()).create(pIRequest);
    }

    /**
     * 创建 Retrofit实例
     */
    private Retrofit createRetrofit(@NonNull OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                // 设置baseUrl
                .baseUrl(base_url)
                // 使用RxJava
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                // 网络数据解析总类
                .addConverterFactory(CustomConverterFactory.create())
                // 设置OkHttpClient
                .client(client)
                // 构建
                .build();
        return retrofit;
    }

    /**
     * 创建 OkHttpClient实例
     */
    private OkHttpClient.Builder createClientBuilder(@IntRange(from = 0) int connectTimeout, @IntRange(from = 0) int readTimeout, @IntRange(from = 0) int writeTimeout) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 检查网络
        builder.addInterceptor(new NetworkInterceptor());
        // 添加参数
        builder.addInterceptor(new ParameterInterceptor());
        // 添加请求头
        builder.addInterceptor(new HeaderInterceptor());
        // 添加Body参数
        builder.addInterceptor(new BodyInterceptor());
        //模拟网络返回
        //builder.addInterceptor(new MockInterceptor());
        // 设置Log日志 -> 需在Gzip前面,否则输出信息因为Gzip压缩导致乱码
        if (net_log_details_all) {
            // 如启用此日志方式,Gzip也开启的情况下,输入日志会有乱码
            builder.addNetworkInterceptor(new LoggerInterceptor());
        } else {
            builder.addInterceptor(new LoggerInterceptor());
        }
        // 启用Gzip压缩
        // mClientBuilder.addInterceptor(new GzipInterceptor());
        // 设置缓存
        if (net_cache_type != 0) {
            // mBuilder.addNetworkInterceptor(new NetCacheInterceptor3()); // 功能尚未完成,无法使用
            builder.cache(new Cache(net_cache_dir, net_cache_size * 1024 * 1024));
        }
        // 启用cookie -> 参考http://www.jianshu.com/p/1a5f14b63f47
        // mClientBuilder.cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context)));
        builder.cookieJar(HttpCookieJar.create());
        // 失败重试
        builder.retryOnConnectionFailure(true);
        // 设置超时时间
        builder.connectTimeout(connectTimeout, TimeUnit.SECONDS);
        builder.writeTimeout(writeTimeout, TimeUnit.SECONDS);
        builder.readTimeout(readTimeout, TimeUnit.SECONDS);
        /**
         * 忽略所有https
         */
        SSLContext sslContext = null;
        try {
//            sslContext = SSLContext.getInstance("TLS");
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }}, new SecureRandom());
        } catch (KeyManagementException pE) {
            pE.printStackTrace();
        } catch (NoSuchAlgorithmException pE) {
            pE.printStackTrace();
        }
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        builder.sslSocketFactory(sslContext.getSocketFactory());
        /**
         * 添加https证书 - http://blog.csdn.net/sk719887916/article/details/51597816
         */
//        /**
//         * 添加证书Pinning
//         */
//        mBuilder.certificatePinner(new CertificatePinner.Builder()
//                .add("YOU API.com", "sha1/DmxUShsZuNiqPQsX2Oi9uv2sCnw=")
//                .add("YOU API..com", "sha1/SXxoaOSEzPC6BgGmxAt/EAcsajw=")
//                .add("YOU API..com", "sha1/blhOM3W9V/bVQhsWAcLYwPU6n24=")
//                .add("YOU API..com", "sha1/T5x9IXmcrQ7YuQxXnxoCmeeQ84c=")
//                .build());
//        /**
//         * 设置代理
//         */
//        mBuilder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
        return builder;
    }
}
