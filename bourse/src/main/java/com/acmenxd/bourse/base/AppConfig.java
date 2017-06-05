package com.acmenxd.bourse.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.acmenxd.bourse.BuildConfig;
import com.acmenxd.bourse.R;
import com.acmenxd.frame.configs.BourseConfig;
import com.acmenxd.marketer.Marketer;

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
    public static BourseConfig config;
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

    /**
     * 初始化 -> BaseApplication中调用
     */
    public static final synchronized void init() {
        BaseApplication app = BaseApplication.instance();
        PackageManager pkgManager = app.getPackageManager();
        int versionCode = 1;
        String versionName = "1.0";
        String projectName = app.getResources().getString(R.string.app_name);
        String packageName = "000000000000000";
        String market = "AcmenXD"; // 默认渠道号
        String imei = "";
        try {
            PackageInfo info = pkgManager.getPackageInfo(app.getPackageName(), 0);
            versionCode = info.versionCode;
            versionName = info.versionName;
            packageName = app.getPackageName();
            projectName = (String) pkgManager.getApplicationLabel(pkgManager.getApplicationInfo(packageName, 0));
            market = Marketer.getMarket(app.getApplicationContext(), market);
            imei = ((TelephonyManager) app.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (PackageManager.NameNotFoundException pE) {
            pE.printStackTrace();
        }
        VERSION_CODE = versionCode;
        VERSION_NAME = versionName;
        PROJECT_NAME = projectName;
        PKG_NAME = packageName;
        MARKET = market;
        IMEI = imei;
    }
}
