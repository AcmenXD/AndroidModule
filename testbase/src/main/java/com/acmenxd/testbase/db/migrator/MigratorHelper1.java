package com.acmenxd.testbase.db.migrator;


import android.support.annotation.NonNull;

import com.acmenxd.testbase.db.core.MigrationHelperUtil;
import com.acmenxd.testbase.db.dao.TestBeanDao;

import org.greenrobot.greendao.database.Database;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/2/28 10:00
 * @detail 更新数据库版本 -> DB版本号 1
 */
public final class MigratorHelper1 extends BaseMigratorHelper {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onUpgrade(@NonNull Database db) {
        //更新数据库表结构
        MigrationHelperUtil.getInstance().migrate(db, new DefaultCallback() {

            @Override
            public String onText(@NonNull String tablename, @NonNull String columnName) {
                return null;
            }

            @Override
            public Long onInteger(@NonNull String tablename, @NonNull String columnName) {
                return null;
            }

            @Override
            public Double onReal(@NonNull String tablename, @NonNull String columnName) {
                return null;
            }

            @Override
            public Boolean onBoolean(@NonNull String tablename, @NonNull String columnName) {
                return null;
            }
        }, TestBeanDao.class);
    }
}