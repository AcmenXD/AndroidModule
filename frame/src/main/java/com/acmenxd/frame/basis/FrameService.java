package com.acmenxd.frame.basis;

import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;

import com.acmenxd.frame.R;
import com.acmenxd.frame.basis.impl.IFrameNet;
import com.acmenxd.frame.basis.impl.IFrameStart;
import com.acmenxd.frame.basis.impl.IFrameSubscription;
import com.acmenxd.frame.basis.impl.IFrameUtils;
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
 * @date 2017/6/14 9:53
 * @detail Service基类
 */
public abstract class FrameService extends Service implements IFrameSubscription, IFrameStart, IFrameNet, IFrameUtils, IBView {
    protected final String TAG = this.getClass().getSimpleName();

    // 统一管理Presenters
    private List<FramePresenter> mPresenters;
    // 统一持有Subscription
    private CompositeSubscription mSubscription;
    // 布局容器
    private Dialog mLoadingDialog;
    // 网络状态监控
    private IMonitorListener mNetListener = new IMonitorListener() {
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
    //------------------------------------子类可使用的工具函数 -> 私有

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
    public final Bundle getBundle(Intent pIntent) {
        Bundle bundle = pIntent.getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        return bundle;
    }

    /**
     * 获取Intent中数据参数
     */
    @Override
    public final Bundle getBundle() {
        return new Bundle();
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
            mLoadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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
