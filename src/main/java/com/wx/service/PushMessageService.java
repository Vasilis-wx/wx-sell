package com.wx.service;

import com.wx.dto.OrderDTO;

/**
 * @author: wx
 * @date: 2018/8/17
 */
public interface PushMessageService {

    /**
     * 订单状态变更消息
     * @param orderDTO
     */
    void orderStatus(OrderDTO orderDTO);
}
