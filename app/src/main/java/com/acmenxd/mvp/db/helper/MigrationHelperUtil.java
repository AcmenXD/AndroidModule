package com.acmenxd.mvp.db.helper;

import android.database.Cursor;
import android.text.TextUtils;

import com.acmenxd.logger.Logger;
import com.acmenxd.mvp.db.migrator.DefaultCallback;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/2/28 10:00
 * @detail 数据库更新
 */
public class MigrationHelperUtil {
    private final String TAG = this.getClass().getSimpleName();
    /**
     * 数据库中对应Java类型 -> String,不支持char类型
     */
    public static final String TYPE_TEXT = "TEXT";
    /**
     * 数据库中对应Java类型 -> Short,Integer,Long
     */
    public static final String TYPE_INTEGER = "INTEGER";
    /**
     * 数据库中对应Java类型 -> Float,Double
     */
    public static final String TYPE_REAL = "REAL";
    /**
     * 数据库中对应Java类型 -> Boolean 0(false) 1(true)
     */
    public static final String TYPE_BOOLEAN = "BOOLEAN";

    private static MigrationHelperUtil instance;

    private MigrationHelperUtil() {
    }

    public static MigrationHelperUtil getInstance() {
        if (instance == null) {
            instance = new MigrationHelperUtil();
        }
        return instance;
    }

    /**
     * 数据库更新
     *
     * @param db
     * @param pCallback  新增字段在更新数据库时回调, 设置其默认值, 如为null,字段会自动设置默认值
     * @param daoClasses 需要更新或新建表的Dao Class类
     */
    public void migrate(Database db, DefaultCallback pCallback, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        // 检查表是否存在,如果不存在则创建
        for (int i = 0; i < daoClasses.length; i++) {
            try {
                Method method = daoClasses[i].getMethod("createTable", Database.class, boolean.class);
                try {
                    method.invoke(null, db, true);
                } catch (IllegalAccessException pE) {
                    pE.printStackTrace();
                } catch (InvocationTargetException pE) {
                    pE.printStackTrace();
                }
            } catch (NoSuchMethodException pE) {
                pE.printStackTrace();
            }
        }
        //新建临时表
        generateTempTables(db, daoClasses);
        //删除表
        invokeMethod("dropTable", true, db, daoClasses);
        //新建表
        invokeMethod("createTable", false, db, daoClasses);
        //临时表数据写入新表，删除临时表
        restoreData(db, pCallback, daoClasses);
    }

