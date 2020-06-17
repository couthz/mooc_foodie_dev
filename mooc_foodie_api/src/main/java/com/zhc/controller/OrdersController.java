package com.zhc.controller;

import com.zhc.enums.PayMethod;
import com.zhc.pojo.UserAddress;
import com.zhc.pojo.bo.AddressBO;
import com.zhc.pojo.bo.SubmitOrderBO;
import com.zhc.service.AddressService;
import com.zhc.utils.IMOOCJSONResult;
import com.zhc.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Controller springmvc用的比较多，做页面跳转
@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RestController
@RequestMapping("orders")
public class OrdersController {

    @ApiOperation(value = "用户下单", notes = "用户下单",httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO submitOrderBO) {

        if(submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
                && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            return IMOOCJSONResult.errorMsg("支付方式不支持");
        }
        System.out.println(submitOrderBO);

        // 1. 创建订单
        // 2. 移除购物车中已结算的商品
        // 3.  向支付中心发送当前订单，用于保存支付中心的相关数据
        return IMOOCJSONResult.ok();
    }

}
