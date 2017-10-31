package com.acmenxd.toaster;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.acmenxd.frame.R;
import com.acmenxd.frame.utils.Utils;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:36
 * @detail Toast总类
 */
public final class Toaster {
    /**
     * 初始化配置
     */
    // Toast调试开关 - 可根据debug-release配置
    public static boolean DEBUG = true;
    // Toast默认显示时长
    public static ToastDuration TOAST_DURATION = ToastDuration.SHORT;
    // Toast显示方式 - 默认为ToastNW.NEED_WAIT(Toast需要等待,并逐个显示) 可设置为:ToastNW.No_NEED_WAIT(Toast无需等待,直接显示)
    public static ToastNW NEED_WAIT = ToastNW.NEED_WAIT;
    // 上下文对象
    private static Context sContext;

    /**
     * 设置Context对象
     * * 必须设置,否则无法使用
     */
    public static void setContext(@NonNull Context pContext) {
        sContext = pContext;
        Toast toast = new Toast(pContext);
        // 初始配置
        gravity = toast.getGravity();
        offsetX = toast.getXOffset();
        offsetY = toast.getYOffset();
        marginX = toast.getHorizontalMargin();
        marginY = toast.getVerticalMargin();
        // 项目配置
        gravity = Gravity.CENTER;
        offsetX = 0;
        offsetY = 0;
        marginX = 0;
        marginY = 0;
    }

    // Toast 表
    private static Map<Long, Toast2> tMap = new ConcurrentHashMap<>();
    private static long mTId = 10000; //每个Toast的唯一Id
    // 默认的一些参数
    private static int gravity = Gravity.CENTER;
    private static int offsetX = 0;
    private static int offsetY = 0;
    private static float marginX = 0;
    private static float marginY = 0;

    /**
     * 取消一个Toast
     *
     * @param tId
     */
    public synchronized static void cancel(long tId) {
        Toast2 t = tMap.remove(tId);
        if (t != null) {
            t.cancel();
        }
    }

    /**
     * 取消所有Toast
     */
    public synchronized static void cancelAll() {
        Iterator<Long> it = tMap.keySet().iterator();
        while (it.hasNext()) {
            cancel(it.next());
        }
    }

    /**
     * 检索集合,整理数据
     */
    private synchronized static void checkCancel() {
        for (Map.Entry<Long, Toast2> entry : tMap.entrySet()) {
            if (entry.getValue().isCancel()) {
                cancel(entry.getKey());
            }
        }
    }

    /**
     * Debug模式 ----------------- start
     */
    public static void debugShow(@StringRes int[] resIds) {
        showAll(true, NEED_WAIT, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, resIds);
    }

    public static void debugShow(@NonNull Object... objs) {
        showAll(true, NEED_WAIT, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, objs);
    }

    public static void debugShow(@NonNull View view) {
        showAll(true, NEED_WAIT, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, view);
    }

    public static void debugShow(@NonNull ToastDuration duration, @StringRes int[] resIds) {
        showAll(true, NEED_WAIT, duration, gravity, offsetX, offsetY, marginX, marginY, resIds);
    }

    public static void debugShow(@NonNull ToastDuration duration, @NonNull Object... objs) {
        showAll(true, NEED_WAIT, duration, gravity, offsetX, offsetY, marginX, marginY, objs);
    }

    public static void debugShow(@NonNull ToastDuration duration, @NonNull View view) {
        showAll(true, NEED_WAIT, duration, gravity, offsetX, offsetY, marginX, marginY, view);
    }

    public static void debugShow(@NonNull ToastNW needWait, @StringRes int[] resIds) {
        showAll(true, needWait, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, resIds);
    }

    public static void debugShow(@NonNull ToastNW needWait, @NonNull Object... objs) {
        showAll(true, needWait, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, objs);
    }

    public static void debugShow(@NonNull ToastNW needWait, @NonNull View view) {
        showAll(true, needWait, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, view);
    }

    public static void debugShow(@IntRange(from = 0) int gravity, @StringRes int[] resIds) {
        showAll(true, NEED_WAIT, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, resIds);
    }

    // 此函数的 objs 特殊,否则与show(Object... objs)冲突
    public static void debugShow(@IntRange(from = 0) int gravity, @NonNull Object[] objs) {
        showAll(true, NEED_WAIT, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, objs);
    }

    public static void debugShow(@IntRange(from = 0) int gravity, @NonNull View view) {
        showAll(true, NEED_WAIT, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, view);
    }

