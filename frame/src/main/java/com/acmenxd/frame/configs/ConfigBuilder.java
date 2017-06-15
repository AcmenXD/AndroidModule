package com.acmenxd.frame.configs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.acmenxd.frame.basis.FrameApplication;
import com.acmenxd.frame.utils.FileUtils;
import com.acmenxd.frame.utils.code.EncodeDecode;
import com.acmenxd.frescoview.FrescoManager;
import com.acmenxd.glide.GlideManager;
import com.acmenxd.logger.Logger;
import com.acmenxd.retrofit.NetCodeUtils;
import com.acmenxd.retrofit.NetManager;
import com.acmenxd.retrofit.exception.NetException;
import com.acmenxd.sptool.SpEncodeDecodeCallback;
import com.acmenxd.sptool.SpManager;
import com.acmenxd.toaster.Toaster;
import com.bumptech.glide.load.DecodeFormat;

import java.io.File;
import java.io.IOException;
import java.util.Map;

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
    public static void createConfig(@NonNull Class<? extends BaseConfig> pConfig, @NonNull boolean isDebug, @NonNull FrameNetCode.Parse pParse) {
        // 配置Retrofit NetCode
        FrameNetCode.setNetCode(pParse);
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
     * 设置Net公共参数 -> 为动态配置而设置的此函数
     */
    protected static void setNetMaps(@NonNull Map<String, String> ParameterMaps,
                                     @NonNull Map<String, String> HeaderMaps,
                                     @NonNull Map<String, String> HeaderMaps2,
                                     @NonNull Map<String, String> BodyMaps) {
        NetManager.INSTANCE.getBuilder().setParameterMaps(ParameterMaps);
        NetManager.INSTANCE.getBuilder().setHeaderMaps(HeaderMaps);
        NetManager.INSTANCE.getBuilder().setHeaderMaps2(HeaderMaps2);
        NetManager.INSTANCE.getBuilder().setBodyMaps(BodyMaps);
    }

    /**
     * 初始化 -> BaseApplication中调用
     * * 基础组件配置
     */
    public static synchronized void init() {
        if (sConfigInfo == null) {
            throw new NullPointerException("ConfigInfo is null");
        }
        Context context = FrameApplication.instance().getApplicationContext();
        //------------------------------------Logger配置---------------------------------
        /**
         * 初始化
         * context必须设置
         */
        Logger.setContext(context);
        /**
         * 设置Log开关,可根据debug-release配置
         *  默认为true
         */
        Logger.setOpen(sConfigInfo.LOG_OPEN);
        /**
         * 设置Log等级, >= 这个配置的log才会显示
         *  默认为LogType.V
         */
        Logger.setLevel(Log.VERBOSE);
        /**
         * 设置本地Log日志的存储路径
         *  默认为sd卡Logger目录下
         * Environment.getExternalStorageDirectory().getAbsolutePath() + "/Logger/"
         */
        Logger.setPath(sConfigInfo.LOG_DIR);

        //------------------------------------Toaster配置---------------------------------
        /**
         * 设置Context对象
         * * 必须设置,否则无法使用
         */
        Toaster.setContext(context);
        /**
         * 设置debug开关,可根据debug-release配置
         * 默认为true
         */
        Toaster.setDebugOpen(sConfigInfo.TOAST_DEBUG_OPEN);
        /**
         * 设置默认显示时长
         * 默认为ToastD.SHORT = Toast.LENGTH_SHORT
         */
        Toaster.setDefaultDuration(sConfigInfo.TOAST_DURATION);
        /**
         * 设置Toaster显示方式 :  |
         * 默认为ToastNW.NEED_WAIT(Toast需要等待,并逐个显示) 可设置为:ToastNW.No_NEED_WAIT(Toast无需等待,直接显示)
         */
        Toaster.setNeedWait(sConfigInfo.TOAST_NEED_WAIT);

        //------------------------------------SpTool配置---------------------------------
        /**
         * 初始化
         * context必须设置
         */
        SpManager.setContext(context);
        /**
         * 设置全局Sp实例,项目启动时创建,并通过getCommonSp拿到,项目中只有一份实例
         */
        SpManager.setCommonSp(sConfigInfo.spAll);
        /**
         * 设置加解密回调
         * * 不设置或null表示不进行加解密处理
         */
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
        /**
         * 初始化 -> 配置完成后必须调用此函数生效
         */
        SpManager.init();

        //------------------------------------Glide配置---------------------------------
        /**
         * 设置缓存磁盘大小
         *
         * @param mainCacheSize  大图片磁盘大小(MB) 默认为50MB
         */
        GlideManager.setCacheSize(50);
        /**
         * 设置缓存图片的存放路径
         * Environment.getExternalStorageDirectory().getAbsolutePath() + "/Glide/"
         *
         * @param cachePath     路径:默认为SD卡根目录Glide下 (此路径非直接存储图片的路径,还需要以下目录设置)
         * @param mainCacheDir  大图片存放目录:默认为MainCache目录
         */
        GlideManager.setCachePath(FileUtils.imgCacheDirPath, "MainCache");
        /**
         * 设置图片解码格式
         *
         * @param decodeFormat 默认:DecodeFormat.PREFER_RGB_565
         */
        GlideManager.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

        //------------------------------------FrescoView配置---------------------------------
        /**
         * 初始化
         * context必须设置
         */
        FrescoManager.setContext(context);
        /**
         * 设置Log开关 & 等级
         * * 默认为 开 & Log.VERBOSE
         */
        FrescoManager.setOpen(sConfigInfo.LOG_OPEN, sConfigInfo.LOG_LEVEL.intValue());
        /**
         * 设置缓存图片的存放路径
         * Environment.getExternalStorageDirectory().getAbsolutePath() + "/FrescoView/"
         *
         * @param cachePath     路径:默认为SD卡根目录FrescoView下 (此路径非直接存储图片的路径,还需要以下目录设置)
         * @param mainCacheDir  大图片存放目录:默认为MainCache目录
         * @param smallCacheDir 小图片存放目录:默认为SmallCache目录 (如不想区分大小图片,可设置为null或者"",表示大小图片都放在mainCacheDir目录下)
         */
        FrescoManager.setCachePath(FileUtils.imgCacheDirPath, "MainCache", "SmallCache");
        /**
         * 设置缓存磁盘大小
         *
         * @param mainCacheSize  大图片磁盘大小(MB) 默认为50MB
         * @param smallCacheSize 小图片磁盘大小(MB) 默认为20MB
         */
        FrescoManager.setCacheSize(50, 20);
        /**
         * 初始化 -> 配置完成后必须调用此函数生效
         */
        FrescoManager.init();

        //------------------------------------Retrofit配置---------------------------------
        NetCodeUtils.startParseNetCode parseNetCode = new NetCodeUtils.startParseNetCode() {
            @Override
            public NetException parse(int code, String msg) {
                return FrameNetCode.frameParseNetCode(code, msg);
            }
        };
        NetManager.newBuilder()
                .setContext(context)// 上下文对象(*必须设置)
                .setParseNetCode(parseNetCode)// 统一处理NetCode回调(如不设置则不会处理NetCode)
                .setBase_url(sConfigInfo.BASE_URL)// 基础URL地址(*必须设置)
                .setNet_log_open(sConfigInfo.NET_LOG_OPEN)// Net Log 的开关
                .setNet_log_level(sConfigInfo.NET_LOG_LEVEL) // Net Log 的日志级别
                .setNet_log_tag(sConfigInfo.NET_LOG_TAG) // Net Log 的日志Tag
                .setNet_log_details(sConfigInfo.NET_LOG_DETAILS)// Net Log 的日志显示形式 -> 是否显示 "请求头 请求体 响应头 错误日志" 等详情
                .setNet_log_details_all(sConfigInfo.NET_LOG_DETAILS_All)// Net Log 的日志显示形式 -> 是否显示请求过程中的日志,包含详细的请求头日志
                .setNet_cache_dir(new File(FrameApplication.instance().getCacheDir(), "NetCache"))  // 网络缓存默认存储路径
                .setNet_cache_type(sConfigInfo.NET_CACHE_TYPE) // 网络缓存策略: 0->不启用缓存  1->遵从服务器缓存配置
                .setNet_cache_size(sConfigInfo.NET_CACHE_SIZE) // 网络缓存大小(MB)
                .setConnect_timeout(sConfigInfo.CONNECT_TIMEOUT)  // 网络连接超时时间(秒)
                .setRead_timeout(sConfigInfo.READ_TIMEOUT) // 读取超时时间(秒)
                .setWrite_timeout(sConfigInfo.WRITE_TIMEOUT)  // 写入超时时间(秒)
                .setNoformbody_canaddbody(sConfigInfo.NOFORMBODY_CANADDBODY) // 非Form表单形式的请求体,是否加入公共Body
                .build();
    }
}
