package com.acmenxd.mvp.base;

import android.support.annotation.CallSuper;

import com.acmenxd.frame.basis.FrameService;
import com.acmenxd.mvp.net.IAllRequest;
import com.acmenxd.retrofit.NetManager;

import org.greenrobot.eventbus.Subscribe;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/24 14:35
 * @detail 顶级Activity
 */
public abstract class BaseService extends FrameService implements INetBase{

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

    /**
     * 获取IAllRequest实例
     */
    @Override
    public final IAllRequest request() {
        return request(IAllRequest.class);
    }

}
