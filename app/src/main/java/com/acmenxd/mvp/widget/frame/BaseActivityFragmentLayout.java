package com.acmenxd.mvp.widget.frame;

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
public class BaseActivityFragmentLayout extends FrameLayout {
    public BaseActivityFragmentLayout(Context context) {
        this(context, null);
    }

    public BaseActivityFragmentLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseActivityFragmentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
