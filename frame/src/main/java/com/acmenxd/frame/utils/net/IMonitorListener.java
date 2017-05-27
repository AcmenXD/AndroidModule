package com.acmenxd.frame.utils.net;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:07
 * @detail 网络监控组件对外监听者
 */
public interface IMonitorListener {
    /**
     * 网络连接类型变化
     *
     * @see NetStatus
     */
    void onConnectionChange(NetStatus status);
}
