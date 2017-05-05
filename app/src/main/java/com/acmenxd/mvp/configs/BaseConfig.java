package com.acmenxd.mvp.configs;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.acmenxd.frescoview.FrescoManager;
import com.acmenxd.glide.GlideManager;
import com.acmenxd.logger.LogType;
import com.acmenxd.logger.Logger;
import com.acmenxd.retrofit.NetCodeUtils;
import com.acmenxd.retrofit.NetManager;
import com.acmenxd.retrofit.exception.NetException;
import com.acmenxd.sptool.SpEncodeDecodeCallback;
import com.acmenxd.sptool.SpManager;
import com.acmenxd.toaster.ToastD;
import com.acmenxd.toaster.ToastNW;
import com.acmenxd.toaster.Toaster;
import com.acmenxd.mvp.BuildConfig;
import com.acmenxd.mvp.base.BaseApplication;
import com.acmenxd.mvp.utils.code.EncodeDecode;
import com.acmenxd.mvp.utils.FileUtils;
import com.acmenxd.mvp.net.NetCode;
import com.bumptech.glide.load.DecodeFormat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/15 16:01
 * @detail 构建配置
 */
public final class BaseConfig {
    /**
     * 调试模式开关
     * * 由build.gradle -> debug | release版本控制
     */
    public static final boolean DEBUG = BuildConfig.MAIN_DEBUG;

    /**
     * 默认渠道号
     */
    public static final String DEFAULT_MARKET = "AcmenXD";

    /**
     * 数据库 配置
     */
    // 数据库名称
    public static final String DB_NAME = "acmen_db";

    /**
     * File 存储配置
     * BASE_DIR 路径下的自定义文件夹会自动创建
     * * 详情参考FileUtils.init();
     */
    public static final String SDCARD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String BASE_DIR = SDCARD_DIR + "/AcmenXD/";

    /**
     * Log 配置
     */
    // Log开关
    public static final boolean LOG_OPEN = DEBUG;
    // Log显示等级, >= LOG_LEVEL的log显示
    public static final LogType LOG_LEVEL = LogType.V;
    // Log日志默认保存路径
    public static final String LOG_DIR = BASE_DIR + "Logger/";

    /**
     * Toast 配置
     * * Toast 有Debug模式,正式上线版本将不会显示debug模式下的Toast
     */
    // Toast调试开关
    public static final boolean TOAST_DEBUG_OPEN = DEBUG;
    // Toast默认显示时长
    public static final ToastD TOAST_DURATION = ToastD.SHORT;
    // Toast显示方式 : Toast需要等待,并逐个显示 | Toast无需等待,直接显示
    public static final ToastNW TOAST_NEED_WAIT = ToastNW.NEED_WAIT;

    /**
     * SharedPreferences 配置
     */
    public static final String SP_Device = "spDevice";
    public static final String SP_User = "spUser";
    public static final String SP_Config = "spConfig";
    public static final String SP_Cookie = "spCookie";
    public static final String[] spAll = {SP_Device, SP_User, SP_Config, SP_Cookie};

    /**
     * Net 配置
     */
    // 请求地址配置 -1:正式版  0->预发布  1->测试1  2->测试2  3->测试3
    public static final byte URL_Type = 1;
    // 基础url配置
    public static final String BASE_URL;

    // 配置连接地址
    static {
        switch (URL_Type) {
            case -1:
                //正式版
                BASE_URL = "http://server.jeasonlzy.com/OkHttpUtils/";
                break;
            case 0:
                //预发布
                BASE_URL = "http://server.jeasonlzy.com/OkHttpUtils/";
                break;
            case 1:
                //测试1
                BASE_URL = "http://server.jeasonlzy.com/OkHttpUtils/";
                break;
            case 2:
                //测试2
                BASE_URL = "http://server.jeasonlzy.com/OkHttpUtils/";
                break;
            default:
                BASE_URL = "http://server.jeasonlzy.com/OkHttpUtils/";
                break;
        }
    }

