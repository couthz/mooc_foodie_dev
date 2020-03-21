package com.zhc.service;

import com.zhc.pojo.Stu;
import com.zhc.pojo.Users;
import com.zhc.pojo.bo.UserBo;

public interface UserService {

    /**
     * 判断用户名是否存在、
     */
    public boolean queryUsernameIsExist(String username);

    /*
    这里把Users返回有什么用？
     */
    //可以这样理解，前端传入后端用于接收的数据体，都可以定义为xxxBO
    public Users createUser(UserBo userBO);

    /**
     * 用于登录
     * @param username
     * @param password
     * @return
     */
    public Users queryUserForLogin(String username, String password);
}
