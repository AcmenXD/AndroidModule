package com.acmenxd.mvpbase.model.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Date;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/2/28 10:00
 * @detail 数据库表测试类
 */
@Entity //将我们的java普通类变为一个能够被greenDAO识别的数据库类型的实体类
public class TestBean {
    /**
     * 注释解释:
     *
     * @NotNull : 表示该字段不可以为空
     * @Unique : 表示该字段唯一
     * @Index(unique = true) : 使普通属性改变成唯一索引属性
     */
    @Id//通过这个注解标记的字段必须是Long类型的，这个字段在数据库中表示它就是主键，并且它默认就是自增的
    private Long id;

    @Property(nameInDb = "NAME")
    private String name;
    private int age;
    private double score;
    private Date date;
    @Transient //表明这个字段不会被写入数据库，只是作为一个普通的java类字段，用来临时存储数据的，不会被持久化
    private Date date2;

    @Generated(hash = 839484647)
    public TestBean(Long id, String name, int age, double score, Date date) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.score = score;
        this.date = date;
    }

    @Generated(hash = 2087637710)
    public TestBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getScore() {
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
