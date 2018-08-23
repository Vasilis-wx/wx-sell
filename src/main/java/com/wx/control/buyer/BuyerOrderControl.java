package com.wx.control.buyer;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.wx.VO.ResultVO;
import com.wx.converter.FOrder2OrderDTOConverter;
import com.wx.dto.OrderDTO;
import com.wx.enums.ResultEnum;
import com.wx.exception.SellException;
import com.wx.fmode.FOrder;
import com.wx.service.BuyerService;
import com.wx.service.OrderService;
import com.wx.util.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wx
 * 2018/8/18
 */
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderControl {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    /**
     * 创建订单
     * @param fOrder            @Valid 对forder的参数进行校验   @Valid的参数后必须紧挨着一个BindingResult 参数，否则spring会在校验不通过时直接抛出异常
     * @param bindingResult     验证结果
     * @return
     */
    @PostMapping("/create")
    public ResultVO<Map<String,String>> create(@Valid FOrder fOrder, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            log.error("【创建订单】参数不正确，fOrder={}",fOrder);
            throw new SellException(ResultEnum.PARAM_ERROR,bindingResult.getFieldError().getDefaultMessage());
        }

        OrderDTO orderDTO = FOrder2OrderDTOConverter.convert(fOrder);
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }

        OrderDTO result = orderService.create(orderDTO);
        Map<String,String> map = new HashMap<>();
        map.put("orderId",result.getOrderId());

        return ResultVOUtil.succsee(map);
    }

    /**
     * 订单列表
     * @param openid
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                         @RequestParam(value = "page",defaultValue = "0") Integer page,
                                         @RequestParam(value = "size",defaultValue = "10") Integer size ){
        if(StringUtils.isEmpty(openid)){
            log.error("【查询订单列表】openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        PageRequest pageRequest = PageRequest.of(page,size);
        Page<OrderDTO> orderDTOPage = orderService.findList(openid,pageRequest);
        return ResultVOUtil.succsee(orderDTOPage.getContent());

    }

    /**
     * 获取订单明细
     * @param openid
     * @param orderId
     * @return
     */
    @GetMapping("/detail")
    public ResultVO<OrderDTO> detail(@RequestParam("openid") String openid,
                                     @RequestParam("orderId") String orderId){

        OrderDTO orderDTO = buyerService.findOrderOne(openid,orderId);
        return ResultVOUtil.succsee(orderDTO);
    }

    /**
     * 取消订单
     * @param openid
     * @param orderId
     * @return
     */
    @GetMapping("/cancel")
    public ResultVO cancel(@RequestParam("openid") String openid,
                           @RequestParam("orderId") String orderId) throws Exception{

        buyerService.cancelOrder(openid,orderId);
        return ResultVOUtil.success();
    }
}
