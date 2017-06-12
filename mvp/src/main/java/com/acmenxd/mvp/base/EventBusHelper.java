package com.acmenxd.mvp.base;

import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/4/10 16:40
 * @detail EventBus事件处理类
 */
public final class EventBusHelper {
    /**
     * 注册EventBus
     */
    public static void register(@NonNull Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    /**
     * 取消注册EventBus
     */
    public static void unregister(@NonNull Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    /**
     * 发布订阅事件
     */
    public static void post(@NonNull Object event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 发布粘性订阅事件
     */
    public static void postSticky(@NonNull Object event) {
        EventBus.getDefault().postSticky(event);
    }

    /**
     * 移除指定的粘性订阅事件
     */
    public static <T> void removeStickyEvent(@NonNull Class<T> eventType) {
        T stickyEvent = EventBus.getDefault().getStickyEvent(eventType);
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
    }

    /**
     * 取消事件传送
     */
    public static void cancelEventDelivery(@NonNull Object event) {
        EventBus.getDefault().cancelEventDelivery(event);
    }

    /**
     * 移除所有的粘性订阅事件
     */
    public static void removeAllStickyEvents() {
        EventBus.getDefault().removeAllStickyEvents();
    }
}
