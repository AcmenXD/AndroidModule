package com.acmenxd.frame.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.text.TextUtils;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/23 16:09
 * @detail Bitmap工具类
 */
public final class BitmapUtils {

    /**
     * 截取屏幕(全屏)并保存
     *
     * @param pActivity
     * @param isSaveStatusBar 是否保留状态栏
     * @param pSavePath       保存路径
     */
    public static void saveScreenAsImage(@NonNull Activity pActivity, boolean isSaveStatusBar, @NonNull File pSavePath) {
        View pView = pActivity.getWindow().getDecorView();
        pView.setDrawingCacheEnabled(true);
        pView.buildDrawingCache();
        Bitmap srcBitmap = pView.getDrawingCache();
        // 图片大小
        Point point = new Point();
        pActivity.getWindowManager().getDefaultDisplay().getSize(point);
        int width = pView.getWidth();//point.x;
        int height = pView.getHeight();//point.y;
        // 标题栏高度
        int statusBarHeight = 0;
        if (isSaveStatusBar == false) {
            Rect frame = new Rect();
            pActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
            height = height - statusBarHeight;
        }
        // 创建存储的bitmap
        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, statusBarHeight, width, height);
        // 清理掉缓存图片,防止内存溢出
        pView.destroyDrawingCache();
        // 保存图片
        saveBitmap(bitmap, new File(pSavePath, String.valueOf(RandomUtils.getRandomByTime()) + ".jpg"));
    }

    /**
     * 保存图片到存储盘
     */
    public static void saveBitmap(@NonNull Bitmap pBitmap, @NonNull File saveFile) {
        saveBitmap(pBitmap, saveFile, null, 100);
    }

    /**
     * 保存图片到存储盘
     *
     * @param pBitmap  存储的bitmap对象
     * @param savePath 存储路径
     * @param format   图片格式 (默认 PNG 格式)
     * @param quality  图片质量 0 - 100,如<=0,则默认为100
     */
    public static void saveBitmap(@NonNull Bitmap pBitmap, @NonNull File savePath, @NonNull Bitmap.CompressFormat format, @IntRange(from = 0) int quality) {
        if (format == null) {
            format = Bitmap.CompressFormat.PNG;
        }
        if (quality <= 0) {
            quality = 100;
        }
        try {
            FileOutputStream fos = new FileOutputStream(savePath);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            pBitmap.compress(format, quality, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取本地图片,如图片宽高大于指定宽高,会自动按比例缩放
     * 如width<=0 或 height <=0 则不进行缩放
     */
    public static Bitmap readBitmap(@NonNull String file, @IntRange(from = 0) int width, @IntRange(from = 0) int height) {
        try {
            FileInputStream fis = new FileInputStream(file);
            BitmapFactory.Options options = new BitmapFactory.Options();
            int inSampleSize = 1;
            if (width > 0 && height > 0) {
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
                float srcWidth = options.outWidth;
                float srcHeight = options.outHeight;
                if (srcHeight > height || srcWidth > width) {
                    if (srcWidth > srcHeight) {
                        inSampleSize = Math.round(srcHeight / height);
                    } else {
                        inSampleSize = Math.round(srcWidth / width);
                    }
                }
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;
            return BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * 读取流图片,如图片宽高大于指定宽高,会自动按比例缩放
     * 如width<=0 或 height <=0 则不进行缩放
     */
    public static Bitmap readBitmap(@NonNull InputStream ins, @IntRange(from = 0) int width, @IntRange(from = 0) int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        int inSampleSize = 1;
        if (width > 0 && height > 0) {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(ins, null, options);
            float srcWidth = options.outWidth;
            float srcHeight = options.outHeight;
            if (srcHeight > height || srcWidth > width) {
                if (srcWidth > srcHeight) {
                    inSampleSize = Math.round(srcHeight / height);
                } else {
                    inSampleSize = Math.round(srcWidth / width);
                }
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeStream(ins, null, options);
    }

    /**
     * 读取资源图片,如图片宽高大于指定宽高,会自动按比例缩放
     * 如width<=0 或 height <=0 则不进行缩放
     */
    public static Bitmap readBitmap(@NonNull Resources resources, @RawRes int resourcesId, @IntRange(from = 0) int width, @IntRange(from = 0) int height) {
        InputStream ins = resources.openRawResource(resourcesId);
        BitmapFactory.Options options = new BitmapFactory.Options();
        int inSampleSize = 1;
        if (width > 0 && height > 0) {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(ins, null, options);
            float srcWidth = options.outWidth;
            float srcHeight = options.outHeight;
            if (srcHeight > height || srcWidth > width) {
                if (srcWidth > srcHeight) {
                    inSampleSize = Math.round(srcHeight / height);
                } else {
                    inSampleSize = Math.round(srcWidth / width);
                }
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeStream(ins, null, options);
    }

    /**
     * 读取资源图片,如图片宽高大于指定宽高,会自动按比例缩放
     * 如width<=0 或 height <=0 则不进行缩放
     * * 此方式较耗费内存,建议使用上面的函数
     */
    public static Bitmap readBitmapFromResources(@NonNull Resources resources, @DrawableRes int resourcesId, @IntRange(from = 0) int width, @IntRange(from = 0) int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        int inSampleSize = 1;
        if (width > 0 && height > 0) {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(resources, resourcesId, options);
            float srcWidth = options.outWidth;
            float srcHeight = options.outHeight;
            if (srcHeight > height || srcWidth > width) {
                if (srcWidth > srcHeight) {
                    inSampleSize = Math.round(srcHeight / height);
                } else {
                    inSampleSize = Math.round(srcWidth / width);
                }
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeResource(resources, resourcesId, options);
    }

    /**
     * 读取二进制图片,如图片宽高大于指定宽高,会自动按比例缩放
     * 如width<=0 或 height <=0 则不进行缩放
     */
    public static Bitmap readBitmap(@NonNull byte[] data, @IntRange(from = 0) int width, @IntRange(from = 0) int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        int inSampleSize = 1;
        if (width > 0 && height > 0) {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, options);
            float srcWidth = options.outWidth;
            float srcHeight = options.outHeight;
            if (srcHeight > height || srcWidth > width) {
                if (srcWidth > srcHeight) {
                    inSampleSize = Math.round(srcHeight / height);
                } else {
                    inSampleSize = Math.round(srcWidth / width);
                }
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * 读取assets图片
     */
    public static Bitmap readBitmap(@NonNull Context context, @NonNull String file) {
        Bitmap bitmap = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(file);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 图片压缩
     */
    public static Bitmap compressImage(@NonNull Bitmap pBitmap, @NonNull Bitmap.CompressFormat format, @IntRange(from = 0) int quality) {
        if (pBitmap == null) {
            return null;
        }
        if (format == null) {
            format = Bitmap.CompressFormat.PNG;
        }
        if (quality <= 0) {
            quality = 100;
        }
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            pBitmap.compress(format, quality, baos);
            byte[] bytes = baos.toByteArray();
            ByteArrayInputStream isBm = new ByteArrayInputStream(bytes);
            Bitmap bitmap = BitmapFactory.decodeStream(isBm);
            return bitmap;
        } catch (OutOfMemoryError e) {
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
            }
        }
        return null;
    }

    /**
     * 图片等比缩放
     */
    public static Bitmap scaleBitmap(@NonNull Bitmap pBitmap, @FloatRange(from = 0) float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(pBitmap, 0, 0, pBitmap.getWidth(), pBitmap.getHeight(), matrix, true);
    }

    /**
     * 图片旋转
     */
    private static Bitmap rotateBitmap(@NonNull Bitmap pBitmap, @FloatRange(from = 0) float rotateDegree) {
        if (pBitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree);
        return Bitmap.createBitmap(pBitmap, 0, 0, pBitmap.getWidth(), pBitmap.getHeight(), matrix, true);
    }

    /**
     * 读取图片exif信息中的旋转角度
     */
    public static int readPictureDegree(@NonNull String path) {
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Exception e) {
        }
        return degree;
    }

    /**
     * Bitmap转二进制
     */
    public static byte[] bitmapToBytes(@NonNull Bitmap pBitmap, @NonNull Bitmap.CompressFormat format, @IntRange(from = 0) int quality) {
        if (pBitmap == null) {
            return null;
        }
        if (format == null) {
            format = Bitmap.CompressFormat.PNG;
        }
        if (quality <= 0) {
            quality = 100;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        pBitmap.compress(format, quality, baos);
        return baos.toByteArray();
    }

    /**
     * Bitmap转Drawable
     */
    public static Drawable bitmapToDrawable(@NonNull Resources resources, @NonNull Bitmap pBitmap) {
        Drawable drawable = new BitmapDrawable(resources, pBitmap);
        return drawable;
    }

    /**
     * Drawable转Bitmap
     */
    public static Bitmap drawableToBitmap(@NonNull Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
