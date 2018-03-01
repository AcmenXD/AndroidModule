package com.acmenxd.mvp.base;

import com.acmenxd.frame.basis.FrameModel;
import com.acmenxd.frame.basis.mvp.IBPresenter;
import com.acmenxd.mvp.base.impl.IBaseNet;
import com.acmenxd.mvp.http.IRequest;

import org.greenrobot.eventbus.Subscribe;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/24 14:46
 * @detail 顶级Model
 */
public abstract class BaseModel extends FrameModel implements IBaseNet {
    /**
     * 构造器,传入IBPresenter实例
     */
    public BaseModel(IBPresenter pIBPresenter) {
        super(pIBPresenter);
        // EventBus事件注册
        EventBusHelper.register(this);
    }

    /**
     * mView销毁时回调
     */
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
