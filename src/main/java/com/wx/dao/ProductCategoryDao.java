package com.wx.dao;

import com.wx.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by wx
 * 2018/8/15
 */
public interface ProductCategoryDao extends JpaRepository<ProductCategory,Integer>{

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory findByCategoryId(int id);

}
