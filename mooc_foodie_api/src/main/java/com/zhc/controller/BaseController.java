package com.zhc.controller;

import com.zhc.pojo.Orders;
import com.zhc.utils.IMOOCJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;

@Controller
public class BaseController {

    public static final String FOODIE_SHOPCART = "shopcart";

    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    //微信支付中心->支付中心->天天吃货平台
    //                    |->回调通知的url
    //这样写本地地址回调的时候肯定访问不到，需要用natapp做个内网穿透
    String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";

    //支付中心的调用地址
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";
}
