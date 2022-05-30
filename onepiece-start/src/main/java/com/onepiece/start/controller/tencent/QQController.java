package com.onepiece.start.controller.tencent;

import com.onepiece.common.vo.QQUrlVO;
import com.onepiece.start.service.impl.QQServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @描述 对接QQ相关的Controller接口
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-30
 */
@RestController
@RequestMapping("/qq")
public class QQController {

    @Autowired
    private QQServiceImpl qqService;

    @GetMapping("/auth/callback")
    public String fallback(HttpServletRequest request, HttpServletResponse response){
        return "QQ登录回调信息";
    }

    @GetMapping("/getQQUrl")
    public QQUrlVO getQQUrl() {
        return qqService.getQQUrl();
    }
}
