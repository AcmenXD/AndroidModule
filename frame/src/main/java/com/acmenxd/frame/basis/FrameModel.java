package com.acmenxd.frame.basis;

import android.support.annotation.CallSuper;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.acmenxd.frame.utils.net.IMonitorListener;
import com.acmenxd.frame.utils.net.Monitor;
import com.acmenxd.frame.utils.net.NetStatus;
import com.acmenxd.retrofit.NetManager;
import com.acmenxd.retrofit.callback.NetCallback;
import com.acmenxd.retrofit.callback.NetSubscriber;

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
 * @detail Model基类
 */
public abstract class FrameModel implements INet {
    protected final String TAG = this.getClass().getSimpleName();

    // Activity|Fragment实例
    protected FramePresenter mFramePresenter;
    // 统一持有Subscription
    private CompositeSubscription mSubscription;
    // 页面是否关闭(包含正在关闭)
    public boolean isFinish = false;
    // 网络状态监控
    IMonitorListener mNetListener = new IMonitorListener() {
        @Override
        public void onConnectionChange(@NonNull NetStatus status) {
            onNetStatusChange(status);
        }
    };

    /**
     * 构造器,传入FramePresenter实例
     */
    public FrameModel(@NonNull FramePresenter pFramePresenter) {
        isFinish = false;
        mFramePresenter = pFramePresenter;
        // 初始化容器
        mSubscription = getCompositeSubscription();
        // 网络监控注册
        Monitor.registListener(mNetListener);
    }

    /**
     * mView销毁时回调
     */
    @CallSuper
    public void unSubscribe() {
        isFinish = true;
        // 网络监控反注册
        Monitor.unRegistListener(mNetListener);
        //解绑 Subscriptions
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    //------------------------------------子类可重写的函数

    /**
     * 网络状态变换调用
     */
    @CallSuper
    protected void onNetStatusChange(@NonNull NetStatus pNetStatus) {
    }
    //------------------------------------工具函数

    /**
     * 退出应用程序
     */
    public final void exit() {
        ActivityStackManager.INSTANCE.exit();
    }

    /**
     * 添加Subscriptions
     */
    public final void addSubscriptions(@NonNull Subscription... pSubscriptions) {
        getCompositeSubscription().addAll(pSubscriptions);
    }

    /**
     * 获取CompositeSubscription实例
     */
    public final CompositeSubscription getCompositeSubscription() {
        if (mSubscription == null) {
            mSubscription = new CompositeSubscription();
        }
        return mSubscription;
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
        if (mFramePresenter != null && mFramePresenter.mView != null) {
            mFramePresenter.mView.showLoadingDialogBySetting(setting);
        }
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (!mSubscription.isUnsubscribed()) {
                    pCallback.onResponse(call, response);
                }
                if (mFramePresenter != null && mFramePresenter.mView != null) {
                    mFramePresenter.mView.hideLoadingDialog();
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (!mSubscription.isUnsubscribed()) {
                    pCallback.onFailure(call, t);
                }
                if (mFramePresenter != null && mFramePresenter.mView != null) {
                    mFramePresenter.mView.hideLoadingDialog();
                }
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
        if (mFramePresenter != null && mFramePresenter.mView != null) {
            mFramePresenter.mView.showLoadingDialogBySetting(setting);
        }
        return new Subscriber<T>() {
            @Override
            public void onCompleted() {
                if (!mSubscription.isUnsubscribed()) {
                    pSubscriber.onCompleted();
                }
                if (mFramePresenter != null && mFramePresenter.mView != null) {
                    mFramePresenter.mView.hideLoadingDialog();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (!mSubscription.isUnsubscribed()) {
                    pSubscriber.onError(e);
                }
                if (mFramePresenter != null && mFramePresenter.mView != null) {
                    mFramePresenter.mView.hideLoadingDialog();
                }
            }

            @Override
            public void onNext(T pT) {
                if (!mSubscription.isUnsubscribed()) {
                    pSubscriber.onNext(pT);
                }
            }
        };
    }
}