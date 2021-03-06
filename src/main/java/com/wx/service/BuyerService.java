package com.wx.service;


import com.wx.dto.OrderDTO;

/**
 * @author: wx 
 * @date: 2018/8/17
 */
public interface BuyerService {

    //查询一个订单
    OrderDTO findOrderOne(String openid, String orderId);

    //取消订单
    OrderDTO cancelOrder(String openid, String orderId) throws Exception;
}
