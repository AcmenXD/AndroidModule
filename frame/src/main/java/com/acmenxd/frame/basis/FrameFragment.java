package com.acmenxd.frame.basis;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.acmenxd.frame.R;
import com.acmenxd.frame.utils.Utils;
import com.acmenxd.frame.utils.net.IMonitorListener;
import com.acmenxd.frame.utils.net.Monitor;
import com.acmenxd.frame.utils.net.NetStatus;
import com.acmenxd.retrofit.NetManager;
import com.acmenxd.retrofit.callback.NetCallback;
import com.acmenxd.retrofit.callback.NetSubscriber;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/14 15:32
 * @detail Fragmeng基类
 */
public abstract class FrameFragment extends Fragment implements IActivityFragment, INet {
    protected final String TAG = this.getClass().getSimpleName();

    // Activity实例
    protected FrameActivity mActivity;
    // 统一持有Subscription
    private CompositeSubscription mSubscription;
    // 统一管理Presenters
    private List<FramePresenter> mPresenters;
    // 存储子控件
    private SparseArray<View> mChildViews;
    // 顶级视图View
    private View mRootView;
    // 布局容器
    private FrameLayout mContentLayout;
    private FrameLayout mLoadingLayout;
    private FrameLayout mErrorLayout;
    private FrameActivityFragmentOtherLayout mOtherLayout;
    private Dialog mLoadingDialog;
    // 视图view
    private View mContentView;
    private View mLoadingView;
    private View mErrorView;
    // Fragment取消预加载后的显隐处理
    private boolean viewPagerFragmentVisible;
    // Fragment取消预加载后的首次显示处理
    private boolean viewPagerFragmentFirstVisible;
    // Fragment取消预加载后的显示计数(第一次显示计数为1)
    private int viewPagerFragmentVisibleIndex;
    // 页面是否关闭(包含正在关闭)
    private boolean isFinish = false;
    // 网络状态监控
    IMonitorListener mNetListener = new IMonitorListener() {
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
        mRootView = inflater.inflate(R.layout.activity_base, container, false);
        // 获取布局容器
        mContentLayout = getView(R.id.activity_base_contentLayout);
        mLoadingLayout = getView(R.id.activity_base_loadingLayout);
        mErrorLayout = getView(R.id.activity_base_errorLayout);
        mOtherLayout = getView(R.id.activity_base_otherLayout);
        // 设置内容视图
        setContentView(onCreateView(LayoutInflater.from(mActivity), savedInstanceState));
        // 设置默认的加载视图
        setLoadingView(FrameActivityFragmentViewHelper.getLoadingView(mActivity));
        // 设置默认的错误视图
        setErrorView(FrameActivityFragmentViewHelper.getErrorView(mActivity));
        // 默认显示加载视图
        showContentView();
        return mRootView;
    }

    @CallSuper
    @Override
    public void onDetach() {
        super.onDetach();
        isFinish = true;
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
        isFinish = false;
        // 网络监控注册
        Monitor.registListener(mNetListener);
    }

