package com.wx.service.impl;

import com.wx.dao.ProductInfoDao;
import com.wx.dto.CartDTO;
import com.wx.enums.ProductStatusEnum;
import com.wx.enums.ResultEnum;
import com.wx.exception.SellException;
import com.wx.model.ProductInfo;
import com.wx.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wx
 * 2018/8/17
 */
@Service
public class ProductInfoServiceImpl implements ProductInfoService{

    @Autowired
    private ProductInfoDao productInfoDao;

    @Override
    public ProductInfo findOne(String productId) {
        return productInfoDao.findByProductId(productId);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return productInfoDao.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoDao.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoDao.save(productInfo);
    }

    @Override
    public void increaseStock(List<CartDTO> cartDTOList) {

        for(CartDTO cartDTO : cartDTOList){
            ProductInfo productInfo = productInfoDao.findByProductId(cartDTO.getProductId());
            if(productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer value = productInfo.getProductStock() + cartDTO.getProductQuantity();

            productInfo.setProductStock(value);
            productInfoDao.save(productInfo);
        }
    }

    @Override
    public void decreaseStock(List<CartDTO> cartDTOList) {

        for(CartDTO cartDTO : cartDTOList){
            ProductInfo productInfo = productInfoDao.findByProductId(cartDTO.getProductId());
            if(productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer value = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if(value < 0 ){
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }

            productInfo.setProductStock(value);
            productInfoDao.save(productInfo);
        }
    }


}
