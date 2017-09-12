package com.acmenxd.frame.utils;

import android.support.annotation.NonNull;

import com.acmenxd.frame.basis.FrameApplication;
import com.acmenxd.logger.Logger;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/6/2 15:35
 * @detail 抓取蹦极日志工具类
 */
public final class CrashUtils implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashUtils";

    private static CrashUtils instance;
    // 程序的Context对象
    private FrameApplication mApplication;
    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashUtils getInstance(@NonNull FrameApplication pApplication) {
        if (instance == null) {
            synchronized (CrashUtils.class) {
                instance = new CrashUtils(pApplication);
            }
        }
        return instance;
    }

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashUtils(@NonNull FrameApplication pApplication) {
        mApplication = pApplication;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(@NonNull Thread pThread, @NonNull Throwable pE) {
        try {
            mApplication.crashException("填入项目信息", pThread, pE);
        } catch (Exception e) {
            Logger.e(e, "crash is error!");
        } finally {
            mDefaultHandler.uncaughtException(pThread, pE);
        }
    }
}
