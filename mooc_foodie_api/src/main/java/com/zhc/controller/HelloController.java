package com.zhc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

//@Controller springmvc用的比较多，做页面跳转
@ApiIgnore
@RestController
public class HelloController {

    final static Logger logger= LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public Object hello() {
        logger.info("info: hello~");
        logger.debug("debug: hello~");
        logger.warn("warn: hello~");
        logger.error("error: hello~");

        return "Hello world";
    }

}
