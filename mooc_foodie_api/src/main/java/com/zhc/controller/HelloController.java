package com.zhc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

//@Controller springmvc用的比较多，做页面跳转
@ApiIgnore
@RestController
public class HelloController {

    @GetMapping("/hello")
    public Object hello() {
        return "Hello world";
    }

}
