package com.acmenxd.toaster;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/21 17:40
 * @detail Toast 封装类,支持自定义时长的Toast
 */
public final class Toast2 {
    private final int length_long = 3500; // 默认Toast长时间显示的时间(毫秒)
    private final int length_short = 2000;// 默认Toast短时间显示的时间(毫秒)
    private final int reTime = length_long - 100;// 默认循环的时间(毫秒)
    // 解决多个Toast连续显示时的cancel的bug,做个缓存时间,不过自定义时间的Toast还存在这个问题
    private final int waitTime = 1000 * 60;

    private Toast mToast;
    private long mTId = 0; //每个Toast唯一Id
    private boolean isCancel = false; //是否取消了
    private int mType = 0;//显示模式 1:指定系统显示时间的  2:自定义显示时间的
    private int mTime; //实际显示的时长
    private long mCurrentTimeMillis; //显示时的当前时间
    private Timer mTimer; //show计时器
    private Timer mAllTimer; //总计时器

    public Toast2(@NonNull Context pContext, long pTid) {
        this.mToast = makeText(pContext, "", Toast.LENGTH_SHORT);
        this.mTId = pTid;
    }

    public long getTId() {
        return mTId;
    }

    /**
     * 当前是否无显示
     *
     * @return
     */
    public boolean isCancel() {
        if (isCancel) {
            return true;
        }
        if (mType == 1) {
            if (mCurrentTimeMillis > 0 && System.currentTimeMillis() - mCurrentTimeMillis > (mTime + waitTime)) {
                isCancel = true;
            }
        } else if (mType == 2) {
            return isCancel;
        }
        return isCancel;
    }

    public void cancel() {
        if (mType == 1) {
            if (mToast != null) {
                mToast.cancel();
                mToast = null;
            }
            isCancel = true;
        } else if (mType == 2) {
            if (mToast != null) {
                mToast.cancel();
                mToast = null;
            }
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
            if (mAllTimer != null) {
                mAllTimer.cancel();
                mAllTimer = null;
            }
            isCancel = true;
        }
    }

    public void show(@IntRange(from = 0) final int duration) {
        /**
         * 设置显示时间
         */
        if (duration == mToast.LENGTH_LONG) {
            mToast.setDuration(mToast.LENGTH_LONG);
            mTime = length_long;
            mType = 1;
        } else if (duration == mToast.LENGTH_SHORT) {
            mToast.setDuration(mToast.LENGTH_SHORT);
            mTime = length_short;
            mType = 1;
        } else {
            mToast.setDuration(mToast.LENGTH_LONG);
            mTime = duration;
            mType = 2;
        }
        /**
         * 显示
         */
        if (mType == 1) {
            mCurrentTimeMillis = System.currentTimeMillis();
            mToast.show();
        } else if (mType == 2) {
            mTimer = new Timer();
            mAllTimer = new Timer();
            /**
             * 设置每次的时间
             */
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mToast.show();
                }
            }, 0, reTime);
            /**
             * 设置总时间
             * 有个bug,同时弹出多个等待模式的自定义时间Toast,计时会出现问题
             * 因为无法拿到系统Toast的开始时间
             * 所以尽量避免此种方式的调用
             */
            mAllTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Toaster.cancel(mTId);
                }
            }, mTime);
        }
    }

    /**
     * 重写Toast的部分方法
     */
    public int getYOffset() {
        return mToast.getYOffset();
    }

    public void setView(@NonNull View view) {
        mToast.setView(view);
    }

    public View getView() {
        return mToast.getView();
    }

    public void setDuration(@IntRange(from = 0) int duration) {
        mToast.setDuration(duration);
    }

    public int getDuration() {
        return mToast.getDuration();
    }

    public void setMargin(float horizontalMargin, float verticalMargin) {
        mToast.setMargin(horizontalMargin, verticalMargin);
    }

    public float getHorizontalMargin() {
        return mToast.getHorizontalMargin();
    }

    public float getVerticalMargin() {
        return mToast.getVerticalMargin();
    }

    public void setGravity(@IntRange(from = 0) int gravity, int xOffset, int yOffset) {
        mToast.setGravity(gravity, xOffset, yOffset);
    }

    public int getGravity() {
        return mToast.getGravity();
    }

    public int getXOffset() {
        return mToast.getXOffset();
    }

    public void setText(@StringRes int resId) {
        mToast.setText(resId);
    }

    public void setText(@NonNull CharSequence s) {
        mToast.setText(s);
    }

    // 不对外开放 -------------------start
    private void show() {
        mToast.show();
    }

    private static Toast makeText(@NonNull Context context, @NonNull CharSequence text, @IntRange(from = 0) int duration) {
        return Toast.makeText(context, text, duration);
    }

    private static Toast makeText(@NonNull Context context, @StringRes int resId, @IntRange(from = 0) int duration) throws Resources.NotFoundException {
        return Toast.makeText(context, resId, duration);
    }
    // 不对外开放 -------------------end

}
