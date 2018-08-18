package com.wx.servuce.impl;

import com.wx.dao.ProductCategoryDao;
import com.wx.model.ProductCategory;
import com.wx.servuce.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wx
 * 2018/8/17
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Override
    public ProductCategory findOne(Integer categoryId) {
        return productCategoryDao.findByCategoryId(categoryId);
    }

    @Override
    public List<ProductCategory> findAll() {

        return productCategoryDao.findAll();
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {

        return productCategoryDao.findByCategoryTypeIn(categoryTypeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {

        return productCategoryDao.save(productCategory);
    }

    @Override
    public ProductCategory update(ProductCategory tmodel) {
        return productCategoryDao.save(tmodel);
    }
}
