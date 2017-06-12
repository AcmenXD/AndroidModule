package com.acmenxd.frame.utils.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import com.acmenxd.frame.basis.FrameApplication;

import java.util.ArrayList;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:07
 * @detail 网络状态监控组件实现类
 */
public final class Monitor {
    private static Monitor mInstance = null;
    private Context mContext = null;
    private ConnectionChangeReceiver mConnectionChangeReceiver = null;
    private ArrayList<IMonitorListener> mListeners = new ArrayList<>();

    private class ConnectionChangeReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            synchronized (mListeners) {
                for (IMonitorListener monitorListener : mListeners) {
                    monitorListener.onConnectionChange(NetUtils.getNetStatus());
                }
            }
        }
    }

    /**
     * 初始化 -> BaseApplication中调用
     */
    public static synchronized void init() {
        if (mInstance == null) {
            synchronized (Monitor.class) {
                if (mInstance == null) {
                    mInstance = new Monitor();
                }
            }
        }
    }

    private Monitor() {
        mContext = FrameApplication.instance();
        if (mConnectionChangeReceiver == null) {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mConnectionChangeReceiver = new ConnectionChangeReceiver();
            mContext.registerReceiver(mConnectionChangeReceiver, filter);
        }
    }

    public void mRelease() {
        if (mConnectionChangeReceiver != null) {
            mContext.unregisterReceiver(mConnectionChangeReceiver);
            mConnectionChangeReceiver = null;
        }
        synchronized (mListeners) {
            mListeners.clear();
        }
        mInstance = null;
    }

    private boolean mRegistListener(@NonNull IMonitorListener listener) {
        synchronized (mListeners) {
            if ((listener != null) && !mListeners.contains(listener)) {
                mListeners.add(listener);
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean mUnRegistListener(@NonNull IMonitorListener listener) {
        synchronized (mListeners) {
            if (listener != null) {
                return mListeners.remove(listener);
            } else {
                return false;
            }
        }
    }

    /**
     * 注册监听
     */
    public static boolean registListener(@NonNull IMonitorListener listener) {
        return mInstance.mRegistListener(listener);
    }

    /**
     * 反注册
     */
    public static boolean unRegistListener(@NonNull IMonitorListener listener) {
        return mInstance.mUnRegistListener(listener);
    }

    /**
     * 解除所有监听
     */
    public static void release() {
        mInstance.mRelease();
    }
}