    // Net Log 的开关
    public static final boolean NET_LOG_OPEN = DEBUG;
    // Net Log 的日志级别
    public static final int NET_LOG_LEVEL = Log.WARN;
    // Net Log 的日志Tag
    public static final String NET_LOG_TAG = "NetLog";
    // Net Log 的日志显示形式 -> 是否显示 "请求头 请求体 响应头 错误日志" 等详情
    public static final boolean NET_LOG_DETAILS = true;
    // Net Log 的日志显示形式 -> 是否显示请求过程中的日志,包含详细的请求头日志
    public static final boolean NET_LOG_DETAILS_All = false;
    // 网络缓存策略: 0->不启用缓存  1->遵从服务器缓存配置
    public static final int NET_CACHE_TYPE = 1;
    // 网络缓存大小(MB)
    public static final int NET_CACHE_SIZE = 10;
    // 网络连接超时时间(秒)
    public static final int CONNECT_TIMEOUT = 30;
    // 读取超时时间(秒)
    public static final int READ_TIMEOUT = 30;
    // 写入超时时间(秒)
    public static final int WRITE_TIMEOUT = 30;
    // 非Form表单形式的请求体,是否加入公共Body
    public static final boolean NOFORMBODY_CANADDBODY = false;
    // 公共请求参数
    public static final Map<String, String> ParameterMaps = new HashMap<>();
    // 公共Header(不允许相同Key值存在)
    public static final Map<String, String> HeaderMaps = new HashMap<>();
    // 公共Header(允许相同Key值存在)
    public static final Map<String, String> HeaderMaps2 = new HashMap<>();
    // 公共Body
    public static final Map<String, String> BodyMaps = new HashMap<>();

    // 配置请求公共需求
    static {
        ParameterMaps.put("parameter_key_1", "parameter_value_1");
        ParameterMaps.put("parameter_key_2", "parameter_value_2");
        HeaderMaps.put("header_key_1", "header_value_1");
        HeaderMaps.put("header_key_2", "header_value_2");
        BodyMaps.put("body_key_1", "body_value_1");
        BodyMaps.put("body_key_2", "body_value_2");
    }


    /**
     * 初始化 -> BaseApplication中调用
     * * 基础组件配置
     */
    public static final synchronized void init() {
        Context context = BaseApplication.instance().getApplicationContext();
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
        Logger.setOpen(LOG_OPEN);
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
        Logger.setPath(LOG_DIR);

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
        Toaster.setDebugOpen(TOAST_DEBUG_OPEN);
        /**
         * 设置默认显示时长
         * 默认为ToastD.SHORT = Toast.LENGTH_SHORT
         */
        Toaster.setDefaultDuration(TOAST_DURATION);
        /**
         * 设置Toaster显示方式 :  |
         * 默认为ToastNW.NEED_WAIT(Toast需要等待,并逐个显示) 可设置为:ToastNW.No_NEED_WAIT(Toast无需等待,直接显示)
         */
        Toaster.setNeedWait(TOAST_NEED_WAIT);

        //------------------------------------SpTool配置---------------------------------
        /**
         * 初始化
         * context必须设置
         */
        SpManager.setContext(context);
        /**
         * 设置全局Sp实例,项目启动时创建,并通过getCommonSp拿到,项目中只有一份实例
         */
        SpManager.setCommonSp(spAll);
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
        FrescoManager.setOpen(LOG_OPEN, LOG_LEVEL.intValue());
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
                return NetCode.parseNetCode(code, msg);
            }
        };
        NetManager.newBuilder()
                .setContext(context)// 上下文对象(*必须设置)
                .setParseNetCode(parseNetCode)// 统一处理NetCode回调(如不设置则不会处理NetCode)
                .setBase_url(BASE_URL)// 基础URL地址(*必须设置)
                .setNet_log_open(NET_LOG_OPEN)// Net Log 的开关
                .setNet_log_level(NET_LOG_LEVEL) // Net Log 的日志级别
                .setNet_log_tag(NET_LOG_TAG) // Net Log 的日志Tag
                .setNet_log_details(NET_LOG_DETAILS)// Net Log 的日志显示形式 -> 是否显示 "请求头 请求体 响应头 错误日志" 等详情
                .setNet_log_details_all(NET_LOG_DETAILS_All)// Net Log 的日志显示形式 -> 是否显示请求过程中的日志,包含详细的请求头日志
                .setNet_cache_dir(new File(BaseApplication.instance().getCacheDir(), "NetCache"))  // 网络缓存默认存储路径
                .setNet_cache_type(NET_CACHE_TYPE) // 网络缓存策略: 0->不启用缓存  1->遵从服务器缓存配置
                .setNet_cache_size(NET_CACHE_SIZE) // 网络缓存大小(MB)
                .setConnect_timeout(CONNECT_TIMEOUT)  // 网络连接超时时间(秒)
                .setRead_timeout(READ_TIMEOUT) // 读取超时时间(秒)
                .setWrite_timeout(WRITE_TIMEOUT)  // 写入超时时间(秒)
                .setNoformbody_canaddbody(NOFORMBODY_CANADDBODY) // 非Form表单形式的请求体,是否加入公共Body
                .setParameterMaps(ParameterMaps) // 公共请求参数
                .setHeaderMaps(HeaderMaps)  // 公共Header(不允许相同Key值存在)
                .setHeaderMaps2(HeaderMaps2)  // 公共Header(允许相同Key值存在)
                .setBodyMaps(BodyMaps)// 公共Body
                .build();
    }
}
