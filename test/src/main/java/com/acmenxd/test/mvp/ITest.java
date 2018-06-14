package com.acmenxd.test.mvp;

import com.acmenxd.frame.basis.RequestCallback;
import com.acmenxd.frame.basis.mvp.IBModel;
import com.acmenxd.frame.basis.mvp.IBPresenter;
import com.acmenxd.frame.basis.mvp.IBView;
import com.acmenxd.core.model.response.TestEntity;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/16 16:07
 * @detail 模拟登陆的MVP接口定义
 */
public interface ITest {
    interface IView extends IBView {
        void loginSuccess(TestEntity data);
    }

    interface IPresenter extends IBPresenter {
        void login();
    }

    interface IModel extends IBModel {
        void doLogin(final RequestCallback callback);
    }
}
