package com.wx.dao;

import com.wx.model.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


/**
 * Created by wx
 * 2018/8/15
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {

//    @Autowired
//    private ProductCategoryDao productCategoryDao;
//
//    @Test
//    @Transactional//方法执行完后回滚，不对数据库造成影响
//    public void add(){
//        ProductCategory tmodel = new ProductCategory();
//        tmodel.setCategoryId(1);
//        tmodel.setCategoryName("面包333");
//        tmodel.setCategoryType(1);
//        ProductCategory ss = productCategoryDao.save(tmodel);
//        Assert.assertNotNull(ss);
//    }
//
//    @Test
//    public void findOne(){
//        ProductCategory tmodel = productCategoryDao.findByCategoryId(1);
////        ProductCategory tmodel =  productCategoryDao.findById(1).get();
//        System.out.println(tmodel.toString());
////        tmodel.setCategoryName("吃饭");
////        productCategoryDao.save(tmodel);
//    }
//
//    @Test
//    public void findByCategoryTypeIn(){
//        List<Integer> list = Arrays.asList(1,2,3,4);
//        List<ProductCategory> list2 = productCategoryDao.findByCategoryTypeIn(list);
//        Assert.assertNotEquals(0,list2.size());
//    }
}