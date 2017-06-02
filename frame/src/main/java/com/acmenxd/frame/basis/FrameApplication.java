package com.acmenxd.frame.basis;

import android.app.Application;
import android.content.res.Configuration;

import com.acmenxd.frame.configs.BaseConfig;
import com.acmenxd.frame.configs.ConfigBuilder;
import com.acmenxd.frame.configs.FrameNetCode;
import com.acmenxd.frame.utils.FileUtils;
import com.acmenxd.frame.utils.net.Monitor;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:36
 * @detail Application基类
 */
public class FrameApplication extends Application {
    protected final String TAG = this.getClass().getSimpleName();

    // 单例实例
    private static FrameApplication sInstance = null;

    public FrameApplication() {
        sInstance = this;
    }

    public static synchronized FrameApplication instance() {
        if (sInstance == null) {
            new RuntimeException("FrameApplication == null ?? you should extends FrameApplication in you app");
        }
        return sInstance;
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
     * 初始化配置
     */
    public void initFrameSetting(Class<? extends BaseConfig> pConfig, boolean isDebug, FrameNetCode.Parse pParse) {
        // 创建配置Info
        ConfigBuilder.createConfig(pConfig, isDebug, pParse);
        // 初始化File配置
        FileUtils.init();
        // 初始化网络监听配置
        Monitor.init();
        // 初始化模块配置
        ConfigBuilder.init();
    }

    /**
     * 获取配置详情
     */
    public <T extends BaseConfig> T getConfig() {
        return ConfigBuilder.getConfigInfo();
    }

    /**
     * 退出应用程序
     */
    public void exit() {
        ActivityStackManager.INSTANCE.exit();
    }
}