    /**
     * 新建临时表
     *
     * @param db
     * @param daoClasses
     */
    private void generateTempTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String divider = "";
            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");
            ArrayList<String> properties = new ArrayList<>();
            StringBuilder createTableStringBuilder = new StringBuilder();
            createTableStringBuilder.append("CREATE TABLE ").append(tempTableName).append(" (");
            for (int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;
                if (getColumns(db, tableName).contains(columnName)) {
                    properties.add(columnName);
                    String type = getTypeByClass(daoConfig.properties[j].type);
                    createTableStringBuilder.append(divider).append(columnName).append(" ").append(type);
                    if (daoConfig.properties[j].primaryKey) {
                        createTableStringBuilder.append(" PRIMARY KEY");
                    }
                    divider = ",";
                }
            }
            createTableStringBuilder.append(");");
            db.execSQL(createTableStringBuilder.toString());
            StringBuilder insertTableStringBuilder = new StringBuilder();
            insertTableStringBuilder.append("INSERT INTO ").append(tempTableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") SELECT ");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(" FROM ").append(tableName).append(";");
            db.execSQL(insertTableStringBuilder.toString());
        }
    }

    /**
     * 删除表 & 新建表
     */
    private void invokeMethod(String methodName, boolean param, Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        try {
            for (int i = 0; i < daoClasses.length; i++) {
                Method method = daoClasses[i].getDeclaredMethod(methodName, Database.class, boolean.class);
                method.invoke(daoClasses[i], db, param);
            }
        } catch (NoSuchMethodException pE) {
            Logger.e(pE);
        } catch (InvocationTargetException pE) {
            Logger.e(pE);
        } catch (IllegalAccessException pE) {
            Logger.e(pE);
        }
    }

    /**
     * 临时表数据写入新表，删除临时表
     *
     * @param db
     * @param daoClasses
     */
    private void restoreData(Database db, DefaultCallback pCallback, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0, len = daoClasses.length; i < len; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");
            ArrayList<String> properties = new ArrayList();
            ArrayList<String> propertiesQuery = new ArrayList();
            for (int j = 0, size = daoConfig.properties.length; j < size; j++) {
                String columnName = daoConfig.properties[j].columnName;
                if (getColumns(db, tempTableName).contains(columnName)) {
                    properties.add(columnName);
                    propertiesQuery.add(columnName);
                } else {
                    if (TYPE_TEXT.equals(getTypeByClass(daoConfig.properties[j].type))) {
                        String temp = null;
                        if (pCallback != null) {
                            temp = pCallback.onText(tableName, columnName);
                        }
                        if (TextUtils.isEmpty(temp)) {
                            temp = "";
                        }
                        propertiesQuery.add("\"" + temp + "\"" + " as " + columnName);
                        properties.add(columnName);
                    } else if (TYPE_INTEGER.equals(getTypeByClass(daoConfig.properties[j].type))) {
                        Long temp = null;
                        if (pCallback != null) {
                            temp = pCallback.onInteger(tableName, columnName);
                        }
                        if (temp == null) {
                            temp = 0L;
                        }
                        propertiesQuery.add(temp + " as " + columnName);
                        properties.add(columnName);
                    } else if (TYPE_REAL.equals(getTypeByClass(daoConfig.properties[j].type))) {
                        Double temp = null;
                        if (pCallback != null) {
                            temp = pCallback.onReal(tableName, columnName);
                        }
                        if (temp == null) {
                            temp = 0.0D;
                        }
                        propertiesQuery.add(temp + " as " + columnName);
                        properties.add(columnName);
                    } else if (TYPE_BOOLEAN.equals(getTypeByClass(daoConfig.properties[j].type))) {
                        Boolean temp = null;
                        if (pCallback != null) {
                            temp = pCallback.onBoolean(tableName, columnName);
                        }
                        byte bool = 0;
                        if (temp != null && temp == true) {
                            bool = 1;
                        }
                        propertiesQuery.add(bool + " as " + columnName);
                        properties.add(columnName);
                    }
                }
            }
            StringBuilder insertTableStringBuilder = new StringBuilder();
            insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") SELECT ");
            insertTableStringBuilder.append(TextUtils.join(",", propertiesQuery));
            insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";");
            StringBuilder dropTableStringBuilder = new StringBuilder();
            dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);
            db.execSQL(insertTableStringBuilder.toString());
            db.execSQL(dropTableStringBuilder.toString());
        }
    }

    private String getTypeByClass(Class<?> type) {
        // 对应Java的基本数据类型,不支持char类型字段
        if (type.equals(String.class)) {
            return TYPE_TEXT;
        }
        if (type.equals(Long.class) || type.equals(Integer.class) || type.equals(Short.class) || type.equals(long.class) || type.equals(int.class) || type.equals(short.class)) {
            return TYPE_INTEGER;
        }
        if (type.equals(Float.class) || type.equals(Double.class) || type.equals(float.class) || type.equals(double.class)) {
            return TYPE_REAL;
        }
        if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return TYPE_BOOLEAN;
        }
        Exception exception = new Exception("MIGRATION HELPER - CLASS DOESN'T MATCH WITH THE CURRENT PARAMETERS".concat(" - Class: ").concat(type.toString()));
        Logger.e(exception);
        return null;
    }

    private static List<String> getColumns(Database db, String tableName) {
        List<String> columns = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 1", null);
            if (cursor != null) {
                columns = new ArrayList<>(Arrays.asList(cursor.getColumnNames()));
            }
        } catch (Exception pE) {
            Logger.e(pE);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return columns;
    }
}
