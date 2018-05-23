package com.acmenxd.glide;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/4 11:31
 * @detail Glide管理类
 */
public final class GlideManager implements GlideModule {
    /**
     * 初始化配置
     */
    // 默认图片解码格式
    public static DecodeFormat DECODEFORMAT = DecodeFormat.PREFER_RGB_565;
    // 默认缓存路径
    public static String IMAGE_CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Glide/";
    // 默认缓存文件夹
    public static String MAIN_CACHE_DIR = "MainCache";
    // 默认磁盘缓存的最大值
    public static int MAX_DISK_CACHE_SIZE = 50;
    // 分配的可用内存
    private static int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();
    private static int MAX_HEAP_SIZE_FINAL = MAX_HEAP_SIZE / 4;
    // 默认内存缓存的最大值
    private static int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE_FINAL;
    private static int MAX_MEMORY_CACHE_SIZE_POOL = MAX_HEAP_SIZE_FINAL;

    /**
     * 设置缓存内存大小(建议不要手动设置)(MB)
     */
    public static void setCacheSizeMemory(@IntRange(from = 0) long memory, @IntRange(from = 0) long memory_pool) {
        MAX_MEMORY_CACHE_SIZE = (int) memory * 1024 * 1024;
        MAX_MEMORY_CACHE_SIZE_POOL = (int) memory_pool * 1024 * 1024;
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        if (MAX_MEMORY_CACHE_SIZE == MAX_HEAP_SIZE_FINAL) {
            MAX_MEMORY_CACHE_SIZE = calculator.getMemoryCacheSize();
        }
        if (MAX_MEMORY_CACHE_SIZE_POOL == MAX_HEAP_SIZE_FINAL) {
            MAX_MEMORY_CACHE_SIZE_POOL = calculator.getBitmapPoolSize();
        }
        // 设置内存缓存大小
        builder.setMemoryCache(new LruResourceCache(MAX_MEMORY_CACHE_SIZE));
        // 设置BitmapPool缓存内存大小
        builder.setBitmapPool(new LruBitmapPool(MAX_MEMORY_CACHE_SIZE_POOL));
        // 设置磁盘缓存大小
        builder.setDiskCache(new DiskLruCacheFactory(IMAGE_CACHE_PATH, MAIN_CACHE_DIR, MAX_DISK_CACHE_SIZE * 1024 * 1024));
        // 设置图片解码格式 ,默认格式RGB_565
        builder.setDecodeFormat(DECODEFORMAT);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
    }

    /**
     * 保存已加载的图片
     * * 如callback不为空,则回调函数的是在子线程中执行的
     */
    public static void saveImage(@NonNull Context pContext, @NonNull String url, @NonNull File outFile, SaveCallback callback) {
        saveImage(Glide.with(pContext).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL), outFile, callback);
    }

    public static void saveImage(@NonNull Context pContext, @NonNull Uri uri, @NonNull File outFile, SaveCallback callback) {
        saveImage(Glide.with(pContext).load(uri).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL), outFile, callback);
    }

    public static void saveImage(@NonNull Context pContext, @NonNull File file, @NonNull File outFile, SaveCallback callback) {
        saveImage(Glide.with(pContext).load(file).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL), outFile, callback);
    }

    public static void saveImage(@NonNull Context pContext, @NonNull Integer resourceId, @NonNull File outFile, SaveCallback callback) {
        saveImage(Glide.with(pContext).load(resourceId).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL), outFile, callback);
    }

    public static void saveImage(@NonNull Context pContext, @NonNull byte[] model, @NonNull File outFile, SaveCallback callback) {
        saveImage(Glide.with(pContext).load(model).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL), outFile, callback);
    }

    public static void saveImage(@NonNull final FutureTarget<File> target, @NonNull final File outFile, final SaveCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = target.get();
                    if (file == null || !file.exists()) {
                        throw new RuntimeException("图片获取失败!");
                    } else {
                        GlideUtils.copyFile(file, outFile, true);
                        if (callback != null) {
                            if (outFile.exists()) {
                                callback.succeed(outFile);
                            } else {
                                callback.failed(new RuntimeException("图片保存失败!"));
                            }
                        }
                    }
                } catch (InterruptedException pE) {
                    pE.printStackTrace();
                } catch (ExecutionException pE) {
                    pE.printStackTrace();
                } catch (IOException pE) {
                    pE.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 清理磁盘缓存 需要在子线程中执行
     */
    @WorkerThread
    public static void clearDiskCache(@NonNull final Context pContext) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(pContext).clearDiskCache();
            }
        }).start();
    }

    /**
     * 清理内存缓存  可以在UI主线程中进行
     */
    public static void clearMemory(@NonNull final Context pContext) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(pContext).clearMemory();
            }
        }).start();
    }
}
