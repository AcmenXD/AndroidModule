package com.acmenxd.frame.basis;

import android.content.Context;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/16 16:06
 * @detail Activity|Fragment接口基类
 */
public interface IBView {
    /**
     * 统一获取上下文对象
     */
    Context getContext();

    /**
     * 判断能否接收Response
     */
    boolean canReceiveResponse();

    /**
     * 根据setting,检查是否显示LoadingDialog
     *
     * @param setting 数组下标 ->
     *                0.是否显示LoadingDialog(默认false)
     *                1.isCancelable(是否可以通过点击Back键取消)(默认true)
     *                2.isCanceledOnTouchOutside(是否在点击Dialog外部时取消Dialog)(默认false)
     */
    void showLoadingDialogBySetting(final boolean... setting);

    /**
     * 显示LoadingDialog
     *
     * @param setting 数组下标 ->
     *                0.isCancelable(是否可以通过点击Back键取消)(默认true)
     *                1.isCanceledOnTouchOutside(是否在点击Dialog外部时取消Dialog)(默认false)
     */
    void showLoadingDialog(final boolean... setting);

    /**
     * 隐藏LoadingDialog
     */
    void hideLoadingDialog();
}
