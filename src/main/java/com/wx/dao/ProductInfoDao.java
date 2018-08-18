package com.wx.dao;


import com.wx.model.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: wx 
 * @date: 2018/8/17
 */
public interface ProductInfoDao extends JpaRepository<ProductInfo, String> {

    List<ProductInfo> findByProductStatus(Integer productStatus);

    ProductInfo findByProductId(String productId);
}
