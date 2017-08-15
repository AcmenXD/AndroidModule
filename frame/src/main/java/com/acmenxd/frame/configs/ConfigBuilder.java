package com.acmenxd.frame.configs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.acmenxd.frame.basis.FrameApplication;
import com.acmenxd.frame.utils.FileUtils;
import com.acmenxd.frame.utils.code.EncodeDecode;
import com.acmenxd.frescoview.FrescoManager;
import com.acmenxd.glide.GlideManager;
import com.acmenxd.logger.Logger;
import com.acmenxd.retrofit.HttpManager;
import com.acmenxd.sptool.SpEncodeDecodeCallback;
import com.acmenxd.sptool.SpManager;
import com.acmenxd.toaster.Toaster;
import com.bumptech.glide.load.DecodeFormat;

import java.io.File;
import java.io.IOException;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/15 16:01
 * @detail 构建配置
 */
public final class ConfigBuilder {
    // 配置详细参数类
    private static BaseConfig sConfigInfo;

    /**
     * 创建配置详情
     */
    public static void createConfig(@NonNull Class<? extends BaseConfig> pConfig, @NonNull boolean isDebug) {
        try {
            sConfigInfo = pConfig.newInstance();
            sConfigInfo.init(isDebug);
        } catch (InstantiationException pE) {
            pE.printStackTrace();
        } catch (IllegalAccessException pE) {
            pE.printStackTrace();
        }
    }

    /**
     * 获取配置详情
     */
    public static <T extends BaseConfig> T getConfigInfo() {
        if (sConfigInfo != null) {
            return (T) sConfigInfo;
        }
        return null;
    }

    /**
     * 初始化 -> BaseApplication中调用
     * * 基础组件配置
     */
    public static void init() {
        if (sConfigInfo == null) {
            throw new NullPointerException("ConfigInfo is null");
        }
        Context context = FrameApplication.instance().getApplicationContext();
        //------------------------------------Toaster配置---------------------------------
        Toaster.DEBUG = sConfigInfo.TOAST_DEBUG_OPEN;
        Toaster.TOAST_DURATION = sConfigInfo.TOAST_DURATION;
        Toaster.NEED_WAIT = sConfigInfo.TOAST_NEED_WAIT;
        // * 必须设置,否则无法使用
        Toaster.setContext(context);
        //------------------------------------Logger配置---------------------------------
        Logger.APP_PKG_NAME = context.getPackageName();
        Logger.LOG_OPEN = sConfigInfo.LOG_OPEN;
        Logger.LOG_LEVEL = sConfigInfo.LOG_LEVEL;
        Logger.LOGFILE_PATH = sConfigInfo.LOG_DIR;
        //------------------------------------SpTool配置---------------------------------
        // 设置全局Sp实例,项目启动时创建,并通过getCommonSp拿到,项目中只有一份实例
        SpManager.CommonSp = sConfigInfo.spAll;
        // 加解密回调 - 不设置或null表示不进行加解密处理
        SpManager.setEncodeDecodeCallback(new SpEncodeDecodeCallback() {
            @Override
            public String encode(String pStr) {
                String result = null;
                try {
                    result = EncodeDecode.encode(pStr);
                } catch (IOException pE) {
                    pE.printStackTrace();
                }
                return result;
            }

            @Override
            public String decode(String pStr) {
                String result = null;
                try {
                    result = EncodeDecode.decode(pStr);
                } catch (IOException pE) {
                    pE.printStackTrace();
                } catch (ClassNotFoundException pE) {
                    pE.printStackTrace();
                }
                return result;
            }
        });
        // * 必须设置,否则无法使用
        SpManager.setContext(context);
        //------------------------------------Retrofit配置---------------------------------
        // * 必须设置,否则无法使用
        HttpManager.INSTANCE.context = context;
        // * 必须设置,否则无法使用
        HttpManager.INSTANCE.base_url = sConfigInfo.BASE_URL;
        HttpManager.INSTANCE.net_log_tag = sConfigInfo.NET_LOG_TAG;
        HttpManager.INSTANCE.net_log_details = sConfigInfo.NET_LOG_DETAILS;
        HttpManager.INSTANCE.net_log_details_all = sConfigInfo.NET_LOG_DETAILS_All;
        HttpManager.INSTANCE.noformbody_canaddbody = sConfigInfo.NOFORMBODY_CANADDBODY;
        HttpManager.INSTANCE.net_cache_dir = new File(FrameApplication.instance().getCacheDir(), "NetCache");
        HttpManager.INSTANCE.net_cache_type = sConfigInfo.NET_CACHE_TYPE;
        HttpManager.INSTANCE.net_cache_size = sConfigInfo.NET_CACHE_SIZE;
        HttpManager.INSTANCE.connect_timeout = sConfigInfo.CONNECT_TIMEOUT;
        HttpManager.INSTANCE.read_timeout = sConfigInfo.READ_TIMEOUT;
        HttpManager.INSTANCE.write_timeout = sConfigInfo.WRITE_TIMEOUT;
        //------------------------------------Glide配置---------------------------------
        GlideManager.DECODEFORMAT = DecodeFormat.PREFER_ARGB_8888;
        GlideManager.IMAGE_CACHE_PATH = FileUtils.imgCacheDirPath;
        GlideManager.MAIN_CACHE_DIR = "MainCache";
        GlideManager.MAX_DISK_CACHE_SIZE = 50;
        //------------------------------------FrescoView配置---------------------------------
        // * 必须设置,否则无法使用
        FrescoManager.setContext(context);
        // 设置Log开关 & 等级 - * 默认为 开 & Log.VERBOSE
        FrescoManager.setOpen(sConfigInfo.LOG_OPEN, sConfigInfo.LOG_LEVEL.intValue());
        // 设置缓存图片的存放路径
        // Environment.getExternalStorageDirectory().getAbsolutePath() + "/FrescoView/"
        // @param cachePath     路径:默认为SD卡根目录FrescoView下 (此路径非直接存储图片的路径,还需要以下目录设置)
        // @param mainCacheDir  大图片存放目录:默认为MainCache目录
        // @param smallCacheDir 小图片存放目录:默认为SmallCache目录 (如不想区分大小图片,可设置为null或者"",表示大小图片都放在mainCacheDir目录下)
        FrescoManager.setCachePath(FileUtils.imgCacheDirPath, "MainCache", "SmallCache");
        // 设置缓存磁盘大小
        // @param mainCacheSize  大图片磁盘大小(MB) 默认为50MB
        // @param smallCacheSize 小图片磁盘大小(MB) 默认为20MB
        FrescoManager.setCacheSize(50, 20);
        // 初始化 -> 配置完成后必须调用此函数生效
        FrescoManager.init();
    }
}
