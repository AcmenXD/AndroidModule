package com.acmenxd.frame.basis;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
 * @date 2016/12/16 16:01
 * @detail Activity基类
 */
public abstract class FrameActivity extends AppCompatActivity implements IActivityFragment, INet {
    protected final String TAG = this.getClass().getSimpleName();

    // 统一持有Subscription
    private CompositeSubscription mSubscription;
    // 统一管理Presenters
    private List<FramePresenter> mPresenters;
    // 存储子控件
    private SparseArray<View> mChildViews;
    // 布局容器
    private LinearLayout mContentLayout;
    private FrameLayout mLoadingLayout;
    private FrameLayout mErrorLayout;
    private FrameActivityFragmentOtherLayout mOtherLayout;
    private Dialog mLoadingDialog;
    // 视图view
    private View mContentView;
    private View mLoadingView;
    private View mErrorView;
    // 页面是否关闭(包含正在关闭)
    private boolean isFinish = false;
    // 网络状态监控
    IMonitorListener mNetListener = new IMonitorListener() {
        @Override
        public void onConnectionChange(@NonNull NetStatus status) {
            onNetStatusChange(status);
        }
    };
    private View mStatusBar; // 状态栏
    private boolean canCustomStatusBar = false; // 能否自定义statusBar
    protected int statusBarHeight = 0; // statusBar高度
    protected boolean skipStatusBar = false; // 是否跳过填充statusBar
    public final int statusBarResId = R.drawable.status_bar_color; // statusBar填充的色值
    public final float statusBarAlpha = 1f; // statusBar透明度

    @Deprecated
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @CallSuper
    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onCreate(savedInstanceState);
        // 子类onCreate之前调用
        onCreateBefore(savedInstanceState);
        // 设置base视图
        super.setContentView(R.layout.activity_base);
        // 初始化容器
        mSubscription = getCompositeSubscription();
        mPresenters = new ArrayList<>();
        mChildViews = new SparseArray<>();
        // 获取布局容器
        mContentLayout = getView(R.id.activity_base_contentLayout);
        mLoadingLayout = getView(R.id.activity_base_loadingLayout);
        mErrorLayout = getView(R.id.activity_base_errorLayout);
        mOtherLayout = getView(R.id.activity_base_otherLayout);
        // 设置默认的加载视图
        setLoadingView(FrameActivityFragmentViewHelper.getLoadingView(this));
        // 设置默认的错误视图
        setErrorView(FrameActivityFragmentViewHelper.getErrorView(this));
        // 默认显示加载视图
        showContentView();
        // 将此Activity添加到ActivityStackManager中管理
        ActivityStackManager.INSTANCE.addActivity(this);
        // 修改状态栏高度
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            canCustomStatusBar = true;
        }
        if (canCustomStatusBar) {
            mStatusBar = getView(R.id.activity_base_statusBar);
            mStatusBar.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight));
            setStatusBarResId(statusBarResId);
            setStatusBarAlpha(statusBarAlpha);
        }
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        // 将此Activity在ActivityStackManager中移除
        ActivityStackManager.INSTANCE.removeActivity(this);
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        isFinish = false;
        // 网络监控注册
        Monitor.registListener(mNetListener);
    }

    /**
     * 为了统一Activity&Fragment 在 Presenter&Model中获取上下文对象
     */
    public Context getContext() {
        return this;
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
    //------------------------------------子类可使用的工具函数 & 继承自IActivityFragment接口

    /**
     * statusBar填充的色值
     */
    public void setStatusBarResId(int pStatusBarResId) {
        if (canCustomStatusBar) {
            mStatusBar.setBackgroundResource(pStatusBarResId);
        }
    }

    /**
     * statusBar透明度
     */
    public void setStatusBarAlpha(float pStatusBarAlpha) {
        if (canCustomStatusBar) {
            mStatusBar.setAlpha(pStatusBarAlpha);
        }
    }

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
                isCancelable = setting[0];
            }
            if (setting.length >= 2) {
                isCanceledOnTouchOutside = setting[1];
            }
        }
        if (mLoadingDialog == null) {
            mLoadingDialog = new Dialog(this, R.style.Translucent_Dialog);
        }
        mLoadingDialog.setContentView(FrameActivityFragmentViewHelper.getDialogView(this));
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
        View view = LayoutInflater.from(this).inflate(layoutResId, null);
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
        if (!skipStatusBar && canCustomStatusBar) {
            View statusBar = new View(this);
            statusBar.setBackgroundDrawable(mContentView.getBackground());
            statusBar.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, statusBarHeight));
            mContentLayout.addView(statusBar);
        }
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
        showLoadingView(false);
    }

    @Override
    public final void showLoadingView(boolean animat) {
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
        showErrorView(false);
        mErrorLayout.setOnClickListener(null);
    }

    @Override
    public final void showErrorView(boolean animat) {
        if (animat) {
            FrameActivityFragmentViewHelper.layoutCancelInOutAnimation(this, mErrorLayout, mContentLayout, mLoadingLayout, mErrorLayout);
        } else {
            FrameActivityFragmentViewHelper.layouts$setVisibility(mErrorLayout, mContentLayout, mLoadingLayout, mErrorLayout);
        }
        mErrorLayout.setOnClickListener(null);
    }

    @Override
    public final void showErrorView(View.OnClickListener pListener) {
        showErrorView(false);
        mErrorLayout.setOnClickListener(pListener);
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
            view = this.findViewById(viewId);
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

}
