package com.zhc.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户对象BO", description = "从客户端，由用户传入的数据")
public class UserBo {

    @ApiModelProperty(value = "用户名",example = "zhc")
    private String username;
    @ApiModelProperty(value = "密码",example = "123456")
    private String password;
    @ApiModelProperty(value = "确认密码",example = "123456",required = false)
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