    /**
     * 此函数返回Activity&Context实例
     * * 由mActivity统一管理,请勿调用getActivity获取
     */
    @Override
    public final Context getContext() {
        return mActivity;
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
    //------------------------------------子类可使用的工具函数 & 继承自IActivityFragment接口

    /**
     * 退出应用程序
     */
    @Override
    public final void exit() {
        ActivityStackManager.INSTANCE.exit();
    }

    /**
     * 添加Subscriptions
     */
    @Override
    public final void addSubscriptions(@NonNull Subscription... pSubscriptions) {
        getCompositeSubscription().addAll(pSubscriptions);
    }

    /**
     * 添加Presenters
     */
    @Override
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
     * 根据IRequest类获取Request实例
     */
    @Override
    public final <T> T request(@NonNull Class<T> pIRequest) {
        return NetManager.INSTANCE.request(pIRequest);
    }

    /**
     * 创建新的Retrofit实例
     * 根据IRequest类获取Request实例
     */
    @Override
    public final <T> T newRequest(@NonNull Class<T> pIRequest) {
        return NetManager.INSTANCE.newRequest(pIRequest);
    }

    /**
     * 创建新的Retrofit实例,并设置超时时间
     * 根据IRequest类获取Request实例
     */
    @Override
    public final <T> T newRequest(@IntRange(from = 0) int connectTimeout, @IntRange(from = 0) int readTimeout, @IntRange(from = 0) int writeTimeout, @NonNull Class<T> pIRequest) {
        return NetManager.INSTANCE.newRequest(connectTimeout, readTimeout, writeTimeout, pIRequest);
    }

    /**
     * 统一处理因异步导致的 Activity|Fragment销毁时发生NullPointerException问题
     *
     * @param pCallback Net请求回调
     * @param setting   数组下标 ->
     *                  0.是否显示LoadingDialog(默认false)
     *                  1.isCancelable(是否可以通过点击Back键取消)(默认true)
     *                  2.isCanceledOnTouchOutside(是否在点击Dialog外部时取消Dialog)(默认false)
     */
    @Override
    public final <T> Callback<T> newCallback(@NonNull final NetCallback<T> pCallback, final boolean... setting) {
        showLoadingDialogBySetting(setting);
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (!mSubscription.isUnsubscribed()) {
                    pCallback.onResponse(call, response);
                }
                hideLoadingDialog();
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (!mSubscription.isUnsubscribed()) {
                    pCallback.onFailure(call, t);
                }
                hideLoadingDialog();
            }
        };
    }

    /**
     * 统一处理因异步导致的 Activity|Fragment销毁时发生NullPointerException问题
     *
     * @param pSubscriber Net请求回调
     * @param setting     数组下标 ->
     *                    0.是否显示LoadingDialog(默认false)
     *                    1.isCancelable(是否可以通过点击Back键取消)(默认true)
     *                    2.isCanceledOnTouchOutside(是否在点击Dialog外部时取消Dialog)(默认false)
     */
    @Override
    public final <T> Subscriber<T> newSubscriber(@NonNull final NetSubscriber<T> pSubscriber, final boolean... setting) {
        showLoadingDialogBySetting(setting);
        return new Subscriber<T>() {
            @Override
            public void onCompleted() {
                if (!mSubscription.isUnsubscribed()) {
                    pSubscriber.onCompleted();
                }
                hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                if (!mSubscription.isUnsubscribed()) {
                    pSubscriber.onError(e);
                }
                hideLoadingDialog();
            }

            @Override
            public void onNext(T pT) {
                if (!mSubscription.isUnsubscribed()) {
                    pSubscriber.onNext(pT);
                }
            }
        };
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
                isCancelable = setting[1];
            }
            if (setting.length >= 2) {
                isCanceledOnTouchOutside = setting[2];
            }
        }
        if (mLoadingDialog == null) {
            mLoadingDialog = new Dialog(mActivity, R.style.Translucent_Dialog);
        }
        mLoadingDialog.setContentView(FrameActivityFragmentViewHelper.getDialogView(mActivity));
        mLoadingDialog.setCancelable(isCancelable);
        mLoadingDialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        mLoadingDialog.show();
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
    //------------------------------------ContentView|LoadingView|ErrorView相关操作 & 继承自IActivityFragment接口

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
        mContentLayout.removeAllViews();
        mContentLayout.addView(mContentView);
    }

    /**
     * 设置加载视图
     */
    @Override
    public final void setLoadingView(@NonNull View view) {
        if (view == null) {
            return;
        }
        mLoadingView = view;
        mLoadingLayout.removeAllViews();
        mLoadingLayout.addView(mLoadingView);
    }

    /**
     * 设置错误视图
     */
    @Override
    public final void setErrorView(@NonNull View view) {
        if (view == null) {
            return;

        }
        mErrorView = view;
        mErrorLayout.removeAllViews();
        mErrorLayout.addView(mErrorView);
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
        showLoadingView(false);
    }

    @Override
    public final void showLoadingView(boolean animat) {
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
        showErrorView(false);
    }

    @Override
    public final void showErrorView(boolean animat) {
        if (animat) {
            FrameActivityFragmentViewHelper.layoutCancelInOutAnimation(mActivity, mErrorLayout, mContentLayout, mLoadingLayout, mErrorLayout);
        } else {
            FrameActivityFragmentViewHelper.layouts$setVisibility(mErrorLayout, mContentLayout, mLoadingLayout, mErrorLayout);
        }
    }

    /**
     * 隐藏内容视图
     */
    @Override
    public final void hideContentView() {
        mContentLayout.setVisibility(View.GONE);
    }

    /**
     * 隐藏加载视图
     */
    @Override
    public final void hideLoadingView() {
        mLoadingLayout.setVisibility(View.GONE);
    }

    /**
     * 隐藏错误视图
     */
    @Override
    public final void hideErrorView() {
        mErrorLayout.setVisibility(View.GONE);
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
    //------------------------------------子类可使用的工具函数 & 继承自IActivityFragment接口

    /**
     * 通过viewId获取控件
     */
    @Override
    public final <T extends View> T getView(@IdRes int viewId) {
        View view = mChildViews.get(viewId);
        if (view == null) {
            view = mRootView.findViewById(viewId);
            mChildViews.put(viewId, view);
        }
        return (T) view;
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

}
