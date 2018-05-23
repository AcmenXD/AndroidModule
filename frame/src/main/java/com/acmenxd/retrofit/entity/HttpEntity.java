package com.acmenxd.retrofit.entity;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/3 15:26
 * @detail 顶级解析实体类
 */
public class HttpEntity implements Serializable {

    private int code;
    private String msg;
    private String json;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(@NonNull String msg) {
        this.msg = msg;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String pJson) {
        json = pJson;
    }
}
