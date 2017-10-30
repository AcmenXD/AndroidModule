package com.acmenxd.mvp.base.impl;

import com.acmenxd.frame.basis.impl.IFrameNet;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/6/26 11:34
 * @detail 支持Retrofit的接口类
 */
public interface IBaseNet extends IFrameNet {
    /**
     * 获取IRequest实例
     */
    <T> T request();

    /**
     * 创建新的Retrofit实例
     * 根据IRequest类获取Request实例
     */
    <T> T newRequest();

    /**
     * 创建新的Retrofit实例,并设置超时时间
     * 根据IRequest类获取Request实例
     */
    <T> T newRequest(int connectTimeout, int readTimeout, int writeTimeout);
}