    public static void debugShow(@NonNull ToastNW needWait, @NonNull ToastDuration duration, @StringRes int[] resIds) {
        showAll(true, needWait, duration, gravity, offsetX, offsetY, marginX, marginY, resIds);
    }

    public static void debugShow(@NonNull ToastNW needWait, @NonNull ToastDuration duration, @NonNull Object... objs) {
        showAll(true, needWait, duration, gravity, offsetX, offsetY, marginX, marginY, objs);
    }

    public static void debugShow(@NonNull ToastNW needWait, @NonNull ToastDuration duration, @NonNull View view) {
        showAll(true, needWait, duration, gravity, offsetX, offsetY, marginX, marginY, view);
    }

    public static void debugShow(@NonNull ToastNW needWait, @NonNull ToastDuration duration, @IntRange(from = 0) int gravity, @StringRes int[] resIds) {
        showAll(true, needWait, duration, gravity, offsetX, offsetY, marginX, marginY, resIds);
    }

    // 此函数的 objs 特殊,否则与show(Object... objs)冲突
    public static void debugShow(@NonNull ToastNW needWait, @NonNull ToastDuration duration, @IntRange(from = 0) int gravity, @NonNull Object[] objs) {
        showAll(true, needWait, duration, gravity, offsetX, offsetY, marginX, marginY, objs);
    }

    public static void debugShow(@NonNull ToastNW needWait, @NonNull ToastDuration duration, @IntRange(from = 0) int gravity, @NonNull View view) {
        showAll(true, needWait, duration, gravity, offsetX, offsetY, marginX, marginY, view);
    }
    // Debug模式 ----------------- end

    /**
     * 非调试模式 ----------------- start
     */
    public static void show(@StringRes int[] resIds) {
        showAll(false, NEED_WAIT, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, resIds);
    }

    public static void show(@NonNull Object... objs) {
        showAll(false, NEED_WAIT, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, objs);
    }

    public static void show(@NonNull View view) {
        showAll(false, NEED_WAIT, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, view);
    }

    public static void show(@NonNull ToastDuration duration, @StringRes int[] resIds) {
        showAll(false, NEED_WAIT, duration, gravity, offsetX, offsetY, marginX, marginY, resIds);
    }

    public static void show(@NonNull ToastDuration duration, @NonNull Object... objs) {
        showAll(false, NEED_WAIT, duration, gravity, offsetX, offsetY, marginX, marginY, objs);
    }

    public static void show(@NonNull ToastDuration duration, @NonNull View view) {
        showAll(false, NEED_WAIT, duration, gravity, offsetX, offsetY, marginX, marginY, view);
    }

    public static void show(@NonNull ToastNW needWait, @StringRes int[] resIds) {
        showAll(false, needWait, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, resIds);
    }

    public static void show(@NonNull ToastNW needWait, @NonNull Object... objs) {
        showAll(false, needWait, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, objs);
    }

    public static void show(@NonNull ToastNW needWait, @NonNull View view) {
        showAll(false, needWait, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, view);
    }

    public static void show(@IntRange(from = 0) int gravity, @StringRes int[] resIds) {
        showAll(false, NEED_WAIT, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, resIds);
    }

    // 此函数的 objs 特殊,否则与show(Object... objs)冲突
    public static void show(@IntRange(from = 0) int gravity, @NonNull Object[] objs) {
        showAll(false, NEED_WAIT, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, objs);
    }

    public static void show(@IntRange(from = 0) int gravity, @NonNull View view) {
        showAll(false, NEED_WAIT, ToastDuration.Default(), gravity, offsetX, offsetY, marginX, marginY, view);
    }

    public static void show(@NonNull ToastNW needWait, @NonNull ToastDuration duration, @StringRes int[] resIds) {
        showAll(false, needWait, duration, gravity, offsetX, offsetY, marginX, marginY, resIds);
    }

    public static void show(@NonNull ToastNW needWait, @NonNull ToastDuration duration, Object... objs) {
        showAll(false, needWait, duration, gravity, offsetX, offsetY, marginX, marginY, objs);
    }

    public static void show(@NonNull ToastNW needWait, @NonNull ToastDuration duration, View view) {
        showAll(false, needWait, duration, gravity, offsetX, offsetY, marginX, marginY, view);
    }

    public static void show(@NonNull ToastNW needWait, @NonNull ToastDuration duration, @IntRange(from = 0) int gravity, @StringRes int[] resIds) {
        showAll(false, needWait, duration, gravity, offsetX, offsetY, marginX, marginY, resIds);
    }

