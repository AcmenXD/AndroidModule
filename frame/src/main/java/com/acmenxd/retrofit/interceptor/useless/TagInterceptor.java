package com.acmenxd.retrofit.interceptor.useless;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/13 18:11
 * @detail 添加Tag拦截器
 */
public final class TagInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String tag = original.header("NetTag");
        if (!TextUtils.isEmpty(tag)) {
            Request request = original.newBuilder().tag(tag).build();
            return chain.proceed(request);
        }
        return chain.proceed(original);
    }
}
