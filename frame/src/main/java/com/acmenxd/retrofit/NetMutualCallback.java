package com.acmenxd.retrofit;

import java.util.Map;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/7/6 16:41
 * @detail Retrofit公共参数回调类
 */
public abstract class NetMutualCallback {
    /**
     * 公共body参数
     */
    public abstract Map<String, String> getBodys(String url);

    /**
     * 请求公共参数
     */
    public abstract Map<String, String> getParameters(String url);

    /**
     * 公共Header
     */
    public abstract Map<String, String> getHeaders(String url);

    /**
     * 公共Header - 允许相同key值的header存在
     */
    public abstract Map<String, String> getReHeaders(String url);
}
