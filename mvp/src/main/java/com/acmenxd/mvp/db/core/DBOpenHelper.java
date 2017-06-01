package com.acmenxd.mvp.db.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.acmenxd.logger.Logger;
import com.acmenxd.mvp.db.dao.DaoMaster;
import com.acmenxd.mvp.db.migrator.BaseMigratorHelper;
import com.acmenxd.mvp.db.migrator.MigratorHelper1;

import org.greenrobot.greendao.database.Database;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/2/28 10:00
 * @detail 数据库升级
 */
public class DBOpenHelper extends DaoMaster.OpenHelper {
    private final String TAG = this.getClass().getSimpleName();

    public DBOpenHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory) {
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
                            .forName("com.acmenxd.mvp.db.migrator.MigratorHelper" + (i + 1)).newInstance();
                    if (migratorHelper != null) {
                        migratorHelper.onUpgrade(db);
                    }
                } catch (ClassNotFoundException | ClassCastException | IllegalAccessException | InstantiationException pE) {
                    Logger.e(pE);
                }
            }
            MigratorHelper1 helper1 = new MigratorHelper1();
            helper1.toString();
        }
    }
}
