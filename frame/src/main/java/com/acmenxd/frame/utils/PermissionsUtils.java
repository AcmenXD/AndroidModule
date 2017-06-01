package com.acmenxd.frame.utils;

import android.app.Activity;

import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/6/1 16:24
 * @detail 权限控制工具类
 */
public class PermissionsUtils {

    public interface Callback {
        void result(String permissionName, boolean result);
    }

    public interface CallbackGroup {
        void result(boolean result);
    }

    /**
     * 同时请求多个权限,分别获取授权结果
     * @param pActivity
     * @param pCallback
     * @param permissions
     */
    public static void requestPermissions(final Activity pActivity, final Callback pCallback, final String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(pActivity);
        rxPermissions.requestEach(permissions)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {// android 6.0之前会默认返回true
                        if (pCallback != null) {
                            pCallback.result(permission.name, permission.granted);
                        }
                    }
                });
    }

    /**
     * 同时请求多个权限,合并获取授权结果 -> 即所有权限请求成功会返回true，若有一个权限未成功则返回false
     * @param pActivity
     * @param pCallbackGroup
     * @param permissions
     */
    public static void requestPermissions(final Activity pActivity, final CallbackGroup pCallbackGroup, final String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(pActivity);
        rxPermissions.request(permissions)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {// android 6.0之前会默认返回true
                        if (pCallbackGroup != null) {
                            pCallbackGroup.result(granted);
                        }
                    }
                });
    }
}
