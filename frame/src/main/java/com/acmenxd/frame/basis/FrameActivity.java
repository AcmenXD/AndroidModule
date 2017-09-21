package com.acmenxd.frame.basis;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.acmenxd.frame.R;
import com.acmenxd.frame.basis.impl.IFrameNet;
import com.acmenxd.frame.basis.impl.IFrameStart;
import com.acmenxd.frame.basis.impl.IFrameSubscription;
import com.acmenxd.frame.basis.impl.IFrameUtils;
import com.acmenxd.frame.basis.impl.IFrameView;
import com.acmenxd.frame.utils.DeviceUtils;
import com.acmenxd.frame.utils.Utils;
import com.acmenxd.frame.utils.net.IMonitorListener;
import com.acmenxd.frame.utils.net.Monitor;
import com.acmenxd.frame.utils.net.NetStatus;
import com.acmenxd.retrofit.HttpManager;
import com.acmenxd.retrofit.callback.HttpCallback;
import com.acmenxd.retrofit.callback.HttpSubscriber;
import com.acmenxd.retrofit.callback.IHttpProgress;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/16 16:01
 * @detail Activity基类
 */
public abstract class FrameActivity extends AppCompatActivity implements IFrameSubscription, IFrameStart, IFrameView, IFrameNet, IFrameUtils, IBView {
    protected final String TAG = this.getClass().getSimpleName();

    // 统一管理Presenters
    private List<FramePresenter> mPresenters;
    // 统一持有Subscription
    private CompositeSubscription mSubscription;
    // 存储子控件
    private SparseArray<View> mChildViews;
    // 布局容器
    private LinearLayout mContentLayout;
    private LinearLayout mLoadingLayout;
    private LinearLayout mErrorLayout;
    private LinearLayout mTitleLayout;
    // 视图view
    private View mContentView;
    private View mLoadingView;
    private View mErrorView;
    private View mTitleView;
    private Dialog mLoadingDialog;
    // 自定义状态栏
    private View mCustomStatusBarBg;
    private int customStatusBarBgResId = R.drawable.status_bar_color; // 自定义状态栏背景色
    private float customStatusBarBgAlpha = 1f; // 自定义状态栏透明度
    protected boolean isCustomStatusBarBg = false; // 自定义状态栏 & 系统支持自定义状态栏
    // 网络状态监控
    IMonitorListener mNetListener = new IMonitorListener() {
        @Override
        public void onConnectionChange(@NonNull NetStatus status) {
            onNetStatusChange(status);
        }
    };

