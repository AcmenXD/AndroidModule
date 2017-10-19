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
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.acmenxd.frame.R;
import com.acmenxd.frame.basis.impl.IFrameNet;
import com.acmenxd.frame.basis.impl.IFrameStart;
import com.acmenxd.frame.basis.impl.IFrameSubscription;
import com.acmenxd.frame.basis.impl.IFrameUtils;
import com.acmenxd.frame.basis.impl.IFrameView;
import com.acmenxd.frame.utils.DeviceUtils;
import com.acmenxd.frame.utils.StatusBarUtils;
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
 * @date 2017/3/14 15:32
 * @detail Fragmeng基类
 */
public abstract class FrameFragment extends Fragment implements IFrameSubscription, IFrameStart, IFrameView, IFrameNet, IFrameUtils, IBView {
    protected final String TAG = this.getClass().getSimpleName();

    // Activity实例
    protected FrameActivity mActivity;
    // 统一管理Presenters
    private List<FramePresenter> mPresenters;
    // 统一持有Subscription
    private CompositeSubscription mSubscription;
    // 存储子控件
    private SparseArray<View> mChildViews;
    // 顶级视图View
    private View mRootView;
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
    // 自定义状态栏 - 对应的Activity必须已经支持自定义状态栏
    private View customStatusBarView;
    private float customStatusBarBgAlpha = 1f; // 自定义状态栏透明度
    private int customStatusBarColorId_can = 0; // 自定义状态栏背景色 - 可行时
    private int customStatusBarColorId_noCan = 0; // 自定义状态栏背景色 - 不可行时
    private boolean isCanCustomStatusBarColor = false; // 是否支持自定义状态栏
    protected int customStatusBarMode = 0; // 状态栏模式 : 0-跟随父Activity  1-自定义背景色  2-浅色模式+自定义背景色  3-深色模式+自定义背景色
    // Fragment取消预加载后的显隐处理
    private boolean viewPagerFragmentVisible;
    // Fragment取消预加载后的首次显示处理
    private boolean viewPagerFragmentFirstVisible;
    // Fragment取消预加载后的显示计数(第一次显示计数为1)
    private int viewPagerFragmentVisibleIndex;
    // 网络状态监控
    private IMonitorListener mNetListener = new IMonitorListener() {
        @Override
        public void onConnectionChange(@NonNull NetStatus status) {
            onNetStatusChange(status);
        }
    };

