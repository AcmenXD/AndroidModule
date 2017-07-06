package com.acmenxd.mvp.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.acmenxd.frame.configs.MvpConfig;
import com.acmenxd.logger.Logger;
import com.acmenxd.marketer.Marketer;
import com.acmenxd.mvp.BuildConfig;
import com.acmenxd.mvp.R;
import com.acmenxd.mvp.net.NetCode;
import com.acmenxd.retrofit.NetManager;
import com.acmenxd.retrofit.NetMutualCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:36
 * @detail App配置
 */
public final class AppConfig {
    /**
     * 调试模式开关
     * * 由build.gradle -> debug | release版本控制
     */
    public static final boolean DEBUG = BuildConfig.MAIN_DEBUG;
    /**
     * 项目整体配置信息
     */
    public static MvpConfig config;
    /**
     * App 版本号
     */
    public static int VERSION_CODE;
    /**
     * App 版本
     */
    public static String VERSION_NAME;
    /**
     * App 名称
     */
    public static String PROJECT_NAME;
    /**
     * App 包名
     */
    public static String PKG_NAME;
    /**
     * App 渠道
     */
    public static String MARKET;
    /**
     * IMEI号
     */
    public static String IMEI = "000000000000000";

    /**
     * 初始化 -> BaseApplication中调用
     */
    public static void init() {
        BaseApplication app = BaseApplication.instance();
        PackageManager pkgManager = app.getPackageManager();
        // 设置默认值
        VERSION_CODE = 1;
        VERSION_NAME = "1.0";
        PROJECT_NAME = app.getResources().getString(R.string.app_name);
        PKG_NAME = "";
        MARKET = "AcmenXD";
        /**
         * 初始化值
         */
        PackageInfo info = null;
        try {
            info = pkgManager.getPackageInfo(app.getPackageName(), 0);
            PKG_NAME = app.getPackageName();
            PROJECT_NAME = (String) pkgManager.getApplicationLabel(pkgManager.getApplicationInfo(PKG_NAME, 0));
        } catch (PackageManager.NameNotFoundException pE) {
            Logger.e(pE);
        }
        VERSION_CODE = info.versionCode;
        VERSION_NAME = info.versionName;
        MARKET = Marketer.getMarket(app.getApplicationContext(), MARKET);


        /**
         * 设置Net公共参数 -> 为动态配置而设置的此函数
         */
        NetManager.INSTANCE.parseNetCode = new NetCode();
        NetManager.INSTANCE.mutualCallback = new NetMutualCallback() {
            @Override
            public Map<String, String> getBodys(String url) {
                Map<String, String> maps = new HashMap<>();
                maps.put("body_key_1", "body_value_1");
                return maps;
            }

            @Override
            public Map<String, String> getParameters(String url) {
                Map<String, String> maps = new HashMap<>();
                maps.put("parameter_key_1", "parameter_value_1");
                maps.put("IMEI", IMEI);
                return maps;
            }

            @Override
            public Map<String, String> getHeaders(String url) {
                Map<String, String> maps = new HashMap<>();
                maps.put("header_key_1", "header_value_1");
                maps.put("IMEI", IMEI);
                return maps;
            }

            @Override
            public Map<String, String> getReHeaders(String url) {
                Map<String, String> maps = new HashMap<>();
                maps.put("header_key_1", "header_value_1");
                maps.put("IMEI", IMEI);
                return null;
            }
        };
    }

    /**
     * 获取到手机权限后回调
     */
    public static void permissionsAfterInit() {
        BaseApplication app = BaseApplication.instance();
        IMEI = ((TelephonyManager) app.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }
}
