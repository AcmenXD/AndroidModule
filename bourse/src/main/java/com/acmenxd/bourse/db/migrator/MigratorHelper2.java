package com.acmenxd.bourse.db.migrator;

import com.acmenxd.bourse.db.core.MigrationHelperUtil;
import com.acmenxd.bourse.db.dao.StudentDao;

import org.greenrobot.greendao.database.Database;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/2/28 10:00
 * @detail 更新数据库版本 -> DB版本号 2
 */
public class MigratorHelper2 extends BaseMigratorHelper {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onUpgrade(Database db) {
        //更新数据库表结构
        MigrationHelperUtil.getInstance().migrate(db, new DefaultCallback() {
            @Override
            public String onText(String tablename, String columnName) {
                return null;
            }

            public Long onInteger(String tablename, String columnName) {
                return null;
            }

            @Override
            public Double onReal(String tablename, String columnName) {
                return null;
            }

            @Override
            public Boolean onBoolean(String tablename, String columnName) {
                return null;
            }
        }, StudentDao.class);
    }
}