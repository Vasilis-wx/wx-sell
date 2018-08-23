package com.wx.service;

import com.wx.model.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wx
 * 2018/8/17
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductCategoryServiceTest {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Test
    public void findOne() throws Exception {
        ProductCategory tmodel = productCategoryService.findOne(1);
        tmodel.setCategoryName("零食");
        tmodel.getCategoryName();
        productCategoryService.update(tmodel);
    }

    @Test
    public void findAll() throws Exception {
        List<ProductCategory> list =  productCategoryService.findAll();
        Assert.assertNotEquals(0,list.size());
        System.out.println(list);
    }

    @Test
    public void findByCategoryTypeIn() throws Exception {
        List<ProductCategory> list = productCategoryService.findByCategoryTypeIn(Arrays.asList(1,2,3,4));
        System.out.println(list);
    }

    @Test
    public void save() throws Exception {
        ProductCategory tmodel = new ProductCategory();
        tmodel.setCategoryName("水果");
        tmodel.setCategoryType(3);
        productCategoryService.save(tmodel);
    }



}