    // 此函数的 objs 特殊,否则与show(Object... objs)冲突
    public static void show(@NonNull ToastNW needWait, @NonNull ToastDuration duration, @IntRange(from = 0) int gravity, @NonNull Object[] objs) {
        showAll(false, needWait, duration, gravity, offsetX, offsetY, marginX, marginY, objs);
    }

    public static void show(@NonNull ToastNW needWait, @NonNull ToastDuration duration, @IntRange(from = 0) int gravity, @NonNull View view) {
        showAll(false, needWait, duration, gravity, offsetX, offsetY, marginX, marginY, view);
    }
    // 非调试模式 ----------------- end

    /**
     * 所有参数为 resIds 的统一到这里
     */
    public static void showAll(boolean isDebug, @NonNull ToastNW needWait, @NonNull ToastDuration duration, int gravity, int offsetX, int offsetY, float marginX, float marginY, @StringRes int... resIds) {
        if (resIds == null) {
            return;
        }
        int len = resIds.length;
        if (len <= 0) {
            return;
        }
        Object[] msgs = new Object[len];
        for (int i = 0; i < len; i++) {
            msgs[i] = sContext.getResources().getString(resIds[i]);
        }
        showBase(isDebug, needWait, duration, gravity, offsetX, offsetY, marginX, marginY, null, msgs);
    }

    /**
     * 所有参数为 objs 的统一到这里
     */
    public static void showAll(boolean isDebug, @NonNull ToastNW needWait, @NonNull ToastDuration duration, int gravity, int offsetX, int offsetY, float marginX, float marginY, @NonNull Object... msgs) {
        showBase(isDebug, needWait, duration, gravity, offsetX, offsetY, marginX, marginY, null, msgs);
    }

    /**
     * 所有参数为 view 的统一到这里
     */
    public static void showAll(boolean isDebug, @NonNull ToastNW needWait, @NonNull ToastDuration duration, int gravity, int offsetX, int offsetY, float marginX, float marginY, @NonNull View view) {
        showBase(isDebug, needWait, duration, gravity, offsetX, offsetY, marginX, marginY, view);
    }

    /**
     * 基础,所有调用 最后都统一到这里
     *
     * @param isDebug  是否是调试~ 如果为true, 那么这个Toast在正式上线版本中不会显示
     * @param needWait Toast显示方式 : Toast需要等待,并逐个显示 | Toast无需等待,直接显示
     * @param duration 显示时间,支持自定义时间(单位毫秒)
     * @param gravity  对其方式 -> 默认 Gravity.BOTTOM
     * @param offsetX  偏移量X
     * @param offsetY  偏移量Y
     * @param marginX  Toast左上角的X间距, 取值0 - 1
     * @param marginY  Toast左上角的Y间距, 取值0 - 1
     * @param view     Toast要显示的view
     * @param msgs     Toast要显示的内容
     * @return Toast2 对象实例
     */
    private static final synchronized Toast2 showBase(boolean isDebug, @NonNull ToastNW needWait, @NonNull ToastDuration duration, int gravity, int offsetX, int offsetY, float marginX, float marginY, View view, Object... msgs) {
        if (view == null && Utils.isEmpty(Utils.appendStrs(msgs))) {
            return null;
        }
        /**
         * 初始化
         */
        boolean canShow = false;
        /**
         * 处理逻辑
         */
        if (needWait.isNeedWait()) {
            checkCancel();
        } else {
            cancelAll();
        }
        mTId += 1; // 自增Toast的唯一id
        Toast2 toast2 = new Toast2(sContext, mTId);
        tMap.put(toast2.getTId(), toast2);
        if (view != null) {
            // view不等于null时, 显示view ,而不显示其他内容
            toast2.setView(view);
            canShow = true;
        } else if (msgs != null & msgs.length > 0) {
            // view等于null时, 不显示view ,而显示内容
            String msgStr = Utils.appendStrs(msgs);
            View layout = LayoutInflater.from(sContext).inflate(R.layout.toaster, null);
            ((TextView) layout.findViewById(R.id.widget_toaster_tvContent)).setText(msgStr);
            toast2.setText(String.valueOf(mTId));
            toast2.setView(layout);
            canShow = true;
        }
        if (canShow) {
            toast2.setGravity(gravity, offsetX, offsetY);
            toast2.setMargin(marginX, marginY);
            if (isDebug) {
                if (DEBUG) {
                    toast2.show(duration.gDuration());
                    return toast2;
                } else {
                    // 取消掉,整理数据
                    cancel(toast2.getTId());
                }
            } else {
                toast2.show(duration.gDuration());
                return toast2;
            }
        }
        return null;
    }
}
