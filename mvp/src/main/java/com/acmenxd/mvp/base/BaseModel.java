package com.acmenxd.mvp.base;

import android.support.annotation.CallSuper;

import com.acmenxd.frame.basis.FrameModel;
import com.acmenxd.frame.basis.FramePresenter;
import com.acmenxd.mvp.net.IAllRequest;
import com.acmenxd.retrofit.NetManager;

import org.greenrobot.eventbus.Subscribe;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/24 14:46
 * @detail 顶级Model
 */
public abstract class BaseModel extends FrameModel implements INetBase{
    /**
     * 构造器,传入FramePresenter实例
     *
     * @param pFramePresenter
     */
    public BaseModel(FramePresenter pFramePresenter) {
        super(pFramePresenter);
    }

    /**
     * mView销毁时回调
     */
    @CallSuper
    @Override
    public void unSubscribe() {
        // EventBus事件反注册
        EventBusHelper.unregister(this);
        super.unSubscribe();
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
