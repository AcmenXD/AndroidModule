package com.acmenxd.mvp.base;

import android.support.annotation.CallSuper;

import com.acmenxd.frame.basis.FrameActivity;
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
public abstract class BaseActivity extends FrameActivity implements INetBase{

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        // EventBus事件注册
        EventBusHelper.register(this);
    }

    @CallSuper
    @Override
    protected void onDestroy() {
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
        return NetManager.INSTANCE.commonRequest(IAllRequest.class);
    }

}
