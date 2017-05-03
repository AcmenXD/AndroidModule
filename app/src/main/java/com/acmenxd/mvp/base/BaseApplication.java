package com.acmenxd.mvp.base;

import android.app.Application;
import android.content.res.Configuration;

import com.acmenxd.logger.Logger;
import com.acmenxd.mvp.MyEventBusIndex;
import com.acmenxd.mvp.configs.AppConfig;
import com.acmenxd.mvp.configs.BaseConfig;
import com.acmenxd.mvp.db.helper.DBManager;
import com.acmenxd.mvp.utils.FileUtils;
import com.acmenxd.mvp.utils.net.Monitor;

import org.greenrobot.eventbus.EventBus;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:36
 * @detail 顶级Application
 */
public final class BaseApplication extends Application {
    protected final String TAG = this.getClass().getSimpleName();

    // 单例实例
    private static BaseApplication sInstance = null;
    // 初始化状态 -> 默认false,初始化完成为true
    public boolean isInitFinish = false;
    // 记录启动时间
    public long startTime = 0;

    public BaseApplication() {
        super();
        sInstance = this;
    }

    public static synchronized BaseApplication instance() {
        if (sInstance == null) {
            new RuntimeException("BaseApplication == null ?? you should extends BaseApplication in you app");
        }
        return sInstance;
    }

    @Override
    public void onCreate() {
        Logger.w("App已启动!");
        startTime = System.currentTimeMillis();

        // 程序创建的时候执行
        super.onCreate();
        // 初始化File配置
        FileUtils.init();
        // 初始化App配置
        AppConfig.init();
        // 初始化模块配置
        BaseConfig.init();
        // 初始化网络监听配置
        Monitor.init();
        // 初始化数据库配置
        DBManager.getInstance().init();
        // 初始化EventBus配置 -> 启用3.0加速功能
        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();

        // 初始化完毕
        isInitFinish = true;
        Logger.w("App初始化完成!");
    }


    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        super.onTerminate();
        // 终止网络监听
        Monitor.release();
    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 应用配置变更~
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 退出应用程序
     */
    public void exit() {
        ActivityStackManager.INSTANCE.exit();
    }
}
