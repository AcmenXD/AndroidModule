package com.acmenxd.mvp.base;

import com.acmenxd.frame.basis.FrameApplication;
import com.acmenxd.frame.configs.MvpConfig;
import com.acmenxd.logger.Logger;
import com.acmenxd.mvp.MyEventBusIndex;
import com.acmenxd.mvp.db.core.DBManager;
import com.acmenxd.mvp.net.NetCode;

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
        initFrameSetting(MvpConfig.class, AppConfig.DEBUG, new NetCode());
        // 存储项目整体配置信息
        AppConfig.config = getConfig();
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
        sb.append("ProjectName").append(" = ").append(AppConfig.PROJECT_NAME).append("\n");
        sb.append("PackageName").append(" = ").append(AppConfig.PKG_NAME).append("\n");
        sb.append("VersionCode").append(" = ").append(AppConfig.VERSION_CODE).append("\n");
        sb.append("VersionName").append(" = ").append(AppConfig.VERSION_NAME).append("\n");
        sb.append("ThreadName").append(" = ").append(pThread.getName()).append("\n");
        super.crashException(sb.toString(), pThread, pE);
    }
}
