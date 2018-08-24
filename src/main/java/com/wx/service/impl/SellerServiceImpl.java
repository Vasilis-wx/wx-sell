package com.wx.service.impl;

import com.wx.dao.SellerInfoDao;
import com.wx.model.SellerInfo;
import com.wx.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wx
 * 2018/8/23
 */
@Service
public class SellerServiceImpl implements SellerService{

    @Autowired
    private SellerInfoDao sellerInfoDao;

    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        return sellerInfoDao.findByOpenid(openid);
    }
}
