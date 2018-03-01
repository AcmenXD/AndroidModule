package com.acmenxd.frame.basis.mvp;

import android.support.annotation.NonNull;

import com.acmenxd.frame.basis.FrameModel;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/16 16:06
 * @detail Presenter接口基类
 */
public interface IBPresenter extends IBMVP {
    void addModels(@NonNull FrameModel... pModels);
}
