package com.wx.service;

import com.wx.model.ProductCategory;

import java.util.List;

/**
 * 商品类别
 * Created by wx
 * 2018/8/17
 */
public interface ProductCategoryService {

    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);

    ProductCategory update(ProductCategory tmodel);
}
