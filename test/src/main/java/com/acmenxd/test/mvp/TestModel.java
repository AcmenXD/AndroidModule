package com.acmenxd.test.mvp;

import com.acmenxd.frame.basis.RequestCallback;
import com.acmenxd.appbase.base.BaseModel;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/8/3 11:45
 * @detail 模拟登陆的Model实现
 */
public class TestModel extends BaseModel implements ITest.IModel {
    /**
     * 构造器,传入IBPresenter实例
     */
    public TestModel(ITest.IPresenter pIBPresenter) {
        super(pIBPresenter);
    }

    public void doLogin(final RequestCallback pCallback) {
        request().get("param").enqueue(pCallback);
    }
}
