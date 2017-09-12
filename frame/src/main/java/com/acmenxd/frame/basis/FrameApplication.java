package com.acmenxd.frame.basis;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Debug;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.acmenxd.frame.configs.BaseConfig;
import com.acmenxd.frame.configs.ConfigBuilder;
import com.acmenxd.frame.utils.CrashUtils;
import com.acmenxd.frame.utils.DateUtils;
import com.acmenxd.frame.utils.FileUtils;
import com.acmenxd.frame.utils.net.Monitor;
import com.acmenxd.logger.LogTag;
import com.acmenxd.logger.Logger;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:36
 * @detail Application基类
 */
public abstract class FrameApplication extends Application {
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

    @CallSuper
    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        super.onTerminate();
        // 终止网络监听
        Monitor.release();
    }

    @CallSuper
    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        super.onLowMemory();
    }

    @CallSuper
    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        super.onTrimMemory(level);
    }

    @CallSuper
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 应用配置变更~
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 初始化配置
     */
    public final void initFrameSetting(@NonNull Class<? extends BaseConfig> pConfig, @NonNull boolean isDebug) {
        // 创建配置Info
        ConfigBuilder.createConfig(pConfig, isDebug);
        // 初始化模块配置
        ConfigBuilder.init();
        // 初始化File配置
        FileUtils.init();
        // 初始化网络监听配置
        Monitor.init();
        // 崩溃异常捕获
        CrashUtils.getInstance(instance());
    }

    /**
     * 获取配置详情
     */
    public final <T extends BaseConfig> T getConfig() {
        return ConfigBuilder.getConfigInfo();
    }

    /**
     * 退出应用程序
     */
    public final void exit() {
        ActivityStackManager.INSTANCE.exit();
    }

    /**
     * 程序发生崩溃异常时回调
     */
    @CallSuper
    public void crashException(@NonNull String projectInformation, @NonNull Thread pThread, @NonNull Throwable pE) {
        String fileName = "crash-" + DateUtils.nowDate(DateUtils.yMdHms2) + ".txt";
        StringBuffer sb = new StringBuffer();
        sb.append("项目信息============================================\n");
        sb.append(projectInformation);
        sb.append("\n机型信息============================================\n");
        Field[] fields = Build.class.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                sb.append(field.getName()).append(" = ").append(field.get("").toString()).append("\n");
            }
        } catch (IllegalAccessException pE1) {
            Logger.e("crashException:" + pE1.getMessage());
        }
        Logger.file(LogTag.mk("crashException"), fileName, pE, sb.toString());
        // 内存溢出类型崩溃,生成.hprof文件 - 由于文件较大,会造成app崩溃时卡顿,所以暂时关闭此功能
        // crashOutOfMemory(pE, fileName.replace(".txt", ".hprof"));
    }

    /**
     * 保存内存溢出日志文件
     */
    private final void crashOutOfMemory(@NonNull Throwable pE, @NonNull String fileName) {
        boolean result = false;
        if (OutOfMemoryError.class.equals(pE.getClass())) {
            result = true;
        } else {
            Throwable cause = pE.getCause();
            while (null != cause) {
                if (OutOfMemoryError.class.equals(cause.getClass())) {
                    result = true;
                }
                cause = cause.getCause();
            }
        }
        if (result) {
            try {
                Debug.dumpHprofData(getConfig().LOG_DIR + fileName);
            } catch (IOException pE1) {
                Logger.e("crashException:" + pE1.getMessage());
            }
        }
    }
}
