package com.wx.service;

/**
 * 卖家端
 * @author: wx
 * @date: 2018/8/17
 */
import com.wx.model.SellerInfo;

public interface SellerService {

    /**
     * 通过openid查询卖家端信息
     * @param openid
     * @return
     */
    SellerInfo findSellerInfoByOpenid(String openid);
}
