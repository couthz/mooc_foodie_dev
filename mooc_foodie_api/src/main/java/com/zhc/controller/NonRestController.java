package com.zhc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//@Controller springmvc用的比较多，做页面跳转
@Controller
@RequestMapping("/Non")
public class NonRestController {

    @GetMapping("/hello")
    @ResponseBody
    public Object hello() {
        return "Helloworld";
    }

}
