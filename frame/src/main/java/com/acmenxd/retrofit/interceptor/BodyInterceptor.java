package com.acmenxd.retrofit.interceptor;

import android.support.annotation.NonNull;

import com.acmenxd.retrofit.NetManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/13 18:11
 * @detail 统一添加body拦截器
 */
public final class BodyInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        RequestBody requestBody = original.body();
        Request.Builder requestBuilder = original.newBuilder();
        Map<String, String> bodys = new HashMap<>();
        if (NetManager.INSTANCE.mutualCallback != null) {
            bodys = NetManager.INSTANCE.mutualCallback.getBodys(original.url().toString());
        }
        if (bodys != null && bodys.size() > 0) {
            if (requestBody instanceof FormBody) {
                //表单形式,添加公共body参数
                FormBody.Builder formBody = new FormBody.Builder();
                FormBody oldFormBody = (FormBody) requestBody;
                for (int i = 0, len = oldFormBody.size(); i < len; i++) {
                    formBody.addEncoded(oldFormBody.encodedName(i), oldFormBody.encodedValue(i));
                }
                for (Map.Entry<String, String> entry : bodys.entrySet()) {
                    formBody.add(entry.getKey(), entry.getValue());
                }
                requestBuilder.method(original.method(), formBody.build());
            } else if (NetManager.INSTANCE.noformbody_canaddbody && !(requestBody instanceof MultipartBody)) {
                //非表单形式 & 非上传数据的情况下,添加公共body参数
                String postBodyString = bodyToString(requestBody);
                FormBody.Builder formBody = new FormBody.Builder();
                for (Map.Entry<String, String> entry : bodys.entrySet()) {
                    formBody.add(entry.getKey(), entry.getValue());
                }
                postBodyString += ((postBodyString.length() > 0) ? "&" : "") + bodyToString(formBody.build());
                requestBuilder = requestBuilder
                        .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString));
            }
        }
        return chain.proceed(requestBuilder.build());
    }

    private String bodyToString(@NonNull final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null) {
                copy.writeTo(buffer);
            } else {
                return "";
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
