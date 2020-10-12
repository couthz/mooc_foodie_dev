package com.zhc.controller;

import com.zhc.enums.OrderStatusEnum;
import com.zhc.enums.PayMethod;
import com.zhc.pojo.bo.SubmitOrderBO;
import com.zhc.pojo.vo.MerchantOrdersVO;
import com.zhc.service.OrderService;
import com.zhc.utils.CookieUtils;
import com.zhc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.spec.IvParameterSpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Controller springmvc用的比较多，做页面跳转
@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController{

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value = "用户下单", notes = "用户下单",httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO submitOrderBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        if(submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
                && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            return IMOOCJSONResult.errorMsg("支付方式不支持");
        }
        System.out.println(submitOrderBO);

        // 1. 创建订单
        MerchantOrdersVO merchantOrdersVO = orderService.createOrder(submitOrderBO);
        String orderId = merchantOrdersVO.getMerchantOrderId();

        // 2. 移除购物车中已结算的商品
        //TODO 后端移除，并且同步到前端的cookie
        //目前是全部清空，部分清空后续会完善
        //CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "");

        // 3. 向支付中心发送当前订单，用于保存支付中心的相关数据
        //直接调用另一个接口，不用再返给前端
        merchantOrdersVO.setReturnUrl(payReturnUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId", "imooc");
        headers.add("password", "imooc");


        restTemplate

        return IMOOCJSONResult.ok(orderId);
    }

    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

}
