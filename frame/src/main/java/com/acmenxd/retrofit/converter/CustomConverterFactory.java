package com.acmenxd.retrofit.converter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/4 15:09
 * @detail retrofit 网络数据解析总类
 */
public final class CustomConverterFactory extends Converter.Factory {
    private Gson mGson;

    public static CustomConverterFactory create() {
        return new CustomConverterFactory();
    }

    public static CustomConverterFactory create(@NonNull Gson gson) {
        return new CustomConverterFactory(gson);
    }

    private CustomConverterFactory(@NonNull Gson gson) {
        if (gson != null) {
            mGson = gson;
        } else {
            mGson = getGson();
        }
    }

    private CustomConverterFactory() {
        mGson = getGson();
    }

    private Gson getGson() {
        return new Gson();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        if (type instanceof Class<?>) {
            // 非泛型
            if (type.equals(Bitmap.class)) {
                // byte -> Bitmap
                return new BitmapRequestBodyConverter();
            }
        }
        // ? 类型 -> json(Gson解析)
        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(mGson, adapter);
//        if (type instanceof ParameterizedType) {
//            // 确认泛型类型
//            Type rawType = ((ParameterizedType) type).getRawType();
//            // 原始类型匹配
//            if (NetEntityToJson.check(rawType)) {
//                // ? 类型 -> json(Gson解析)
//                TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
//                return new GsonRequestBodyConverter<>(mGson, adapter);
//            }
//        }
//        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);

// ------------------------------------泛型解析
//        Type rawType = ((ParameterizedType) type).getRawType();
//        Type actualType = ((ParameterizedType) type).getActualTypeArguments()[0];
//        if (rawType == List.class && actualType == User.class)
//        {
//
//        }
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type instanceof Class<?>) {
            // 非泛型
            if (type.equals(Bitmap.class)) {
                // byte -> Bitmap
                return new BitmapResponseBodyConverter();
            }
        }
        // json(Gson解析) -> ? 类型
        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
        return new GsonResponseBodyConverter<>(mGson, adapter);
//        if (type instanceof ParameterizedType) {
//            // 确认泛型类型
//            Type rawType = ((ParameterizedType) type).getRawType();
//            // 原始类型匹配
//            if (rawType.equals(NetEntity.class)) {
//                // json(Gson解析) -> ? 类型
//                TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
//                return new GsonResponseBodyConverter<>(mGson, adapter);
//            }
//        }
//        return super.responseBodyConverter(type, annotations, retrofit);
    }

    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return super.stringConverter(type, annotations, retrofit);
    }
}
