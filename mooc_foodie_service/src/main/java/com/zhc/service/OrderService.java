package com.zhc.service;

import com.zhc.pojo.OrderStatus;
import com.zhc.pojo.UserAddress;
import com.zhc.pojo.bo.AddressBO;
import com.zhc.pojo.bo.SubmitOrderBO;
import com.zhc.pojo.vo.MerchantOrdersVO;

import java.util.List;

public interface OrderService {

    /**
     * 用于创建订单
     * @param submitOrderBO
     */
    public MerchantOrdersVO createOrder(SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    public void updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 查询订单状态
     */
    public OrderStatus queryOrderStatusInfo(String orderId);

}
