package com.acmenxd.core.base;

import android.support.annotation.CallSuper;

import com.acmenxd.frame.basis.FrameService;
import com.acmenxd.core.base.impl.IBaseNet;
import com.acmenxd.core.http.IRequest;

import org.greenrobot.eventbus.Subscribe;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/24 14:35
 * @detail 顶级Service
 */
public abstract class BaseService extends FrameService implements IBaseNet {

    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();
        // EventBus事件注册
        EventBusHelper.register(this);
    }

    @CallSuper
    @Override
    public void onDestroy() {
        // EventBus事件反注册
        EventBusHelper.unregister(this);
        super.onDestroy();
    }

    /**
     * EventBus默认添加的函数(子类无法重写,无需关心此函数)
     * * EventBus注册时,类中必须有@Subscribe注解的函数
     */
    @Subscribe
    public final void eventBusDefault(Object object) {
    }
    //------------------------------------子类可使用的工具函数 -> IBaseNet

    /**
     * 获取IRequest实例
     * * 开放重写,满足不同需求
     */
    @Override
    public IRequest request() {
        return request(IRequest.class);
    }

    /**
     * 创建新的Retrofit实例
     * * 开放重写,满足不同需求
     */
    @Override
    public IRequest newRequest() {
        return newRequest(IRequest.class);
    }

    /**
     * 创建新的Retrofit实例,并设置超时时间
     * * 开放重写,满足不同需求
     */
    @Override
    public IRequest newRequest(int connectTimeout, int readTimeout, int writeTimeout) {
        return newRequest(IRequest.class, connectTimeout, readTimeout, writeTimeout);
    }
}
