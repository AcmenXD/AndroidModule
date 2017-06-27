package com.acmenxd.mvp.base;

import com.acmenxd.frame.basis.INet;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/6/26 11:34
 * @detail 支持Retrofit的接口类
 */
public interface INetBase extends INet {
    /**
     * 获取IAllRequest实例
     */
    <T> T request();
}
