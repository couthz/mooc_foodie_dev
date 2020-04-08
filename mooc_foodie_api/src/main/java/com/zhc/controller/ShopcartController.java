package com.zhc.controller;

import com.zhc.pojo.bo.ShopcartBO;
import com.zhc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//@Controller springmvc用的比较多，做页面跳转
@Api(value ="购物车接口controller",tags = {"购物车相关的api"})
@RequestMapping("shopcart")
@RestController
public class ShopcartController {

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")

    @PostMapping("/add")
    public IMOOCJSONResult add(
            @RequestParam String userId,
            @RequestBody ShopcartBO shopcartBO,
            HttpServletRequest request,
            HttpServletResponse response
            ) {
        //按理来说这个跟用户相关的地方，应该有权限判断，暂时先做个简单的
        if(StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        //TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步到redis
        return IMOOCJSONResult.ok();

    }

    @ApiOperation(value = "删除购物车商品", notes = "删除购物车商品", httpMethod = "POST")

    @PostMapping("/del")
    public IMOOCJSONResult del(
            @RequestParam String userId,
            @RequestBody String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        //按理来说这个跟用户相关的地方，应该有权限判断，暂时先做个简单的
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId) ) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }
        //TODO 前端用户在登录的情况下，删除购物车商品，需要同步删除后端购物车商品
        return IMOOCJSONResult.ok();

    }
}
