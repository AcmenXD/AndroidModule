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
    public static String IMEI;

    // 公共请求参数
    protected static Map<String, String> ParameterMaps = new HashMap<>();
    // 公共Header(不允许相同Key值存在)
    protected static Map<String, String> HeaderMaps = new HashMap<>();
    // 公共Header(允许相同Key值存在)
    protected static Map<String, String> HeaderMaps2 = new HashMap<>();
    // 公共Body
    protected static Map<String, String> BodyMaps = new HashMap<>();

    /**
     * 初始化 -> BaseApplication中调用
     */
    public static synchronized void init() {
        BaseApplication app = BaseApplication.instance();
        PackageManager pkgManager = app.getPackageManager();
        /**
         * 提取
         */
        int versionCode = 1;
        String versionName = "1.0";
        String projectName = app.getResources().getString(R.string.app_name);
        String packageName = "000000000000000";
        String market = "AcmenXD"; // 默认渠道号
        String imei = "";
        PackageInfo info = null;
        try {
            info = pkgManager.getPackageInfo(app.getPackageName(), 0);
            packageName = app.getPackageName();
            projectName = (String) pkgManager.getApplicationLabel(pkgManager.getApplicationInfo(packageName, 0));
        } catch (PackageManager.NameNotFoundException pE) {
            Logger.e(pE);
        }
        versionCode = info.versionCode;
        versionName = info.versionName;
        market = Marketer.getMarket(app.getApplicationContext(), market);
        imei = ((TelephonyManager) app.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        /**
         * 赋值
         */
        VERSION_CODE = versionCode;
        VERSION_NAME = versionName;
        PROJECT_NAME = projectName;
        PKG_NAME = packageName;
        MARKET = market;
        IMEI = imei;
        /**
         * 设置Net请求参数
         */
        ParameterMaps.put("parameter_key_1", "parameter_value_1");
        ParameterMaps.put("parameter_key_2", "parameter_value_2");
        HeaderMaps.put("header_key_1", "header_value_1");
        HeaderMaps.put("header_key_2", "header_value_2");
        HeaderMaps2.put("header_key_2", "header_value_2");
        HeaderMaps2.put("header_key_3", "header_value_3");
        BodyMaps.put("body_key_1", "body_value_1");
        BodyMaps.put("body_key_2", "body_value_2");
    }
}
