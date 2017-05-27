package com.acmenxd.bourse.db.migrator;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/2/28 10:00
 * @detail 新增字段在更新数据库时回调, 设置其默认值, 如为null,字段会自动设置默认值
 */
public interface DefaultCallback {
    /**
     * 更新文本类型的数据库字段
     * 如返回null,表示设置为''
     *
     * @param tablename  表名
     * @param columnName 要设置的字段名
     */
    String onText(String tablename, String columnName);

    /**
     * 更新整形类型的数据库字段
     * 如返回null,表示设置为0
     *
     * @param tablename  表名
     * @param columnName 要设置的字段名
     */
    Long onInteger(String tablename, String columnName);

    /**
     * 更新浮点类型的数据库字段
     * 如返回null,表示设置为0.0
     *
     * @param tablename  表名
     * @param columnName 要设置的字段名
     */
    Double onReal(String tablename, String columnName);

    /**
     * 更新布尔类型的数据库字段
     * 如返回null,表示设置为0(false)
     *
     * @param tablename  表名
     * @param columnName 要设置的字段名
     */
    Boolean onBoolean(String tablename, String columnName);

}
