package com.wx.service.impl;

import com.wx.enums.ProductStatusEnum;
import com.wx.model.ProductInfo;
import com.wx.service.ProductInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * Created by wx
 * 2018/8/17
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductInfoServiceImplTest {

    @Autowired
    private ProductInfoService productInfoService;

    @Test
    public void findOne() throws Exception {
    }

    @Test
    public void findUpAll() throws Exception {
    }

    @Test
    public void findAll() throws Exception {
        PageRequest page = PageRequest.of(0,20);
        Page<ProductInfo> list =  productInfoService.findAll(page);
        System.out.println(list.getContent());
    }

    @Test
    public void save() throws Exception {
        ProductInfo tmodel = new ProductInfo();
        tmodel.setProductId("000002");
        tmodel.setCategoryType(2);
        tmodel.setProductDescription("好吃");
        tmodel.setProductName("旺仔小馒头");
        tmodel.setProductPrice(new BigDecimal(20));
        tmodel.setProductStatus(ProductStatusEnum.UP.getCode());
        tmodel.setProductStock(10);
        productInfoService.save(tmodel);
    }

}