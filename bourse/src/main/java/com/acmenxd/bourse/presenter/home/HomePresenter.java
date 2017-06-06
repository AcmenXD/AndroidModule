package com.acmenxd.bourse.presenter.home;

import com.acmenxd.bourse.base.BasePresenter;
import com.acmenxd.bourse.model.response.HomeEntity;
import com.acmenxd.bourse.model.response.TestEntity;
import com.acmenxd.frame.utils.RxUtils;
import com.acmenxd.retrofit.NetEntity;
import com.acmenxd.retrofit.callback.NetCallback;
import com.acmenxd.retrofit.exception.NetException;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/16 15:50
 * @detail 首页Presenter
 */
public class HomePresenter extends BasePresenter<IHome.IView> implements IHome.IPresenter {
    public HomePresenter(IHome.IView pView) {
        super(pView);
    }

    public void login() {
        request().get("token").enqueue(newCallback(new NetCallback<NetEntity<TestEntity>>() {
            @Override
            public void succeed(NetEntity<TestEntity> pData) {
                int code = pData.getCode();
                String msg = pData.getMsg();
                TestEntity data = pData.getData();
            }

            @Override
            public void failed(NetException pE) {
                int code = pE.getCode();
                String msg = pE.getMsg();
                String toastMsg = pE.getToastMsg();
            }
        }));
    }

    /**
     * 模拟测试数据
     */
    private HomeEntity mHomeEntity;

    @Override
    public void loadData() {
        addSubscriptions(Observable
                .just("模拟测试数据")
                .timer(1, TimeUnit.SECONDS)
                .compose(RxUtils.<Long>applySchedulers())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long pLong) {
                        if (mHomeEntity == null) {
                            mHomeEntity = new HomeEntity();
                        }
                        mView.refreshRootView(mHomeEntity);
                    }
                }));
    }
}
