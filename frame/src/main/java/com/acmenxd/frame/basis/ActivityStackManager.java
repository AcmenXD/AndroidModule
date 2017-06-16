package com.acmenxd.frame.basis;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/13 15:29
 * @detail Activity堆栈管理器
 */
public enum ActivityStackManager {
    INSTANCE;

    private static Stack<Activity> activityStack;

    /**
     * 获取当前Activity
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 判断Activity是否在栈顶
     */
    public boolean isCurrentActivity(@NonNull Activity activity) {
        return activity == currentActivity();
    }

    /**
     * 根据class,获取Activity实例
     */
    public List<Activity> getActivitys(@NonNull Class<?> cls) {
        List<Activity> activities = new ArrayList<>();
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                activities.add(activity);
            }
        }
        return activities;
    }

    /**
     * 添加一个Activity
     */
    public void addActivity(@NonNull Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 移除一个Activity
     */
    public void removeActivity(@NonNull Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.remove(activity);
    }

    /**
     * 结束当前Activity
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束一个Activity
     */
    public void finishActivity(@NonNull Activity activity) {
        if (activity != null) {
            removeActivity(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束一个Activity,根据class
     */
    public void finishActivity(@NonNull Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (Activity activity : activityStack) {
            if (null != activity) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序 -> 杀进程
     */
    public void exit() {
        exit2();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 退出应用程序 -> 不杀进程,只是关掉所有Activity
     */
    public void exit2() {
        finishAllActivity();
    }

    /**
     * 重新启动App -> 杀进程,会短暂黑屏,启动慢
     */
    public void restartApp(@NonNull Class<?> splashActivityClass) {
        Intent intent = new Intent(FrameApplication.instance(), splashActivityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        FrameApplication.instance().startActivity(intent);
        //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 重新启动App -> 不杀进程,缓存的东西不清除,启动快
     */
    public void restartApp2() {
        final Intent intent = FrameApplication.instance().getPackageManager()
                .getLaunchIntentForPackage(FrameApplication.instance().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        FrameApplication.instance().startActivity(intent);
    }

}
