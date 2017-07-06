package com.acmenxd.toaster;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/22 14:21
 * @detail Toast 显示模式 -> 作用ToastsNeedWait
 */
public final class ToastNW {
    /**
     * Toast需要等待,并逐个显示
     */
    public static ToastNW NEED_WAIT = ToastNW.mk(true);
    /**
     * Toast无需等待,直接显示
     */
    public static ToastNW No_NEED_WAIT = ToastNW.mk(false);


    private static ToastNW mk(boolean pIsNeedWait) {
        return new ToastNW(pIsNeedWait);
    }

    private boolean mIsNeedWait = true;

    private ToastNW(boolean pIsNeedWait) {
        this.mIsNeedWait = pIsNeedWait;
    }

    public boolean isNeedWait() {
        return mIsNeedWait;
    }
}
