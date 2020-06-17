package com.zhc.service;

import com.zhc.pojo.UserAddress;
import com.zhc.pojo.bo.AddressBO;
import com.zhc.pojo.bo.SubmitOrderBO;

import java.util.List;

public interface OrderService {

    /**
     * 用于创建订单
     * @param submitOrderBO
     */
    public void createOrder(SubmitOrderBO submitOrderBO);

}
