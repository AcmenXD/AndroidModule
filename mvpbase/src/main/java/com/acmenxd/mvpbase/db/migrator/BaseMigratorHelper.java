package com.acmenxd.mvpbase.db.migrator;

import android.support.annotation.NonNull;

import com.acmenxd.frame.utils.proguard.IKeepClass;

import org.greenrobot.greendao.database.Database;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/2/28 10:00
 * @detail 更新数据库版本基类
 */
public abstract class BaseMigratorHelper implements IKeepClass {
    public abstract void onUpgrade(@NonNull Database db);
}
