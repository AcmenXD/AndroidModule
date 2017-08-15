package com.acmenxd.mvp.presenter;

import android.support.annotation.NonNull;

import com.acmenxd.mvp.base.BasePresenter;
import com.acmenxd.mvp.model.response.TestHttpEntity;
import com.acmenxd.retrofit.exception.HttpException;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/16 15:50
 * @detail 模拟登陆的Presenter实现
 */
public class LoginPresenter extends BasePresenter<ILogin.IView> implements ILogin.IPresenter {

    /**
     * 构造器,传入BaseView实例
     *
     * @param pView
     */
    public LoginPresenter(@NonNull ILogin.IView pView) {
        super(pView);
    }

    @Override
    public void login() {
        request().get("token").enqueue(new BindCallback<TestHttpEntity>() {
            @Override
            public void succeed(@NonNull TestHttpEntity pData) {
                int code = pData.getCode();
                String msg = pData.getMsg();
                mView.loginSuccess(pData);
            }

            @Override
            public void failed(@NonNull HttpException pE) {
                int code = pE.getCode();
                String msg = pE.getMsg();
            }
        });
    }
}
