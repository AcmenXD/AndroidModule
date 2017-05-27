package com.acmenxd.bourse.presenter.mine;

import com.acmenxd.bourse.model.response.MineEntity;
import com.acmenxd.frame.basis.IBPresenter;
import com.acmenxd.frame.basis.IBView;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/16 16:07
 * @detail 我的MVP接口定义
 */
public interface IMine {
    interface IView extends IBView {
        void refreshRootView(MineEntity data);
    }

    interface IPresenter extends IBPresenter {
        void loadData();
    }
}
