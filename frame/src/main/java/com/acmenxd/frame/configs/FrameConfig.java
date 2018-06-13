package com.acmenxd.frame.configs;

import android.os.Environment;
import android.support.annotation.CallSuper;

import com.acmenxd.logger.LogTag;
import com.acmenxd.logger.LogType;
import com.acmenxd.toaster.ToastDuration;
import com.acmenxd.toaster.ToastNW;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/25 10:02
 * @detail 配置详细参数类
 */
public class FrameConfig {
    /**
     * 初始化
     */
    @CallSuper
    protected void init(boolean isDebug) {
        DEBUG = isDebug;
        LOG_OPEN = isDebug;
        TOAST_DEBUG_OPEN = isDebug;
    }

    /**
     * 调试模式开关
     * * 由build.gradle -> debug | release版本控制
     */
    protected boolean DEBUG = false;

    /**
     * 数据库 配置
     */
    // 数据库名称
    public String DB_NAME = "appframe_db";

    /**
     * File 存储配置
     * BASE_DIR 路径下的自定义文件夹会自动创建
     * * 详情参考FileUtils.init();
     */
    public String SDCARD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    // 项目主路径
    public String BASE_DIR = SDCARD_DIR + "/AppFrame/";
    // Log日志默认保存路径
    public String LOG_DIR = BASE_DIR + "Logger/";

    /**
     * SharedPreferences 配置
     */
    public String SP_Cookie = "spCookie";
    protected String[] spAll = new String[]{SP_Cookie};

    /**
     * Log 配置
     */
    // Log显示等级, >= LOG_LEVEL的log显示
    public LogType LOG_LEVEL = LogType.V;
    // Log开关
    protected boolean LOG_OPEN = DEBUG;

    /**
     * Toast 配置
     * * Toast 有Debug模式,正式上线版本将不会显示debug模式下的Toast
     */
    // Toast调试开关
    protected boolean TOAST_DEBUG_OPEN = DEBUG;
    // Toast默认显示时长
    protected ToastDuration TOAST_DURATION = ToastDuration.SHORT;
    // Toast显示方式 : Toast需要等待,并逐个显示 | Toast无需等待,直接显示
    protected ToastNW TOAST_NEED_WAIT = ToastNW.NEED_WAIT;

    /**
     * Net 配置
     */
    // 基础url配置
    protected String BASE_URL = "";
    // Net Log 的日志Tag
    protected LogTag NET_LOG_TAG = LogTag.mk("NetLog");
    // Net Log 的日志显示形式 -> 是否显示 "请求头 请求体 响应头 错误日志" 等详情
    protected boolean NET_LOG_DETAILS = true;
    // Net Log 的日志显示形式 -> 是否显示请求过程中的日志,包含详细的请求头日志
    protected boolean NET_LOG_DETAILS_All = false;
    // 非Form表单形式的请求体,是否加入公共Body
    protected boolean NOFORMBODY_CANADDBODY = false;
    // 网络缓存策略: 0->不启用缓存  1->遵从服务器缓存配置
    protected int NET_CACHE_TYPE = 0;
    // 网络缓存大小(MB)
    protected int NET_CACHE_SIZE = 0;
    // 网络连接超时时间(秒)
    protected int CONNECT_TIMEOUT = 30;
    // 写入超时时间(秒)
    protected int WRITE_TIMEOUT = 30;
    // 读取超时时间(秒)
    protected int READ_TIMEOUT = 30;
}
