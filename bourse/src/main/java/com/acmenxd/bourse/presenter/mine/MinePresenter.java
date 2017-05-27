package com.acmenxd.bourse.presenter.mine;

import com.acmenxd.bourse.base.BasePresenter;
import com.acmenxd.bourse.model.response.MineEntity;
import com.acmenxd.frame.utils.RxUtils;

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
public class MinePresenter extends BasePresenter<IMine.IView> implements IMine.IPresenter {
    public MinePresenter(IMine.IView pView) {
        super(pView);
    }

    /**
     * 模拟测试数据
     */
    private MineEntity mMineEntity;

    @Override
    public void loadData() {
        Subscription subscription = Observable
                .just("模拟测试数据")
                .timer(1, TimeUnit.SECONDS)
                .compose(RxUtils.<Long>applySchedulers())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long pLong) {
                        if (mMineEntity == null) {
                            mMineEntity = new MineEntity();
                        }
                        mView.refreshRootView(mMineEntity);
                    }
                });
        addSubscriptions(subscription);
    }
}
