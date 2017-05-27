package com.acmenxd.bourse.presenter.home;

import com.acmenxd.bourse.model.response.HomeEntity;
import com.acmenxd.frame.basis.IBPresenter;
import com.acmenxd.frame.basis.IBView;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/16 16:07
 * @detail 首页MVP接口定义
 */
public interface IHome {
    interface IView extends IBView {
        void refreshRootView(HomeEntity data);
    }

    interface IPresenter extends IBPresenter {
        void loadData();
    }
}
