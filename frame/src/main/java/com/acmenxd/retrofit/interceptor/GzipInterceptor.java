package com.acmenxd.retrofit.interceptor;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/13 18:11
 * @detail gzip压缩拦截器
 */
public final class GzipInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        if (original.body() == null || original.header("Content-Encoding") != null) {
            return chain.proceed(original);
        }
        // 启用gzip
        Request request = original.newBuilder()
                .header("Content-Encoding", "gzip")
                .method(original.method(), gzip(original.body()))
                .build();
        return chain.proceed(request);
    }

    /**
     * gzip 压缩
     */
    private RequestBody gzip(@NonNull final RequestBody body) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() {
                return -1;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    }
}
