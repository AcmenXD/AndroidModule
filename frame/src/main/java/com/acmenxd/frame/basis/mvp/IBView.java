package com.acmenxd.frame.basis.mvp;

import android.support.annotation.NonNull;

import com.acmenxd.frame.basis.FramePresenter;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/16 16:06
 * @detail View接口基类
 */
public interface IBView extends IBMVP {
    void addPresenters(@NonNull FramePresenter... pPresenters);
}
