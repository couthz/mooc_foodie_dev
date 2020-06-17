package com.zhc.service.impl;

import com.zhc.enums.YesOrNo;
import com.zhc.mapper.ItemsImgMapper;
import com.zhc.mapper.OrdersMapper;
import com.zhc.mapper.UserAddressMapper;
import com.zhc.pojo.Orders;
import com.zhc.pojo.UserAddress;
import com.zhc.pojo.bo.AddressBO;
import com.zhc.pojo.bo.SubmitOrderBO;
import com.zhc.service.AddressService;
import com.zhc.service.OrderService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void createOrder(SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        //包邮费用设置为0
        Integer postAmount = 0;

        String orderId = sid.nextShort();
        // 1. 新订单数据保存
        Orders newOrder = new Orders();


        // 2.循环根据itemSpecIds保存订单商品信息

    }
}
