package com.acmenxd.mvp.presenter;

import com.acmenxd.frame.basis.IBPresenter;
import com.acmenxd.frame.basis.IBView;
import com.acmenxd.mvp.model.response.TestEntity;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/16 16:07
 * @detail 模拟登陆的MVP接口定义
 */
public interface ILogin {
    interface IView extends IBView {
        void loginSuccess(TestEntity data);
    }

    interface IPresenter extends IBPresenter {
        void login();
    }
}
