package com.acmenxd.retrofit.converter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/4 17:29
 * @detail 下载Bitmap对象拆包类
 */
final class BitmapResponseBodyConverter implements Converter<ResponseBody, Bitmap> {
    @Override
    public Bitmap convert(ResponseBody value) throws IOException {
        try {
            return BitmapFactory.decodeStream(value.byteStream());
        } finally {
            value.close();
        }
    }
}
