package com.acmenxd.frame.basis;

import com.acmenxd.retrofit.callback.HttpCallback;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/8/3 11:20
 * @detail 网络请求返回后, 处理回调
 */
public abstract class RequestCallback<T> extends HttpCallback<T> {
    private IBView mIBView;

    /**
     * 设置LoadingDialog参数
     *
     * @param setting 数组下标 ->
     *                0.是否显示LoadingDialog(默认false)
     *                1.isCancelable(是否可以通过点击Back键取消)(默认true)
     *                2.isCanceledOnTouchOutside(是否在点击Dialog外部时取消Dialog)(默认false)
     */
    public RequestCallback(IBView pIBView, boolean... setting) {
        mIBView = pIBView;
        mIBView.showLoadingDialogBySetting(setting);
    }

    @Deprecated
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (mIBView.canReceiveResponse()) {
            super.onResponse(call, response);
        }
        mIBView.hideLoadingDialog();
    }

    @Deprecated
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (mIBView.canReceiveResponse()) {
            super.onFailure(call, t);
        }
        mIBView.hideLoadingDialog();
    }
}
