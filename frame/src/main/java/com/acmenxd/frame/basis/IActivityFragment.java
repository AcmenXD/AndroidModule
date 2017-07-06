package com.acmenxd.frame.basis;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.view.View;

import com.acmenxd.retrofit.callback.NetCallback;
import com.acmenxd.retrofit.callback.NetSubscriber;

import retrofit2.Callback;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/13 15:29
 * @detail BaseActivity & BaseFragment 的基础函数库
 */
public interface IActivityFragment {

    /**
     * 退出应用程序
     */
    void exit();

    /**
     * 添加Subscriptions
     */
    void addSubscriptions(@NonNull Subscription... pSubscriptions);

    /**
     * 添加Presenters
     */
    void addPresenters(@NonNull FramePresenter... pPresenters);

    /**
     * 获取CompositeSubscription实例
     */
    CompositeSubscription getCompositeSubscription();

    /**
     * * Activity中:获取Intent中数据参数
     * * Fragment中:获取Activity的Intent中数据参数
     */
    Bundle getBundle();

    /**
     * 启动Activity
     */
    void startActivity(@NonNull Class cls);

    /**
     * 启动Activity
     */
    void startActivity(@NonNull Class cls, @NonNull Bundle bundle);

    /**
     * 启动Activity
     */
    void startActivity(@NonNull Class cls, @NonNull Bundle bundle, int flags);

    /**
     * 根据setting,检查是否显示LoadingDialog
     *
     * @param setting 数组下标 ->
     *                0.是否显示LoadingDialog(默认false)
     *                1.isCancelable(是否可以通过点击Back键取消)(默认true)
     *                2.isCanceledOnTouchOutside(是否在点击Dialog外部时取消Dialog)(默认false)
     */
    void showLoadingDialogBySetting(final boolean... setting);

    /**
     * 显示LoadingDialog
     *
     * @param setting 数组下标 ->
     *                0.isCancelable(是否可以通过点击Back键取消)(默认true)
     *                1.isCanceledOnTouchOutside(是否在点击Dialog外部时取消Dialog)(默认false)
     */
    void showLoadingDialog(final boolean... setting);

    /**
     * 隐藏LoadingDialog
     */
    void hideLoadingDialog();

    /**
     * 设置内容视图
     */
    void setContentView(@LayoutRes int layoutResId);

    void setContentView(@NonNull View view);

    /**
     * 设置加载视图
     */
    void setLoadingView(@NonNull View view);

    /**
     * 设置错误视图
     */
    void setErrorView(@NonNull View view);

    /**
     * 显示内容视图,隐藏其他视图
     */
    void showContentView();

    void showContentView(boolean animat);

    /**
     * 显示加载视图,隐藏其他视图
     */
    void showLoadingView();

    void showLoadingView(boolean animat);

    /**
     * 显示错误视图,隐藏其他视图
     */
    void showErrorView();

    void showErrorView(boolean animat);

    void showErrorView(View.OnClickListener pListener);

    /**
     * 隐藏内容视图
     */
    void hideContentView();

    /**
     * 隐藏加载视图
     */
    void hideLoadingView();

    /**
     * 隐藏错误视图
     */
    void hideErrorView();

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
     * 根据viewId获取控件实例
     */
    <T extends View> T getView(@IdRes int viewId);

    /**
     * 串拼接
     *
     * @param strs 可变参数类型
     * @return 拼接后的字符串
     */
    String appendStrs(@NonNull Object... strs);

    /**
     * 串变化 -> 大小&颜色
     *
     * @param start 从0开始计数(包含start)
     * @param end   从1开始计数(包含end)
     */
    SpannableString changeStr(@NonNull String str, @IntRange(from = 0) int start, @IntRange(from = 0) int end, @IntRange(from = 0) int dip, @ColorInt int color);

    SpannableString changeStr(@NonNull SpannableString spannableString, @IntRange(from = 0) int start, @IntRange(from = 0) int end, @IntRange(from = 0) int dip, @ColorInt int color);

    /**
     * 根据手机的分辨率从 dp 的单位转成 px(像素)
     */
    float dp2px(@FloatRange(from = 0) float dp);

    /**
     * 根据手机的分辨率从 px(像素)的单位转成 dp
     */
    float px2dp(@FloatRange(from = 0) float px);

    /**
     * 根据手机的分辨率从 sp 的单位转成 px(像素)
     */
    float sp2px(@FloatRange(from = 0) float sp);

    /**
     * 根据手机的分辨率从 px(像素)的单位转成 sp
     */
    float px2sp(@FloatRange(from = 0) float px);
}
