package com.acmenxd.mvp.base;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.acmenxd.frame.basis.FramePresenter;
import com.acmenxd.frame.basis.IBView;
import com.acmenxd.mvp.net.IAllRequest;
import com.acmenxd.retrofit.NetManager;

import org.greenrobot.eventbus.Subscribe;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/24 14:46
 * @detail 顶级Presenter
 */
public abstract class BasePresenter<T extends IBView> extends FramePresenter<T> {
    /**
     * 构造器,传入BaseView实例
     *
     * @param pView
     */
    public BasePresenter(@NonNull T pView) {
        super(pView);
        // EventBus事件注册
        EventBusHelper.register(this);
    }

    /**
     * mView销毁时回调
     */
    @CallSuper
    public final void unSubscribe() {
        // EventBus事件反注册
        EventBusHelper.unregister(this);
        super.unSubscribe();
    }

    /**
     * EventBus默认添加的函数(子类无法重写,无需关心此函数)
     * * EventBus注册时,类中必须有@Subscribe注解的函数
     */
    @Subscribe
    private final void eventBusDefault(Object object) {
    }

    /**
     * 获取IAllRequest实例
     * * 开放重写,满足不同需求
     */
    public final IAllRequest request() {
        return NetManager.INSTANCE.commonRequest(IAllRequest.class);
    }

}
