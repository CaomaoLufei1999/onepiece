package com.onepiece.start.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @描述 项目初始化测试
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-24
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
