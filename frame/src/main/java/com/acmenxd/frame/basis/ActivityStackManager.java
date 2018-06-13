package com.acmenxd.frame.basis;

import android.app.ActivityManager;
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

    private static Stack<FrameActivity> activityStack = new Stack<>();

    /**
     * 获取当前Activity
     */
    public FrameActivity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 判断Activity是否在栈顶
     */
    public boolean isCurrentActivity(@NonNull FrameActivity activity) {
        return activity == currentActivity();
    }

    /**
     * 判断Activity是否在栈顶 -> android系统Activity管理栈
     */
    public boolean isSysCurrentActivity(@NonNull FrameActivity activity) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            return runningTaskInfos.get(0).topActivity.getClassName().equals(activity.getClass().getName());
        }
        return false;
    }

    /**
     * 是否包含Activity
     */
    public boolean containsActivity(@NonNull Class<? extends FrameActivity> cls) {
        for (FrameActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据class,获取Activity实例
     */
    public List<FrameActivity> getActivitys(@NonNull Class<? extends FrameActivity> cls) {
        List<FrameActivity> activities = new ArrayList<>();
        for (FrameActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                activities.add(activity);
            }
        }
        return activities;
    }

    /**
     * 获取前一个Activity
     */
    public FrameActivity prevActivity(@NonNull FrameActivity activity) {
        if (activity != null && activityStack.size() > 0 && activityStack.contains(activity) && activityStack.firstElement() != activity) {
            return activityStack.get(activityStack.lastIndexOf(activity) - 1);
        }
        return null;
    }

    /**
     * 获取下一个Activity
     */
    public FrameActivity nextActivity(@NonNull FrameActivity activity) {
        if (activity != null && activityStack.size() > 0 && activityStack.contains(activity) && activityStack.lastElement() != activity) {
            return activityStack.get(activityStack.lastIndexOf(activity) + 1);
        }
        return null;
    }

    /**
     * 结束当前Activity
     */
    public synchronized void finishActivity() {
        finishActivity(true);
    }

    public synchronized void finishActivity(boolean styleAnimat) {
        FrameActivity activity = activityStack.lastElement();
        finishActivity(activity, styleAnimat);
    }

    /**
     * 结束一个Activity
     */
    public synchronized void finishActivity(@NonNull FrameActivity activity) {
        finishActivity(activity, true);
    }

    public synchronized void finishActivity(@NonNull FrameActivity activity, boolean styleAnimat) {
        if (activity != null) {
            removeActivity(activity);
            activity.finish();
            if (!styleAnimat) {
                activity.overridePendingTransition(0, 0);
            }
        }
    }

    /**
     * 结束一个Activity,根据class
     */
    public synchronized void finishActivity(@NonNull Class<? extends FrameActivity> cls) {
        finishActivity(cls, true);
    }

    public synchronized void finishActivity(@NonNull Class<? extends FrameActivity> cls, boolean styleAnimat) {
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            if (activityStack.get(i).getClass().equals(cls)) {
                finishActivity(activityStack.get(i), styleAnimat);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public synchronized void finishAllActivity() {
        finishAllActivity(true);
    }

    public synchronized void finishAllActivity(boolean styleAnimat) {
        for (FrameActivity activity : activityStack) {
            if (null != activity) {
                activity.finish();
                if (!styleAnimat) {
                    activity.overridePendingTransition(0, 0);
                }
            }
        }
        activityStack.clear();
    }

    /**
     * 添加一个Activity
     */
    protected synchronized void addActivity(@NonNull FrameActivity activity) {
        if (activity != null) {
            activityStack.add(activity);
        }
    }

    /**
     * 移除一个Activity
     */
    protected synchronized void removeActivity(@NonNull FrameActivity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 退出应用程序 -> 杀进程
     */
    public synchronized void exit() {
        exit2();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 退出应用程序 -> 不杀进程,只是关掉所有Activity
     */
    public synchronized void exit2() {
        finishAllActivity(false);
    }

    /**
     * 重新启动App -> 杀进程,会短暂黑屏,启动慢
     */
    public synchronized void restartApp(@NonNull Class<?> splashActivityClass) {
        Intent intent = new Intent(FrameApplication.instance(), splashActivityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        FrameApplication.instance().startActivity(intent);
        //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 重新启动App -> 不杀进程,缓存的东西不清除,启动快
     */
    public synchronized void restartApp2() {
        final Intent intent = FrameApplication.instance().getPackageManager()
                .getLaunchIntentForPackage(FrameApplication.instance().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        FrameApplication.instance().startActivity(intent);
    }

}
