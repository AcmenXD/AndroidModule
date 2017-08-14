package com.acmenxd.frame.basis.impl;

import android.support.annotation.NonNull;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/7/28 16:28
 * @detail 支持Subscription的接口类
 */
public interface IFrameSubscription {
    /**
     * 添加Subscriptions
     */
    void addSubscriptions(@NonNull Subscription... pSubscriptions);

    /**
     * 获取CompositeSubscription实例
     */
    CompositeSubscription getCompositeSubscription();

    /**
     * 判断能否接收Response
     */
    boolean canReceiveResponse();
}
