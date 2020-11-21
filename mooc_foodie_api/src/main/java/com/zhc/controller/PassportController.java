package com.zhc.controller;

import com.zhc.pojo.Users;
import com.zhc.pojo.bo.ShopcartBO;
import com.zhc.pojo.bo.UserBo;
import com.zhc.service.StuService;
import com.zhc.service.UserService;
import com.zhc.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value ="用户api",tags = {"用户认证接口"})
@RestController
@RequestMapping("/passport")
public class PassportController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    //@RequestParam 表示username是请求参数，而不是路径参数（@PathVariable）
    @ApiOperation(value = "用户名是否存在", notes = "检查用户名是否存在")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username) {

        //1.isBlank可以判断空或字符串
        if (StringUtils.isBlank(username)) {
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }

        //2.查找注册的用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }

        return IMOOCJSONResult.ok();

    }

    //@RequestBody获取整个请求体，而不写会解析json
    //在这个项目里校验基本都是在api层做的，因此api层代码的逻辑都应该是校验+调用service
    @ApiOperation(value = "用户注册", notes = "用于用户注册")
    @PostMapping("/regist")
    public IMOOCJSONResult regist(@RequestBody UserBo userBo,
                                  HttpServletRequest request,
                                  HttpServletResponse response){

        String username = userBo.getUsername();
        String password = userBo.getPassword();
        String confirmPwd = userBo.getConfirmPassword();

        //0.判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
        StringUtils.isBlank(password)||
        StringUtils.isBlank(confirmPwd)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }

        //1.查询用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }
        //2.密码长度不能少于6位
        if (password.length() < 6 ) {
            return IMOOCJSONResult.errorMsg("密码长度不能少于6");
        }

        //3.判断两次密码是否一致
        if (!password.equals(confirmPwd)) {
            return IMOOCJSONResult.errorMsg("两次密码输入不一致");
        }

        //4.实现注册
        Users userResult = userService.createUser(userBo);
        userResult = setNullProperty(userResult);
        //将用户信息用utf-8编码，添加到cookie
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult),true);
        //TODO 生成用户token，存入redis会话
        //TODO 同步购物车数据
        syncShopcartData(userResult.getId(), request, response);
        return IMOOCJSONResult.ok();

    }

    @ApiOperation(value = "用户登录", notes = "用于用户登录")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UserBo userBo,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String username = userBo.getUsername();
        String password = userBo.getPassword();
        //0.判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        //1.实现登录
        Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if(userResult ==  null){
            return IMOOCJSONResult.errorMsg("用户名或密码不正确");
        }

        //2.清空敏感信息，返回
        userResult = setNullProperty(userResult);
        //将用户信息用utf-8编码，添加到cookie
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult),true);
        //TODO 生成用户token，存入redis会话
        //TODO 同步购物车数据
        syncShopcartData(userResult.getId(), request, response);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户退出", notes = "用于用户退出")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId,HttpServletRequest request,
                                  HttpServletResponse response) {

        //清除用户相关信息的cookie,主页就不会显示用户信息了
        //本项目中没有做权限控制，访问主页不会跳回登录页
        CookieUtils.deleteCookie(request,response,"user");

        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART);
        //TODO 分布式会话中需要清除用户数据
        return IMOOCJSONResult.ok();

    }

    /**
     * 注册登录成功后，同步cookie和redis中的购物车数据
     * @return
     */
    private void syncShopcartData(String userId, HttpServletRequest request, HttpServletResponse response) {
        String shopcartJsonRedis = redisOperator.get(FOODIE_SHOPCART + ":" + userId);

        String shopcartStrCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);

        //redis为空
        if (StringUtils.isBlank(shopcartJsonRedis)) {
            if (StringUtils.isNotBlank(shopcartStrCookie)) {
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, shopcartStrCookie);
            }
        } else {
            //redis不为空，cookie不为空,其实就是合并两个购物车，相同商品的数量以cookie购物车的为准
            if (StringUtils.isNotBlank(shopcartStrCookie)) {
                List<ShopcartBO> shopcartListRedis = JsonUtils.jsonToList(shopcartJsonRedis, ShopcartBO.class);
                List<ShopcartBO> shopcartListCookie = JsonUtils.jsonToList(shopcartStrCookie, ShopcartBO.class);

                List<ShopcartBO> pendingDeleteList = new ArrayList<>();

                //1.找到两购物车中相同的商品，cookie购买数量覆盖redis购买数量(这个是当当现在的逻辑)
                for (ShopcartBO redisShopcart : shopcartListRedis) {
                    String redisSpecId = redisShopcart.getSpecId();

                    for (ShopcartBO cookieShopcart : shopcartListCookie) {
                        String cookieSpecId = cookieShopcart.getSpecId();

                        if (redisSpecId.equals(cookieSpecId)) {
                            // 覆盖购买数量，不累加，参考京东
                            redisShopcart.setBuyCounts(cookieShopcart.getBuyCounts());
                            // 把cookieShopcart放入待删除列表，用于最后的删除与合并
                            pendingDeleteList.add(cookieShopcart);
                        }

                    }
                }

                // 2.从现有cookie中删除对应的覆盖过的商品数据（一次性删除，比一个一个删除，效率高一些，不用多次移数据）
                shopcartListCookie.removeAll(pendingDeleteList);

                // 3.合并两个list
                shopcartListRedis.addAll(shopcartListCookie);
                // 更新到redis和cookie
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartListRedis), true);
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartListRedis));
            } else {
                //redis不为空，cookie为空
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, shopcartJsonRedis, true);
            }
        }

    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }



}
