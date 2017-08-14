package com.acmenxd.frame.utils.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import com.acmenxd.frame.basis.FrameApplication;
import com.acmenxd.toaster.Toaster;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:07
 * @detail 网络状态Util
 */
public final class NetUtils {
    // Wifi
    private static final int NETWORK_TYPE_WIFI = -1;
    /**
     * Network type is unknown
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     */
    public static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    public static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    public static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    public static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     */
    public static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    public static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    public static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     */
    public static final int NETWORK_TYPE_HSPAP = 15;
    /**
     * Current network is GSM
     */
    public static final int NETWORK_TYPE_GSM = 16;
    /**
     * Current network is TD_SCDMA
     */
    public static final int NETWORK_TYPE_TD_SCDMA = 17;
    /**
     * Current network is IWLAN
     */
    public static final int NETWORK_TYPE_IWLAN = 18;
    /**
     * Current network is LTE_CA {@hide}
     */
    public static final int NETWORK_TYPE_LTE_CA = 19;

    /**
     * 检查网络是否连接 如无网络弹出提示
     */
    public static boolean checkNetworkWithToast() {
        boolean network = checkNetwork();
        if (!network) {
            Toaster.show("无网络连接");
        }
        return network;
    }

    /**
     * 检查网络是否连接
     */
    public static boolean checkNetwork() {
        boolean result = false;
        ConnectivityManager connMgr = (ConnectivityManager) FrameApplication.instance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr == null) {
            return result;
        }
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected()) {
                result = true;
            } else {
                //获取移动数据连接的信息
                NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (dataNetworkInfo != null && dataNetworkInfo.isConnected()) {
                    result = true;
                }
            }
        } else {
            //API大于23时使用下面的方式进行网络监听
            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
            if (networks != null && networks.length > 0) {
                for (int i = 0, len = networks.length; i < len; i++) {
                    NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                    if (networkInfo != null && networkInfo.isConnected()) {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取当前网络状态
     */
    public static NetStatus getNetStatus() {
        int networkType = NETWORK_TYPE_UNKNOWN;
        try {
            ConnectivityManager manager = (ConnectivityManager) FrameApplication.instance().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (info != null && info.isAvailable() && info.isConnected()) {
                    int type = info.getType();
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        networkType = NETWORK_TYPE_WIFI;
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        TelephonyManager telephonyManager = (TelephonyManager) FrameApplication.instance().getSystemService(Context.TELEPHONY_SERVICE);
                        networkType = telephonyManager.getNetworkType();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return getNetStatus(networkType);
    }

    /**
     * 根据网络基本类型, 获取当前网络状态
     */
    private static NetStatus getNetStatus(@IntRange(from = -1) int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_WIFI:
                return NetStatus.Wifi;
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
            case NETWORK_TYPE_GSM:
                return NetStatus.TwoG;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
            case NETWORK_TYPE_TD_SCDMA:
                return NetStatus.ThreeG;
            case NETWORK_TYPE_LTE:
            case NETWORK_TYPE_IWLAN:
            case NETWORK_TYPE_LTE_CA:
                return NetStatus.FourG;
            default:
                return NetStatus.No;
        }
    }

    /**
     * 获取当前网络状态 -> 文本类型
     */
    public static String getNetStatusStr() {
        return getNetStatusStr(getNetStatus());
    }

    public static String getNetStatusStr(@NonNull NetStatus status) {
        if (status == NetStatus.Wifi) {
            return "WiFi";
        } else if (status == NetStatus.TwoG) {
            return "2G";
        } else if (status == NetStatus.ThreeG) {
            return "3G";
        } else if (status == NetStatus.FourG) {
            return "4G";
        } else if (status == NetStatus.No) {
            return "不可用";
        }
        return "未知";
    }

    /**
     * 获取运营商
     */
    public static String getProvider() {
        String provider = "未知";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) FrameApplication.instance().getSystemService(Context.TELEPHONY_SERVICE);
            String IMSI = telephonyManager.getSubscriberId();
            if (IMSI == null) {
                if (TelephonyManager.SIM_STATE_READY == telephonyManager.getSimState()) {
                    String operator = telephonyManager.getSimOperator();
                    if (operator != null) {
                        if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                            provider = "中国移动";
                        } else if (operator.equals("46001")) {
                            provider = "中国联通";
                        } else if (operator.equals("46003")) {
                            provider = "中国电信";
                        }
                    }
                }
            } else {
                if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                    provider = "中国移动";
                } else if (IMSI.startsWith("46001")) {
                    provider = "中国联通";
                } else if (IMSI.startsWith("46003")) {
                    provider = "中国电信";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provider;
    }
}
