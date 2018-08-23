package com.wx.service;

import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;
import com.wx.dto.OrderDTO;

/**
 * 支付
 * @author: wx
 * @date: 2018/8/17
 */
public interface PayService {

     PayResponse create(OrderDTO orderDTO);

    PayResponse notify(String notifyData);

    RefundResponse refund(OrderDTO orderDTO);
}
