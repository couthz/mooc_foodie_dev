package com.zhc.controller;

import com.zhc.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller springmvc用的比较多，做页面跳转
@RestController
public class StuController {

    @Autowired
    private StuService stuService;

    //id作为路径参数映射
    //id作为请求参数映射
    @GetMapping("/getStu")
    public Object getStu(int id) {
        return stuService.getStuInfo(id);
    }

    @PostMapping("/saveStu")
    public Object saveStu() {
        stuService.saveStu();
        return "ok";
    }

    @PostMapping("/getStu")
    public Object updateStu(int id) {
        stuService.updateStu(id);
        return "ok";
    }

    @PostMapping("/deleteStu")
    public Object deleteStu(int id) {
        stuService.deleteStu(id);
        return "ok";
    }



}
