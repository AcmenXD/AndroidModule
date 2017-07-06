package com.acmenxd.retrofit.interceptor.useless;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/13 18:11
 * @detail 有网络的时候请求网络 | 自己设置一定的缓存，没有网络的时候添加缓存
 * * 功能尚未完成,无法使用
 */
public final class NetCacheInterceptor2 implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();//获取请求
        boolean isNetWork = true;//是否有网络 NetworkUtils.isNetworkAvailable(BaseApp.getInstance().getApplicationContext())
        //判断网络条件，有网络的话就直接获取网络上面的数据，没有网络的话就去缓存里面取数据
        if (!isNetWork) {
            request = request.newBuilder()
                    //只从缓存取，缓存策略。
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response originalResponse = chain.proceed(request);
        if (isNetWork) {
            String cacheControl = request.cacheControl().toString();
            return originalResponse.newBuilder()
                    //这里设置的为0就是说不进行缓存，我们也可以设置缓存时间
                    .header("Cache-Control", "public, max-age=" + 0)
                    .removeHeader("Pragma")
                    .build();
        } else {
            int maxTime = 4 * 24 * 60 * 60;
            return originalResponse.newBuilder()
                    //这里的设置的是我们的没有网络的缓存时间，想设置多少就是多少。
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxTime)
                    .removeHeader("Pragma")
                    .build();
        }
    }
}
