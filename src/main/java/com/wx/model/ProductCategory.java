package com.wx.model;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 商品类别
 */
@Entity(name="product_category")
//添加注解后修改时会同步修改设置了同步修改的属性
@DynamicUpdate
//使用data注解自动生成set、get和toString等基本方法,需要引用lombok包和安装插件
@Data
public class ProductCategory {

    /** 类别id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    /** 类别名字. */
    private String categoryName;

    /** 类别编号. */
    private Integer categoryType;

    private Date createTime;

    private Date updateTime;


}
