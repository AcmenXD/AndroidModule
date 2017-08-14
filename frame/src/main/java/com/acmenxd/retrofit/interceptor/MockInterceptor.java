package com.acmenxd.retrofit.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 模拟服务端返回json数据--方便测试使用
 */

public class MockInterceptor implements Interceptor {


    private String responseString = "";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        Response.Builder responseBuilder = new Response.Builder()
                .code(200)
                .message("")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .addHeader("content-type", "application/json");
        Request request = chain.request();
        if (request.url().toString().contains("asyncDoInvest.c")) { //拦截指定地址

            responseString = "{\"sys_time\":1501472461131,\"code\":-15,\"msg\":\"标的过期\"}";
            responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()));//将数据设置到body中
            response = responseBuilder.build(); //builder模式构建response

        } else {
            response = chain.proceed(request);
        }
        return response;
    }

}
