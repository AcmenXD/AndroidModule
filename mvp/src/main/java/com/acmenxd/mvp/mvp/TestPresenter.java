package com.acmenxd.mvp.mvp;

import android.support.annotation.NonNull;

import com.acmenxd.frame.basis.RequestCallback;
import com.acmenxd.mvp.base.BasePresenter;
import com.acmenxd.mvp.model.response.TestEntity;
import com.acmenxd.retrofit.exception.HttpException;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/16 15:50
 * @detail 模拟登陆的Presenter实现
 */
public class TestPresenter extends BasePresenter implements ITest.IPresenter {
    private ITest.IView mView;
    private ITest.IModel mModel;

    /**
     * 构造器,传入IBView实例
     *
     * @param pView
     */
    public TestPresenter(@NonNull ITest.IView pView) {
        super(pView);
        mView = pView;
        mModel = new TestModel(this);
    }

    @Override
    public void login() {
        mModel.doLogin(new RequestCallback<TestEntity>(mView) {
            @Override
            public void succeed(@NonNull TestEntity pData) {
                super.succeed(pData);
                int code = pData.getCode();
                String msg = pData.getMsg();
                mView.loginSuccess(pData);
            }

            @Override
            public void failed(@NonNull HttpException pE) {
                super.failed(pE);
                int code = pE.getCode();
                String msg = pE.getMsg();
            }
        });
    }
}
