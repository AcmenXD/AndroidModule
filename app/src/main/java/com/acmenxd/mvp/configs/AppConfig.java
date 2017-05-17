package com.acmenxd.mvp.configs;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.acmenxd.marketer.Marketer;
import com.acmenxd.mvp.base.BaseApplication;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:36
 * @detail App配置
 */
public final class AppConfig {
    /**
     * App 版本号
     */
    public static int VERSION_CODE;
    /**
     * App 版本name
     */
    public static String VERSION_NAME;
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
        int versionCode = 1;
        String versionName = "1.0";
        String packageName = "000000000000000";
        String market = BaseConfig.DEFAULT_MARKET;
        String imei = "";
        try {
            PackageInfo info = app.getPackageManager().getPackageInfo(app.getPackageName(), 0);
            versionCode = info.versionCode;
            versionName = info.versionName;
            packageName = app.getPackageName();
            market = Marketer.getMarket(app.getApplicationContext(), market);
            imei = ((TelephonyManager) app.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (PackageManager.NameNotFoundException pE) {
            pE.printStackTrace();
        }
        VERSION_CODE = versionCode;
        VERSION_NAME = versionName;
        PKG_NAME = packageName;
        MARKET = market;
        IMEI = imei;
    }
}
