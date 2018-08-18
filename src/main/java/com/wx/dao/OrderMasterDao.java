package com.wx.dao;


import com.wx.model.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: wx 
 * @date: 2018/8/17
 */
public interface OrderMasterDao extends JpaRepository<OrderMaster, String> {

    Page<OrderMaster> findByBuyerOpenid(String buyerOpenid, Pageable pageable);

    OrderMaster findByOrderId(String orderId);
}