    @CallSuper
    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 子类onCreate之前调用
        onCreateBefore(savedInstanceState);
        // 设置base视图
        super.setContentView(R.layout.activity_frame);
        // 初始化容器
        mSubscription = getCompositeSubscription();
        mPresenters = new ArrayList<>();
        mChildViews = new SparseArray<>();
        // 获取布局容器
        mContentLayout = getView(R.id.activity_frame_contentLayout);
        mLoadingLayout = getView(R.id.activity_frame_loadingLayout);
        mErrorLayout = getView(R.id.activity_frame_errorLayout);
        mTitleLayout = getView(R.id.activity_frame_titleLayout);
        mCustomStatusBarBg = getView(R.id.activity_frame_customStatusBarBg);
        // 默认显示内容视图
        showContentView();
        // 将此Activity添加到ActivityStackManager中管理
        ActivityStackManager.INSTANCE.addActivity(this);
        // 修改状态栏高度
        if (isCustomStatusBarBg && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); // 系统状态栏透明
            int customStatusBarHeight = DeviceUtils.getStatusBarHeight(this);// 系统状态栏高度
            mCustomStatusBarBg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, customStatusBarHeight));
            setCustomStatusBarBgResId(customStatusBarBgResId);
            setCustomStatusBarBgAlpha(customStatusBarBgAlpha);
        } else {
            isCustomStatusBarBg = false;
        }
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 网络监控反注册
        Monitor.unRegistListener(mNetListener);
        //解绑 Subscriptions
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        //解绑 Presenters
        if (mPresenters != null && mPresenters.size() > 0) {
            for (int i = 0, len = mPresenters.size(); i < len; i++) {
                mPresenters.get(i).unSubscribe();
            }
            mPresenters.clear();
        }
        //移除 ChildViews
        mChildViews.clear();
        //关闭 Dialog
        hideLoadingDialog();
        // 将此Activity在ActivityStackManager中移除
        ActivityStackManager.INSTANCE.removeActivity(this);
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        // 网络监控注册
        Monitor.registListener(mNetListener);
    }
    //------------------------------------子类可重写的函数

    /**
     * 子类onCreate之前调用,用来设置横竖屏等需要在setContentView之前做的操作
     */
    @CallSuper
    protected void onCreateBefore(@NonNull Bundle savedInstanceState) {
    }

    /**
     * 网络状态变换调用
     */
    @CallSuper
    protected void onNetStatusChange(@NonNull NetStatus pNetStatus) {
    }
    //------------------------------------子类可使用的工具函数 -> 私有

    /**
     * 自定义状态栏背景色
     */
    public final void setCustomStatusBarBgResId(@DrawableRes int pCustomStatusBarBgResId) {
        if (isCustomStatusBarBg) {
            this.customStatusBarBgResId = pCustomStatusBarBgResId;
            if (mCustomStatusBarBg != null) {
                mCustomStatusBarBg.setBackgroundResource(pCustomStatusBarBgResId);
            }
        }
    }

    /**
     * 自定义状态栏透明度
     */
    public final void setCustomStatusBarBgAlpha(@FloatRange(from = 0, to = 1) float pCustomStatusBarBgAlpha) {
        if (isCustomStatusBarBg) {
            this.customStatusBarBgAlpha = pCustomStatusBarBgAlpha;
            if (mCustomStatusBarBg != null) {
                mCustomStatusBarBg.setAlpha(pCustomStatusBarBgAlpha);
            }
        }
    }

    /**
     * 添加Presenters
     */
    public final void addPresenters(@NonNull FramePresenter... pPresenters) {
        if (pPresenters != null && pPresenters.length > 0) {
            if (mPresenters == null) {
                mPresenters = new ArrayList<>();
            }
            for (int i = 0, len = pPresenters.length; i < len; i++) {
                mPresenters.add(pPresenters[i]);
            }
        }
    }

    /**
     * 统一处理因异步导致的 Activity|Fragment销毁时发生NullPointerException问题
     * 统一处理LoadingDialog逻辑
     */
    public abstract class BindCallback<E> extends HttpCallback<E> {
        /**
         * 设置LoadingDialog参数
         *
         * @param setting 数组下标 ->
         *                0.是否显示LoadingDialog(默认false)
         *                1.isCancelable(是否可以通过点击Back键取消)(默认true)
         *                2.isCanceledOnTouchOutside(是否在点击Dialog外部时取消Dialog)(默认false)
         */
        public BindCallback(boolean... setting) {
            showLoadingDialogBySetting(setting);
        }

        @Deprecated
        @Override
        public void onResponse(Call<E> call, Response<E> response) {
            if (canReceiveResponse()) {
                super.onResponse(call, response);
            }
            hideLoadingDialog();
        }

        @Deprecated
        @Override
        public void onFailure(Call<E> call, Throwable t) {
            if (canReceiveResponse()) {
                super.onFailure(call, t);
            }
            hideLoadingDialog();
        }
    }

    /**
     * 统一处理因异步导致的 Activity|Fragment销毁时发生NullPointerException问题
     * 统一处理LoadingDialog逻辑
     */
    public abstract class BindSubscriber<E> extends HttpSubscriber<E> {
        /**
         * 设置LoadingDialog参数
         *
         * @param setting 数组下标 ->
         *                0.是否显示LoadingDialog(默认false)
         *                1.isCancelable(是否可以通过点击Back键取消)(默认true)
         *                2.isCanceledOnTouchOutside(是否在点击Dialog外部时取消Dialog)(默认false)
         */
        public BindSubscriber(boolean... setting) {
            showLoadingDialogBySetting(setting);
        }

        @Deprecated
        @Override
        public void onNext(E data) {
            if (canReceiveResponse()) {
                super.onNext(data);
            }
        }

        @Deprecated
        @Override
        public void onError(Throwable pE) {
            if (canReceiveResponse()) {
                super.onError(pE);
            }
        }

        @Deprecated
        @Override
        public void onCompleted() {
            if (canReceiveResponse()) {
                super.onCompleted();
            }
            hideLoadingDialog();
        }
    }
    //------------------------------------子类可使用的工具函数 -> IFrameSubscription

    /**
     * 添加Subscriptions
     */
    @Override
    public final void addSubscriptions(@NonNull Subscription... pSubscriptions) {
        getCompositeSubscription().addAll(pSubscriptions);
    }

    /**
     * 获取CompositeSubscription实例
     */
    @Override
    public final CompositeSubscription getCompositeSubscription() {
        if (mSubscription == null) {
            mSubscription = new CompositeSubscription();
        }
        return mSubscription;
    }

    /**
     * 判断能否接收Response
     */
    @Override
    public final boolean canReceiveResponse() {
        return !mSubscription.isUnsubscribed();
    }
    //------------------------------------子类可使用的工具函数 -> IFrameStart

    /**
     * 获取Intent中数据参数
     */
    @Override
    public final Bundle getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        return bundle;
    }

    /**
     * 为了统一启动方式,所以暂作过时处理
     * * 尽量不要使用此函数启动Activity
     */
    @Deprecated
    @Override
    public final void startActivity(@NonNull Intent intent) {
        super.startActivity(intent);
    }

    /**
     * 为了统一启动方式,所以暂作过时处理
     * * 尽量不要使用此函数启动Activity
     */
    @Deprecated
    @Override
    public final void startActivity(@NonNull Intent intent, @NonNull Bundle options) {
        super.startActivity(intent, options);
    }

    /**
     * 启动Activity
     */
    @Override
    public final void startActivity(@NonNull Class cls) {
        Intent intent = new Intent(this, cls);
        super.startActivity(intent);
    }

    /**
     * 启动Activity
     */
    @Override
    public final void startActivity(@NonNull Class cls, @NonNull Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        super.startActivity(intent);
    }

    /**
     * 启动Activity
     */
    @Override
    public final void startActivity(@NonNull Class cls, @NonNull Bundle bundle, int flags) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        intent.setFlags(flags);
        super.startActivity(intent);
    }

    /**
     * 为了统一启动方式,所以暂作过时处理
     * * 尽量不要使用此函数启动Activity
     */
    @Deprecated
    @Override
    public final ComponentName startService(Intent intent) {
        return super.startService(intent);
    }

    /**
     * 启动Service
     */
    @Override
    public final ComponentName startService(@NonNull Class cls) {
        Intent intent = new Intent(this, cls);
        return super.startService(intent);
    }

    /**
     * 启动Service
     */
    @Override
    public final ComponentName startService(@NonNull Class cls, @NonNull Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        return super.startService(intent);
    }
    //------------------------------------子类可使用的工具函数 -> IFrameView

    /**
     * 通过viewId获取控件
     */
    @Override
    public final <E extends View> E getView(@IdRes int viewId) {
        View view = mChildViews.get(viewId);
        if (view == null) {
            view = this.findViewById(viewId);
            mChildViews.put(viewId, view);
        }
        return (E) view;
    }

    /**
     * 设置内容视图
     */
    @Override
    public final void setContentView(@LayoutRes int layoutResId) {
        View view = LayoutInflater.from(this).inflate(layoutResId, mContentLayout, false);
        setContentView(view);
    }

    @Override
    public final void setContentView(@NonNull View view) {
        if (view == null) {
            return;
        }
        mContentView = view;
        mContentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mContentLayout.removeAllViews();
        mContentLayout.addView(mContentView);
    }

    /**
     * 设置标题视图
     */
    @Override
    public final void setTitleView(@LayoutRes int layoutResId) {
        View view = LayoutInflater.from(this).inflate(layoutResId, mTitleLayout, false);
        setTitleView(view);
    }

    @Override
    public final void setTitleView(@NonNull View view) {
        if (view == null) {
            return;
        }
        mTitleView = view;
        mTitleLayout.removeAllViews();
        mTitleLayout.addView(mTitleView);
    }

    /**
     * 显示内容视图,隐藏其他视图
     */
    @Override
    public final void showContentView() {
        showContentView(false);
    }

    @Override
    public final void showContentView(boolean animat) {
        if (animat) {
            FrameActivityFragmentViewHelper.layoutCancelInOutAnimation(this, mContentLayout, mContentLayout, mLoadingLayout, mErrorLayout);
        } else {
            FrameActivityFragmentViewHelper.layouts$setVisibility(mContentLayout, mContentLayout, mLoadingLayout, mErrorLayout);
        }
    }

    /**
     * 显示加载视图,隐藏其他视图
     */
    @Override
    public final void showLoadingView() {
        showLoadingView(null, false);
    }

    @Override
    public final void showLoadingView(View pView) {
        showLoadingView(pView, false);
    }

    @Override
    public final void showLoadingView(boolean animat) {
        showLoadingView(null, animat);
    }

    @Override
    public final void showLoadingView(View pView, boolean animat) {
        if (pView == null) {
            // 设置默认的加载视图
            mLoadingView = FrameActivityFragmentViewHelper.getLoadingView(this);
        } else {
            mLoadingView = pView;
        }
        mLoadingLayout.removeAllViews();
        mLoadingLayout.addView(mLoadingView);
        if (animat) {
            FrameActivityFragmentViewHelper.layoutCancelInOutAnimation(this, mLoadingLayout, mContentLayout, mLoadingLayout, mErrorLayout);
        } else {
            FrameActivityFragmentViewHelper.layouts$setVisibility(mLoadingLayout, mContentLayout, mLoadingLayout, mErrorLayout);
        }
    }

    /**
     * 显示错误视图,隐藏其他视图
     */
    @Override
    public final void showErrorView() {
        showErrorView(null, null, false);
    }

    @Override
    public final void showErrorView(View pView) {
        showErrorView(pView, null, false);
    }

    @Override
    public final void showErrorView(boolean animat) {
        showErrorView(null, null, animat);
    }

    @Override
    public final void showErrorView(Utils.OnClickListener pListener) {
        showErrorView(null, pListener, false);
    }

    @Override
    public final void showErrorView(View pView, boolean animat) {
        showErrorView(pView, null, animat);
    }

    @Override
    public final void showErrorView(View pView, Utils.OnClickListener pListener) {
        showErrorView(pView, pListener, false);
    }

    @Override
    public final void showErrorView(View pView, Utils.OnClickListener pListener, boolean animat) {
        if (pView == null) {
            // 设置默认的错误视图
            mErrorView = FrameActivityFragmentViewHelper.getErrorView(this);
        } else {
            mErrorView = pView;
        }
        mErrorLayout.removeAllViews();
        mErrorLayout.addView(mErrorView);
        mErrorLayout.setOnClickListener(pListener);
        if (animat) {
            FrameActivityFragmentViewHelper.layoutCancelInOutAnimation(this, mErrorLayout, mContentLayout, mLoadingLayout, mErrorLayout);
        } else {
            FrameActivityFragmentViewHelper.layouts$setVisibility(mErrorLayout, mContentLayout, mLoadingLayout, mErrorLayout);
        }
    }

    /**
     * 获取内容视图实例
     */
    @Override
    public final View getContentView() {
        return mContentView;
    }

    /**
     * 获取加载视图实例
     */
    @Override
    public final View getLoadingView() {
        return mLoadingView;
    }

    /**
     * 获取错误视图实例
     */
    @Override
    public final View getErrorView() {
        return mErrorView;
    }

    /**
     * 获取标题视图实例
     */
    @Override
    public final View getTitleView() {
        return mTitleView;
    }
    //------------------------------------子类可使用的工具函数 -> IFrameNet

    /**
     * 根据IRequest类获取Request实例
     */
    @Override
    public final <E> E request(@NonNull Class<E> pIRequest) {
        return HttpManager.INSTANCE.request(pIRequest);
    }

    /**
     * 创建新的Retrofit实例
     * 根据IRequest类获取Request实例
     */
    @Override
    public final <E> E newRequest(@NonNull Class<E> pIRequest) {
        return HttpManager.INSTANCE.newRequest(pIRequest);
    }

    /**
     * 创建新的Retrofit实例,并设置超时时间
     * 根据IRequest类获取Request实例
     */
    @Override
    public final <E> E newRequest(@NonNull Class<E> pIRequest, @IntRange(from = 0) int connectTimeout, @IntRange(from = 0) int readTimeout, @IntRange(from = 0) int writeTimeout) {
        return HttpManager.INSTANCE.newRequest(pIRequest, connectTimeout, readTimeout, writeTimeout);
    }

    /**
     * 下载Retrofit实例 -> 默认读取超时时间5分钟
     * 根据IRequest类获取Request实例
     */
    @Override
    public final <E> E downloadRequest(@NonNull Class<E> pIRequest, @NonNull IHttpProgress pProgress) {
        return HttpManager.INSTANCE.downloadRequest(pIRequest, pProgress);
    }

    /**
     * 下载Retrofit实例,并设置读取超时时间
     * 根据IRequest类获取Request实例
     */
    @Override
    public final <E> E downloadRequest(@NonNull Class<E> pIRequest, @NonNull IHttpProgress pProgress, @IntRange(from = 0) int read_timeout) {
        return HttpManager.INSTANCE.downloadRequest(pIRequest, pProgress, read_timeout);
    }

    /**
     * 上传Retrofit实例 -> 默认写入超时时间5分钟
     * 根据IRequest类获取Request实例
     */
    @Override
    public final <E> E uploadRequest(@NonNull Class<E> pIRequest, @NonNull IHttpProgress pProgress) {
        return HttpManager.INSTANCE.uploadRequest(pIRequest, pProgress);
    }

    /**
     * 上传Retrofit实例,并设置写入超时时间
     * 根据IRequest类获取Request实例
     */
    @Override
    public final <E> E uploadRequest(@NonNull Class<E> pIRequest, @NonNull IHttpProgress pProgress, @IntRange(from = 0) int writeTimeout) {
        return HttpManager.INSTANCE.uploadRequest(pIRequest, pProgress, writeTimeout);
    }
    //------------------------------------子类可使用的工具函数 -> IFrameUtils

    /**
     * 退出应用程序
     */
    @Override
    public final void exit() {
        ActivityStackManager.INSTANCE.exit();
    }

    /**
     * 字符串是否为空
     */
    @Override
    public final boolean isEmpty(@Nullable CharSequence str) {
        return Utils.isEmpty(str);
    }

    /**
     * 串拼接
     *
     * @param strs 可变参数类型
     * @return 拼接后的字符串
     */
    @Override
    public final String appendStrs(@NonNull Object... strs) {
        return Utils.appendStrs(strs);
    }

    /**
     * 串变化 -> 大小&颜色
     *
     * @param start 从0开始计数(包含start)
     * @param end   从1开始计数(包含end)
     */
    @Override
    public final SpannableString changeStr(@NonNull String str, @IntRange(from = 0) int start, @IntRange(from = 0) int end, @IntRange(from = 0) int dip, @ColorInt int color) {
        return Utils.changeStr(str, start, end, dip, color);
    }

    @Override
    public final SpannableString changeStr(@NonNull SpannableString spannableString, @IntRange(from = 0) int start, @IntRange(from = 0) int end, @IntRange(from = 0) int dip, @ColorInt int color) {
        return Utils.changeStr(spannableString, start, end, dip, color);
    }

    /**
     * 根据手机的分辨率从 dp 的单位转成 px(像素)
     */
    @Override
    public final float dp2px(@FloatRange(from = 0) float dp) {
        return Utils.dp2px(this, dp);
    }

    /**
     * 根据手机的分辨率从 px(像素)的单位转成 dp
     */
    @Override
    public final float px2dp(@FloatRange(from = 0) float px) {
        return Utils.px2dp(this, px);
    }

    /**
     * 根据手机的分辨率从 sp 的单位转成 px(像素)
     */
    @Override
    public final float sp2px(@FloatRange(from = 0) float sp) {
        return Utils.sp2px(this, sp);
    }

    /**
     * 根据手机的分辨率从 px(像素)的单位转成 sp
     */
    @Override
    public final float px2sp(@FloatRange(from = 0) float px) {
        return Utils.px2sp(this, px);
    }
    //------------------------------------子类可使用的工具函数 -> IBView

    /**
     * 统一获取上下文对象
     */
    @Override
    public final Context getContext() {
        return this;
    }

    /**
     * 根据setting,检查是否显示LoadingDialog
     *
     * @param setting 数组下标 ->
     *                0.是否显示LoadingDialog(默认false)
     *                1.isCancelable(是否可以通过点击Back键取消)(默认true)
     *                2.isCanceledOnTouchOutside(是否在点击Dialog外部时取消Dialog)(默认false)
     */
    @Override
    public final void showLoadingDialogBySetting(final boolean... setting) {
        boolean isShow = false;
        boolean isCancelable = true;
        boolean isCanceledOnTouchOutside = false;
        if (setting != null) {
            if (setting.length >= 1) {
                isShow = setting[0];
            }
            if (setting.length >= 2) {
                isCancelable = setting[1];
            }
            if (setting.length >= 3) {
                isCanceledOnTouchOutside = setting[2];
            }
        }
        if (isShow == true) {
            showLoadingDialog(isCancelable, isCanceledOnTouchOutside);
        }
    }

    /**
     * 显示LoadingDialog
     *
     * @param setting 数组下标 ->
     *                0.isCancelable(是否可以通过点击Back键取消)(默认true)
     *                1.isCanceledOnTouchOutside(是否在点击Dialog外部时取消Dialog)(默认false)
     */
    @Override
    public final void showLoadingDialog(final boolean... setting) {
        boolean isCancelable = true;
        boolean isCanceledOnTouchOutside = false;
        if (setting != null) {
            if (setting.length >= 1) {
                isCancelable = setting[0];
            }
            if (setting.length >= 2) {
                isCanceledOnTouchOutside = setting[1];
            }
        }
        if (mLoadingDialog == null || !mLoadingDialog.isShowing()) {
            mLoadingDialog = new Dialog(this);
            mLoadingDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            mLoadingDialog.setContentView(FrameActivityFragmentViewHelper.getLoadingDialogView(this));
            mLoadingDialog.show();
        }
        mLoadingDialog.setCancelable(isCancelable);
        mLoadingDialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
    }

    /**
     * 隐藏LoadingDialog
     */
    @Override
    public final void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
}
