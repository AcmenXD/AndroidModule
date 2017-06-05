package com.acmenxd.frame.utils;

import com.acmenxd.frame.basis.FrameApplication;
import com.acmenxd.logger.Logger;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/6/2 15:35
 * @detail something
 */
public class CrashUtils {
    public static class CrashManager implements Thread.UncaughtExceptionHandler {
        public static final String TAG = "CrashManager";

        private static CrashManager instance;
        // 程序的Context对象
        private FrameApplication mApplication;
        // 系统默认的UncaughtException处理类
        private Thread.UncaughtExceptionHandler mDefaultHandler;

        /**
         * 获取CrashHandler实例 ,单例模式
         */
        public static CrashManager getInstance(FrameApplication pApplication) {
            if (instance == null) {
                synchronized (CrashManager.class) {
                    instance = new CrashManager(pApplication);
                }
            }
            return instance;
        }

        /**
         * 保证只有一个CrashHandler实例
         */
        private CrashManager(FrameApplication pApplication) {
            mApplication = pApplication;
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(this);
        }

        /**
         * 当UncaughtException发生时会转入该函数来处理
         */
        @Override
        public void uncaughtException(Thread pThread, Throwable pE) {
            try {
                mApplication.crashException("填入项目信息", pThread, pE);
            } catch (Exception e) {
                Logger.e(e, "crash is error!");
            } finally {
                mDefaultHandler.uncaughtException(pThread, pE);
            }
        }
    }


}
