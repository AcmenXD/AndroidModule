package com.acmenxd.core.base;

import android.content.Context;

import com.acmenxd.core.MyEventBusIndex;
import com.acmenxd.core.db.core.DBManager;
import com.acmenxd.frame.basis.FrameApplication;
import com.acmenxd.frame.utils.FileUtils;
import com.acmenxd.frescoview.FrescoManager;
import com.acmenxd.glide.GlideManager;
import com.acmenxd.logger.Logger;
import com.bumptech.glide.load.DecodeFormat;

import org.greenrobot.eventbus.EventBus;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/24 14:54
 * @detail 顶级Application
 */
public final class BaseApplication extends FrameApplication {
    // 初始化状态 -> 默认false,初始化完成为true
    public boolean isInitFinish = false;
    // 记录启动时间
    public long startTime = 0;

    public static synchronized BaseApplication instance() {
        return (BaseApplication) FrameApplication.instance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.w("App已启动!");
        startTime = System.currentTimeMillis();

        // 配置框架设置
        initFrameSetting(AppFrameConfig.class, AppConfig.DEBUG);
        initBaseSetting();

        // 初始化App配置
        AppConfig.init();

        // 初始化数据库配置
        DBManager.getInstance().init();
        // 初始化EventBus配置 -> 启用3.0加速功能
        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();

        // 初始化完毕
        isInitFinish = true;
        Logger.w("App初始化完成!");
    }

    @Override
    public void crashException(String projectInformation, Thread pThread, Throwable pE) {
        StringBuffer sb = new StringBuffer();
        sb.append("Debug").append(" = ").append(AppConfig.DEBUG).append("\n");
        sb.append("Imei").append(" = ").append(AppConfig.IMEI).append("\n");
        sb.append("Market").append(" = ").append(AppConfig.MARKET).append("\n");
        sb.append("PackageName").append(" = ").append(AppConfig.PKG_NAME).append("\n");
        sb.append("ProjectName").append(" = ").append(AppConfig.PROJECT_NAME).append("\n");
        sb.append("VersionCode").append(" = ").append(AppConfig.VERSION_CODE).append("\n");
        sb.append("VersionName").append(" = ").append(AppConfig.VERSION_NAME).append("\n");
        sb.append("ThreadName").append(" = ").append(pThread.getName()).append("\n");
        super.crashException(sb.toString(), pThread, pE);
    }

    /**
     * 初始化配置
     */
    public void initBaseSetting() {
        Context context = getApplicationContext();
        //------------------------------------Glide配置---------------------------------
        GlideManager.DECODEFORMAT = DecodeFormat.PREFER_ARGB_8888;
        GlideManager.IMAGE_CACHE_PATH = FileUtils.imgCacheDirPath;
        GlideManager.MAIN_CACHE_DIR = "MainCache";
        GlideManager.MAX_DISK_CACHE_SIZE = 50;
        //------------------------------------FrescoView配置---------------------------------
        /**
         * 设置Log开关 & 等级
         * * 默认为 开 & Log.VERBOSE
         */
        FrescoManager.LOG_OPEN = AppConfig.DEBUG;
        FrescoManager.LOG_LEVEL = getConfig().LOG_LEVEL.intValue();
        /**
         * 设置缓存图片的存放路径
         * Environment.getExternalStorageDirectory().getAbsolutePath() + "/FrescoView/"
         * * 路径:默认为SD卡根目录FrescoView下 (此路径非直接存储图片的路径,还需要以下目录设置)
         * * 大图片存放目录:默认为MainCache目录
         * * 小图片存放目录:默认为SmallCache目录 (如不想区分大小图片,可设置为null或者"",表示大小图片都放在mainCacheDir目录下)
         */
        FrescoManager.IMAGE_CACHE_PATH = FileUtils.imgCacheDirPath;
        FrescoManager.MAIN_CACHE_DIR = "MainCache";
        FrescoManager.SMALL_CACHE_DIR = "SmallCache";
        /**
         * 设置缓存磁盘大小
         * * mainCacheSize  大图片磁盘大小(MB) 默认为50MB
         * * smallCacheSize 小图片磁盘大小(MB) 默认为20MB
         */
        FrescoManager.MAX_DISK_CACHE_SIZE = 50;
        FrescoManager.MAX_SMALL_DISK_LOW_CACHE_SIZE = 20;
        // * 必须设置,否则无法使用
        FrescoManager.init(context);
    }
}
