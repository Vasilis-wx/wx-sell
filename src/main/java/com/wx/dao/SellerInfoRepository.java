package com.wx.dao;

import com.wx.model.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: wx
 * @date: 2018/8/17
 */
public interface SellerInfoRepository extends JpaRepository<SellerInfo, String> {
    SellerInfo findByOpenid(String openid);
}
