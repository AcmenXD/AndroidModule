package com.acmenxd.frame.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/23 16:09
 * @detail APK工具类
 */
public final class ApkUtils {

    /**
     * 安装一个apk文件
     */
    public static void install(@NonNull Context context, @NonNull File uriFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            data = Uri.fromFile(uriFile);
        } else {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            data = FileProvider.getUriForFile(context, "com.acmenxd.frame.fileprovider", uriFile);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 卸载一个app
     */
    public static void uninstall(@NonNull Context context, @NonNull String packageName) {
        //通过程序的包名创建URI
        Uri packageURI = Uri.parse("package:" + packageName);
        //创建Intent意图
        Intent intent = new Intent(Intent.ACTION_DELETE, packageURI);
        //执行卸载程序
        context.startActivity(intent);
    }

    /**
     * 检查手机上是否安装了指定的软件
     */
    public static boolean isAvailable(@NonNull Context context, @NonNull String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 检查手机上是否安装了指定的软件
     */
    public static boolean isAvailable(@NonNull Context context, @NonNull File file) {
        return isAvailable(context, getPackageName(context, file.getAbsolutePath()));
    }

    /**
     * 根据文件路径获取包名
     */
    public static String getPackageName(@NonNull Context context, @NonNull String filePath) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo info = packageManager.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            return appInfo.packageName;  //得到安装包名称
        }
        return null;
    }
}
