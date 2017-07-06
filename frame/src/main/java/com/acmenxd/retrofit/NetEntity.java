package com.acmenxd.retrofit;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/3 15:26
 * @detail 顶级解析实体类
 */
public class NetEntity<T> extends BaseEntity implements Serializable {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(@NonNull T pData) {
        data = pData;
    }
}
