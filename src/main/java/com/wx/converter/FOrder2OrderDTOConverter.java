package com.wx.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wx.dto.OrderDTO;
import com.wx.enums.ResultEnum;
import com.wx.exception.SellException;
import com.wx.fmode.FOrder;
import com.wx.model.OrderDetail;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * fmodel 转 DTO
 * @author: wx
 * @date: 2018/8/18
 */
@Slf4j
public class FOrder2OrderDTOConverter {

    public static OrderDTO convert(FOrder forder) {
        Gson gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setBuyerName(forder.getName());
        orderDTO.setBuyerPhone(forder.getPhone());
        orderDTO.setBuyerAddress(forder.getAddress());
        orderDTO.setBuyerOpenid(forder.getOpenid());

        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            orderDetailList = gson.fromJson(forder.getItems(),new TypeToken<List<OrderDetail>>() {}.getType());
        } catch (Exception e) {
            log.error("【对象转换】错误, string={}", forder.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }
}
