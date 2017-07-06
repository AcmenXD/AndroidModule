package com.acmenxd.retrofit;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.acmenxd.logger.LogTag;
import com.acmenxd.retrofit.converter.CustomConverterFactory;
import com.acmenxd.retrofit.cookie.NetCookieJar;
import com.acmenxd.retrofit.interceptor.BodyInterceptor;
import com.acmenxd.retrofit.interceptor.HeaderInterceptor;
import com.acmenxd.retrofit.interceptor.LoggerInterceptor;
import com.acmenxd.retrofit.interceptor.ParameterInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/28 11:46
 * @detail 网络请求总类
 */
public enum NetManager {
    INSTANCE;
    /**
     * 初始化配置
     */
    // 上下文对象
    public Context context;
    // 基础URL地址
    public String base_url = "";
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
    // 统一处理NetCode回调
    public NetCodeParse.parseNetCode parseNetCode;
    public NetMutualCallback mutualCallback;

    private Retrofit mRetrofit;

    /**
     * 根据IRequest类获取Request实例
     */
    public <T> T request(@NonNull Class<T> pIRequest) {
        if (mRetrofit == null) {
            mRetrofit = createRetrofit(createClient(connect_timeout, read_timeout, write_timeout));
        }
        return mRetrofit.create(pIRequest);
    }

    /**
     * 创建新的Retrofit实例
     * 根据IRequest类获取Request实例
     */
    public <T> T newRequest(@NonNull Class<T> pIRequest) {
        return newRequest(connect_timeout, read_timeout, write_timeout, pIRequest);
    }

    /**
     * 创建新的Retrofit实例,并设置超时时间
     * 根据IRequest类获取Request实例
     */
    public <T> T newRequest(@IntRange(from = 0) int connectTimeout, @IntRange(from = 0) int readTimeout, @IntRange(from = 0) int writeTimeout, @NonNull Class<T> pIRequest) {
        mRetrofit = createRetrofit(createClient(connectTimeout, readTimeout, writeTimeout));
        return mRetrofit.create(pIRequest);
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
    private OkHttpClient createClient(@IntRange(from = 0) int connectTimeout, @IntRange(from = 0) int readTimeout, @IntRange(from = 0) int writeTimeout) {
        OkHttpClient.Builder mClientBuilder = new OkHttpClient.Builder();
        // 添加参数
        mClientBuilder.addInterceptor(new ParameterInterceptor());
        // 添加请求头
        mClientBuilder.addInterceptor(new HeaderInterceptor());
        // 添加Body参数
        mClientBuilder.addInterceptor(new BodyInterceptor());
        // 设置Log日志 -> 需在Gzip前面,否则输出信息因为Gzip压缩导致乱码
        if (net_log_details_all) {
            // 如启用此日志方式,Gzip也开启的情况下,输入日志会有乱码
            mClientBuilder.addNetworkInterceptor(new LoggerInterceptor());
        } else {
            mClientBuilder.addInterceptor(new LoggerInterceptor());
        }
        // 启用Gzip压缩
        // mClientBuilder.addInterceptor(new GzipInterceptor());
        // 设置缓存
        if (net_cache_type != 0) {
            // mBuilder.addNetworkInterceptor(new NetCacheInterceptor3()); // 功能尚未完成,无法使用
            mClientBuilder.cache(new Cache(net_cache_dir, net_cache_size * 1024 * 1024));
        }
        // 启用cookie -> 参考http://www.jianshu.com/p/1a5f14b63f47
        // mClientBuilder.cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context)));
        mClientBuilder.cookieJar(NetCookieJar.create());
        // 失败重试
        mClientBuilder.retryOnConnectionFailure(true);
        // 设置超时时间
        mClientBuilder.connectTimeout(connectTimeout, TimeUnit.SECONDS);
        mClientBuilder.readTimeout(readTimeout, TimeUnit.SECONDS);
        mClientBuilder.writeTimeout(writeTimeout, TimeUnit.SECONDS);
//        /**
//         * 添加证书
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
//        /**
//         * 支持https
//         * http://blog.csdn.net/sk719887916/article/details/51597816
//         */
//        SLSocketFactory sslSocketFactory = getSSLSocketFactory_Certificate(context, "BKS", R.raw.srca);
        return mClientBuilder.build();
    }
}