    @CallSuper
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 获取Activity实例
        mActivity = (FrameActivity) context;
        // 初始化容器
        mSubscription = getCompositeSubscription();
        mPresenters = new ArrayList<>();
        mChildViews = new SparseArray<>();
    }

    @Deprecated
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_frame, container, false);
        // 获取布局容器
        mContentLayout = getView(R.id.activity_frame_contentLayout);
        mLoadingLayout = getView(R.id.activity_frame_loadingLayout);
        mErrorLayout = getView(R.id.activity_frame_errorLayout);
        mTitleLayout = getView(R.id.activity_frame_titleLayout);
        customStatusBarView = getView(R.id.activity_frame_customStatusBarView);
        // 默认显示内容视图
        showContentView();
        // 设置内容视图
        setContentView(onCreateView(LayoutInflater.from(mActivity), savedInstanceState));
        // 显示自定义状态栏
        if (customStatusBarMode > 0) {
            if (mActivity.customStatusBarMode == 1 || mActivity.customStatusBarMode == 2 || mActivity.customStatusBarMode == 3) {
                mActivity.hideCustomStatusBar();
                if (customStatusBarMode == 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        isCanCustomStatusBarColor = true;
                    }
                    if (customStatusBarColorId_can == 0) {
                        customStatusBarColorId_can = R.drawable.status_bar_color_dark;
                    }
                    if (customStatusBarColorId_noCan == 0) {
                        customStatusBarColorId_noCan = R.drawable.status_bar_color_dark;
                    }
                } else if (customStatusBarMode == 2) {
                    if (StatusBarUtils.setModeStatusBar(mActivity, false)) {
                        isCanCustomStatusBarColor = true;
                    }
                    if (customStatusBarColorId_can == 0) {
                        customStatusBarColorId_can = R.drawable.status_bar_color_dark;
                    }
                    if (customStatusBarColorId_noCan == 0) {
                        customStatusBarColorId_noCan = R.drawable.status_bar_color_dark;
                    }
                } else if (customStatusBarMode == 3) {
                    if (StatusBarUtils.setModeStatusBar(mActivity, true)) {
                        isCanCustomStatusBarColor = true;
                    }
                    if (customStatusBarColorId_can == 0) {
                        customStatusBarColorId_can = R.drawable.status_bar_color_light;
                    }
                    if (customStatusBarColorId_noCan == 0) {
                        customStatusBarColorId_noCan = R.drawable.status_bar_color_dark;
                    }
                }
                // 显示自定义状态栏
                showCustomStatusBar();
            }
        }
        return mRootView;
    }

    @CallSuper
    @Override
    public void onDetach() {
        super.onDetach();
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
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        // 网络监控注册
        Monitor.registListener(mNetListener);
    }
    //------------------------------------子类可重写的函数

    /**
     * 子类创建视图请实现此函数, 勿使用onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
     */
    @CallSuper
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull Bundle savedInstanceState) {
        return null;
    }

    /**
     * Fragment取消预加载后,首次显示时回调函数
     */
    @CallSuper
    protected void onViewPagerFragmentFirstVisible() {
    }

    /**
     * Fragment取消预加载后,显示时回调函数
     */
    @CallSuper
    protected void onViewPagerFragmentVisible(@IntRange(from = 0) int viewPagerFragmentVisibleIndex) {
    }

    /**
     * Fragment取消预加载后,隐藏时回调函数
     */
    @CallSuper
    protected void onViewPagerFragmentInVisible(@IntRange(from = 0) int viewPagerFragmentVisibleIndex) {
    }

    /**
     * 网络状态变换调用
     */
    @CallSuper
    protected void onNetStatusChange(@NonNull NetStatus pNetStatus) {
    }
    //------------------------------------子类可使用的工具函数 -> 私有

    /**
     * 是否支持自定义状态栏
     */
    public boolean isCanCustomStatusBarColor() {
        return isCanCustomStatusBarColor;
    }

    /**
     * 隐藏自定义状态栏
     */
    public final void hideCustomStatusBar() {
        if (customStatusBarView != null) {
            customStatusBarView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
        }
    }

    /**
     * 显示自定义状态栏
     */
    public final void showCustomStatusBar() {
        if (mActivity.customStatusBarMode == 1 || mActivity.customStatusBarMode == 2 || mActivity.customStatusBarMode == 3) {
            if ((customStatusBarMode == 1 || customStatusBarMode == 2 || customStatusBarMode == 3)
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 4.4 以上系统支持状态栏修改
                int customStatusBarHeight = DeviceUtils.getStatusBarHeight(mActivity);// 系统状态栏高度
                customStatusBarView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, customStatusBarHeight));
                setCustomStatusBarBgAlpha(customStatusBarBgAlpha);
                if (isCanCustomStatusBarColor) {
                    setCustomStatusBarColorId_can(customStatusBarColorId_can);
                } else {
                    setCustomStatusBarColorId_noCan(customStatusBarColorId_noCan);
                }
            }
        }
    }

    /**
     * 设置状态栏透明度
     */
    public final void setCustomStatusBarBgAlpha(@FloatRange(from = 0, to = 1) float pCustomStatusBarBgAlpha) {
        if (mActivity.customStatusBarMode == 1 || mActivity.customStatusBarMode == 2 || mActivity.customStatusBarMode == 3) {
            if ((customStatusBarMode == 1 || customStatusBarMode == 2 || customStatusBarMode == 3)
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                this.customStatusBarBgAlpha = pCustomStatusBarBgAlpha;
                if (customStatusBarView != null) {
                    customStatusBarView.setAlpha(pCustomStatusBarBgAlpha);
                }
            }
        }
    }

    /**
     * 设置状态栏背景色 - 可行时
     */
    public final void setCustomStatusBarColorId_can(@DrawableRes int pCustomStatusBarColorId_can) {
        if (mActivity.customStatusBarMode == 1 || mActivity.customStatusBarMode == 2 || mActivity.customStatusBarMode == 3) {
            if ((customStatusBarMode == 1 || customStatusBarMode == 2 || customStatusBarMode == 3)
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                this.customStatusBarColorId_can = pCustomStatusBarColorId_can;
                if (customStatusBarView != null) {
                    customStatusBarView.setBackgroundResource(pCustomStatusBarColorId_can);
                }
            }
        }
    }

    /**
     * 设置状态栏背景色 - 不可行时
     */
    public final void setCustomStatusBarColorId_noCan(@DrawableRes int pCustomStatusBarColorId_noCan) {
        if (mActivity.customStatusBarMode == 1 || mActivity.customStatusBarMode == 2 || mActivity.customStatusBarMode == 3) {
            if ((customStatusBarMode == 1 || customStatusBarMode == 2 || customStatusBarMode == 3)
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                this.customStatusBarColorId_noCan = pCustomStatusBarColorId_noCan;
                if (customStatusBarView != null) {
                    customStatusBarView.setBackgroundResource(pCustomStatusBarColorId_noCan);
                }
            }
        }
    }

    /**
     * ViewPagerFragment取消预加载处理
     */
    @Override
    public final void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            viewPagerFragmentVisible = true;
            viewPagerFragmentVisibleIndex++;
            if (viewPagerFragmentVisibleIndex == 1) {
                viewPagerFragmentFirstVisible = true;
                onViewPagerFragmentFirstVisible();
            } else {
                viewPagerFragmentFirstVisible = false;
            }
            onViewPagerFragmentVisible(viewPagerFragmentVisibleIndex);
        } else {
            viewPagerFragmentVisible = false;
            viewPagerFragmentFirstVisible = false;
            onViewPagerFragmentInVisible(viewPagerFragmentVisibleIndex);
        }
    }

    /**
     * Fragment取消预加载后,获取显隐状态
     */
    public final boolean isViewPagerFragmentVisible() {
        return viewPagerFragmentVisible;
    }

    /**
     * Fragment取消预加载后,获取首次显隐状态
     */
    public final boolean isViewPagerFragmentFirstVisible() {
        return viewPagerFragmentFirstVisible;
    }

    /**
     * Fragment取消预加载后,获取显示计数
     */
    public final int getViewPagerFragmentVisibleIndex() {
        return viewPagerFragmentVisibleIndex;
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
     * 获取Activity的Intent中数据参数
     */
    @Override
    public final Bundle getBundle() {
        Bundle bundle = mActivity.getIntent().getExtras();
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
        Intent intent = new Intent(mActivity, cls);
        super.startActivity(intent);
    }

    /**
     * 启动Activity
     */
    @Override
    public final void startActivity(@NonNull Class cls, @NonNull Bundle bundle) {
        Intent intent = new Intent(mActivity, cls);
        intent.putExtras(bundle);
        super.startActivity(intent);
    }

    /**
     * 启动Activity
     */
    @Override
    public final void startActivity(@NonNull Class cls, @NonNull Bundle bundle, int flags) {
        Intent intent = new Intent(mActivity, cls);
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
        return mActivity.startService(intent);
    }

    /**
     * 启动Service
     */
    @Override
    public final ComponentName startService(@NonNull Class cls) {
        return mActivity.startService(cls);
    }

    /**
     * 启动Service
     */
    @Override
    public final ComponentName startService(@NonNull Class cls, @NonNull Bundle bundle) {
        return mActivity.startService(cls, bundle);
    }
    //------------------------------------子类可使用的工具函数 -> IFrameView

    /**
     * 通过viewId获取控件
     */
    @Override
    public final <E extends View> E getView(@IdRes int viewId) {
        View view = mChildViews.get(viewId);
        if (view == null) {
            view = mRootView.findViewById(viewId);
            mChildViews.put(viewId, view);
        }
        return (E) view;
    }

    /**
     * 设置内容视图
     */
    @Override
    public final void setContentView(@LayoutRes int layoutResId) {
        View view = LayoutInflater.from(mActivity).inflate(layoutResId, null);
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
        View view = LayoutInflater.from(mActivity).inflate(layoutResId, mTitleLayout, false);
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
            FrameActivityFragmentViewHelper.layoutCancelInOutAnimation(mActivity, mContentLayout, mContentLayout, mLoadingLayout, mErrorLayout);
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
            mLoadingView = FrameActivityFragmentViewHelper.getLoadingView(mActivity);
        } else {
            mLoadingView = pView;
        }
        mLoadingLayout.removeAllViews();
        mLoadingLayout.addView(mLoadingView);
        if (animat) {
            FrameActivityFragmentViewHelper.layoutCancelInOutAnimation(mActivity, mLoadingLayout, mContentLayout, mLoadingLayout, mErrorLayout);
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
            mErrorView = FrameActivityFragmentViewHelper.getErrorView(mActivity);
        } else {
            mErrorView = pView;
        }
        mErrorLayout.removeAllViews();
        mErrorLayout.addView(mErrorView);
        mErrorLayout.setOnClickListener(pListener);
        if (animat) {
            FrameActivityFragmentViewHelper.layoutCancelInOutAnimation(mActivity, mErrorLayout, mContentLayout, mLoadingLayout, mErrorLayout);
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
        return Utils.dp2px(mActivity, dp);
    }

    /**
     * 根据手机的分辨率从 px(像素)的单位转成 dp
     */
    @Override
    public final float px2dp(@FloatRange(from = 0) float px) {
        return Utils.px2dp(mActivity, px);
    }

    /**
     * 根据手机的分辨率从 sp 的单位转成 px(像素)
     */
    @Override
    public final float sp2px(@FloatRange(from = 0) float sp) {
        return Utils.sp2px(mActivity, sp);
    }

    /**
     * 根据手机的分辨率从 px(像素)的单位转成 sp
     */
    @Override
    public final float px2sp(@FloatRange(from = 0) float px) {
        return Utils.px2sp(mActivity, px);
    }
    //------------------------------------子类可使用的工具函数 -> IBView

    /**
     * 统一获取上下文对象
     * 此函数返回Activity&Context实例
     * * 由mActivity统一管理,请勿调用getActivity获取
     */
    @Override
    public final Context getContext() {
        return mActivity;
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
            mLoadingDialog = new Dialog(mActivity);
            mLoadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            mLoadingDialog.setContentView(FrameActivityFragmentViewHelper.getLoadingDialogView(mActivity));
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
