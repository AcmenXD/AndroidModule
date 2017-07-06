package com.acmenxd.retrofit.converter;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/4 17:29
 * @detail 上传Bitmap对象打包类
 */
final class BitmapRequestBodyConverter implements Converter<Bitmap, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("image/png");
//    private static final MediaType MEDIA_TYPE = MediaType.parse("image/jpeg");
//    private static final MediaType MEDIA_TYPE = MediaType.parse("image/gif");
    @Override
    public RequestBody convert(Bitmap pBitmap) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        pBitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        byte[] result = output.toByteArray();
        output.close();
        return RequestBody.create(MEDIA_TYPE, result);
    }
}
