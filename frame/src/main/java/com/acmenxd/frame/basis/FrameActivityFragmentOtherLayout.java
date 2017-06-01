package com.acmenxd.frame.basis;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/17 15:47
 * @detail BaseActivity|BaseFragment 的 otherLayout布局
 */
public class FrameActivityFragmentOtherLayout extends FrameLayout {
    public FrameActivityFragmentOtherLayout(Context context) {
        this(context, null);
    }

    public FrameActivityFragmentOtherLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameActivityFragmentOtherLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }
}
