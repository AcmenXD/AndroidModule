package com.acmenxd.frame.basis;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.SpannableString;

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
 * @date 2017/6/14 9:53
 * @detail something
 */
public abstract class FrameService extends Service {
    protected final String TAG = this.getClass().getSimpleName();

    // 统一持有Subscription
    private CompositeSubscription mSubscription;
    // 统一管理Presenters
    private List<FramePresenter> mPresenters;
    // 网络状态监控
    IMonitorListener mNetListener = new IMonitorListener() {
        @Override
        public void onConnectionChange(@NonNull NetStatus status) {
            onNetStatusChange(status);
        }
    };

    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化容器
        mSubscription = getCompositeSubscription();
        mPresenters = new ArrayList<>();
        // 网络监控注册
        Monitor.registListener(mNetListener);
    }

    @CallSuper
    @Override
    public void onDestroy() {
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
    }

    //------------------------------------子类可重写的函数

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
     * 获取CompositeSubscription实例
     */
    public final CompositeSubscription getCompositeSubscription() {
        if (mSubscription == null) {
            mSubscription = new CompositeSubscription();
        }
        return mSubscription;
    }

    /**
     * 获取Intent中数据参数
     */
    public final Bundle getBundle(Intent pIntent) {
        Bundle bundle = pIntent.getExtras();
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
    public final void startActivity(@NonNull Class cls) {
        Intent intent = new Intent(this, cls);
        super.startActivity(intent);
    }

    /**
     * 启动Activity
     */
    public final void startActivity(@NonNull Class cls, @NonNull Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        super.startActivity(intent);
    }

    /**
     * 启动Activity
     */
    public final void startActivity(@NonNull Class cls, @NonNull Bundle bundle, int flags) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        intent.setFlags(flags);
        super.startActivity(intent);
    }

    /**
     * 根据IRequest类获取Request实例
     */
    public final <T> T request(@NonNull Class<T> pIRequest) {
        return NetManager.INSTANCE.request(pIRequest);
    }

    /**
     * 创建新的Retrofit实例
     * 根据IRequest类获取Request实例
     */
    public final <T> T newRequest(@NonNull Class<T> pIRequest) {
        return NetManager.INSTANCE.newRequest(pIRequest);
    }

    /**
     * 创建新的Retrofit实例,并设置超时时间
     * 根据IRequest类获取Request实例
     */
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
    public final <T> Callback<T> newCallback(@NonNull final NetCallback<T> pCallback, final boolean... setting) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (!mSubscription.isUnsubscribed()) {
                    pCallback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (!mSubscription.isUnsubscribed()) {
                    pCallback.onFailure(call, t);
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
    public final <T> Subscriber<T> newSubscriber(@NonNull final NetSubscriber<T> pSubscriber, final boolean... setting) {
        return new Subscriber<T>() {
            @Override
            public void onCompleted() {
                if (!mSubscription.isUnsubscribed()) {
                    pSubscriber.onCompleted();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (!mSubscription.isUnsubscribed()) {
                    pSubscriber.onError(e);
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
    //------------------------------------子类可使用的工具函数 & 继承自IActivityFragment接口

    /**
     * 串拼接
     *
     * @param strs 可变参数类型
     * @return 拼接后的字符串
     */
    public final String appendStrs(@NonNull Object... strs) {
        return Utils.appendStrs(strs);
    }

    /**
     * 串变化 -> 大小&颜色
     *
     * @param start 从0开始计数(包含start)
     * @param end   从1开始计数(包含end)
     */
    public final SpannableString changeStr(@NonNull String str, @IntRange(from = 0) int start, @IntRange(from = 0) int end, @IntRange(from = 0) int dip, @ColorInt int color) {
        return Utils.changeStr(str, start, end, dip, color);
    }

    public final SpannableString changeStr(@NonNull SpannableString spannableString, @IntRange(from = 0) int start, @IntRange(from = 0) int end, @IntRange(from = 0) int dip, @ColorInt int color) {
        return Utils.changeStr(spannableString, start, end, dip, color);
    }

    /**
     * 根据手机的分辨率从 dp 的单位转成 px(像素)
     */
    public final float dp2px(@FloatRange(from = 0) float dp) {
        return Utils.dp2px(this, dp);
    }

    /**
     * 根据手机的分辨率从 px(像素)的单位转成 dp
     */
    public final float px2dp(@FloatRange(from = 0) float px) {
        return Utils.px2dp(this, px);
    }

    /**
     * 根据手机的分辨率从 sp 的单位转成 px(像素)
     */
    public final float sp2px(@FloatRange(from = 0) float sp) {
        return Utils.sp2px(this, sp);
    }

    /**
     * 根据手机的分辨率从 px(像素)的单位转成 sp
     */
    public final float px2sp(@FloatRange(from = 0) float px) {
        return Utils.px2sp(this, px);
    }

}
