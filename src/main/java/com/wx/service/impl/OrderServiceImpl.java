package com.wx.service.impl;

import com.wx.converter.OrderMaster2OrderDtoConverter;
import com.wx.dao.OrderDetailDao;
import com.wx.dao.OrderMasterDao;
import com.wx.dto.CartDTO;
import com.wx.dto.OrderDTO;
import com.wx.enums.OrderStatusEnum;
import com.wx.enums.PayStatusEnum;
import com.wx.enums.ResultEnum;
import com.wx.exception.SellException;
import com.wx.model.OrderDetail;
import com.wx.model.OrderMaster;
import com.wx.model.ProductInfo;
import com.wx.service.OrderService;
import com.wx.service.PayService;
import com.wx.service.ProductInfoService;
import com.wx.service.PushMessageService;
import com.wx.util.KeyUtil;
import com.wx.websocket.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wx
 * 2018/8/18
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private OrderMasterDao orderMasterDao;

    @Autowired
    private PayService payService;

    @Autowired
    private PushMessageService pushMessageService;

    @Autowired
    private WebSocket webSocket;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        String orderId = KeyUtil.getUniqueKey();
        BigDecimal orderMoney = new BigDecimal(0);

        /***** 1、查询商品价格数量  *****/
        for(OrderDetail orderDetail : orderDTO.getOrderDetailList()){
            ProductInfo product = productInfoService.findOne(orderDetail.getProductId());
            if(product == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            /***** 2、计算总价  *****/
            orderMoney = product.getProductPrice().multiply(BigDecimal.valueOf(orderDetail.getProductQuantity())).add(orderMoney);

            /***** 3、保存订单明细  *****/
            //保存订单明细
            BeanUtils.copyProperties(product,orderDetail);
            orderDetail.setDetailId(KeyUtil.getUniqueKey());
            orderDetail.setOrderId(orderId);
            orderDetailDao.save(orderDetail);
        }

        /***** 4、保存订单  *****/
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderMoney);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterDao.save(orderMaster);


        /***** 4、更新库存  *****/
        List<CartDTO> list = orderDTO.getOrderDetailList().stream().map(
                e ->  new CartDTO(e.getProductId(),e.getProductQuantity())
        ).collect(Collectors.toList());
        productInfoService.decreaseStock(list);

        orderDTO.setOrderId(orderId);

        /***** 5、发送websocket消息  *****/
        webSocket.sendMessage("您有新的订单！");

        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {

        OrderMaster orderMaster = orderMasterDao.findByOrderId(orderId);
        if(orderMaster == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        List<OrderDetail> detailList = orderDetailDao.findByOrderId(orderId);
        if(CollectionUtils.isEmpty(detailList)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(detailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        //获取订单
        Page<OrderMaster> orderMasterPage = orderMasterDao.findByBuyerOpenid(buyerOpenid,pageable);
        //转化从DTO
        List<OrderDTO> orderDTOList = OrderMaster2OrderDtoConverter.convert(orderMasterPage.getContent());
        //放到page
        Page<OrderDTO> orderDTOPage = new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasterPage.getTotalElements());
        return orderDTOPage;
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO){
        /********* 1、判断订单状态  *********/
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【取消订单】失败，订单状态不对，只能取消新订单。订单id："+orderDTO.getOrderId());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        /********  2、修改订单状态为已取消  ***********/
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());

        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster result = orderMasterDao.save(orderMaster);
        if(result == null){
            log.error("【取消订单】失败，orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        /********  3、返回库存  ***********/
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【取消订单】中订单中无商品详情，orderIdDTO = {}",orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(),e.getProductQuantity()))
                .collect(Collectors.toList());
        productInfoService.increaseStock(cartDTOList);

        try {
            /********  4、如果已支付，需要退款  ***********/
            if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
                payService.refund(orderDTO);
            }
        } catch (Exception e) {
            log.error("【取消订单】退款失败，orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    public OrderDTO finish(OrderDTO orderDTO) {
        /********* 1、判断订单状态  *********/
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【完结订单】失败，订单状态不对，只能完结新订单。订单id："+orderDTO.getOrderId());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        /********  2、修改订单状态为已完结  ***********/
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());

        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster result = orderMasterDao.save(orderMaster);
        if(result == null){
            log.error("【完结订单】失败，orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        pushMessageService.orderStatus(orderDTO);

        return orderDTO;
    }

    @Override
    public OrderDTO paid(OrderDTO orderDTO) {
        /********* 1、判断订单状态  *********/
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【订单支付】失败，订单状态不对，只能支付新订单。订单id："+orderDTO.getOrderId());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        /********* 2、判断支付状态  *********/
        if(!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
            log.error("【订单支付】失败，支付状态不对，只能支付未支付的订单。订单id："+orderDTO.getOrderId());
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        /********  3、修改订单支付状态为已支付  ***********/
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());

        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster result = orderMasterDao.save(orderMaster);
        if(result == null){
            log.error("【订单支付】失败，orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        //获取订单
        Page<OrderMaster> orderMasterPage = orderMasterDao.findAll(pageable);
        //转化从DTO
        List<OrderDTO> orderDTOList = OrderMaster2OrderDtoConverter.convert(orderMasterPage.getContent());
        //放到page
        Page<OrderDTO> orderDTOPage = new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasterPage.getTotalElements());
        return orderDTOPage;
    }
}
