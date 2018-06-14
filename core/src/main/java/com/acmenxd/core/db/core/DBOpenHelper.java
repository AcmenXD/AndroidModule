package com.acmenxd.core.db.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.acmenxd.logger.Logger;
import com.acmenxd.core.db.dao.DaoMaster;
import com.acmenxd.core.db.migrator.BaseMigratorHelper;

import org.greenrobot.greendao.database.Database;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/2/28 10:00
 * @detail 数据库升级
 */
public final class DBOpenHelper extends DaoMaster.OpenHelper {
    private final String TAG = this.getClass().getSimpleName();

    public DBOpenHelper(@NonNull Context context, @NonNull String dbName, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, dbName, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        //DBManager.getInstance().onUpgrade(db);
        /**
         * * 数据库版本号不能降低,会导致App无法安装(newVersion < oldVersion)
         * 循环数据库版本,更新各版本数据结构差异
         */
        if (newVersion > oldVersion) {
            for (int i = oldVersion; i < newVersion; i++) {
                try {
                    BaseMigratorHelper migratorHelper = (BaseMigratorHelper) Class
                            .forName("com.acmenxd.core.db.migrator.MigratorHelper" + (i + 1)).newInstance();
                    if (migratorHelper != null) {
                        migratorHelper.onUpgrade(db);
                    }
                } catch (ClassNotFoundException | ClassCastException | IllegalAccessException | InstantiationException pE) {
                    Logger.e(pE);
                }
            }
        }
    }
}
