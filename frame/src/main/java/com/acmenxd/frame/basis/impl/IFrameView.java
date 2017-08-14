package com.acmenxd.frame.basis.impl;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

import com.acmenxd.frame.utils.Utils;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/7/28 16:26
 * @detail 支持View的接口类
 */
public interface IFrameView {
    /**
     * 根据viewId获取控件实例
     */
    <T extends View> T getView(@IdRes int viewId);

    /**
     * 设置内容视图
     */
    void setContentView(@LayoutRes int layoutResId);

    void setContentView(@NonNull View view);

    /**
     * 设置标题视图
     */
    void setTitleView(@LayoutRes int layoutResId);

    void setTitleView(@NonNull View view);

    /**
     * 显示内容视图,隐藏其他视图
     */
    void showContentView();

    void showContentView(boolean animat);

    /**
     * 显示加载视图,隐藏其他视图
     */
    void showLoadingView();

    void showLoadingView(View pView);

    void showLoadingView(boolean animat);

    void showLoadingView(View pView, boolean animat);

    /**
     * 显示错误视图,隐藏其他视图
     */
    void showErrorView();

    void showErrorView(View pView);

    void showErrorView(boolean animat);

    void showErrorView(Utils.OnClickListener pListener);

    void showErrorView(View pView, boolean animat);

    void showErrorView(View pView, Utils.OnClickListener pListener);

    void showErrorView(View pView, Utils.OnClickListener pListener, boolean animat);

    /**
     * 获取内容视图实例
     */
    View getContentView();

    /**
     * 获取加载视图实例
     */
    View getLoadingView();

    /**
     * 获取错误视图实例
     */
    View getErrorView();

    /**
     * 获取标题视图实例
     */
    View getTitleView();
}
