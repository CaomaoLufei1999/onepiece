package com.onepiece.start.controller.tencent;

import com.alibaba.fastjson.JSONObject;
import com.onepiece.start.service.OpenWechatService;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @描述 微信开放平台对接的Controller
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-07
 */
@RestController
@RequestMapping("/open-wechat")
public class OpenWechatController {

    @Autowired
    private OpenWechatService openWechatService;

    @RequestMapping("/callback")
    public JSONObject callBack(HttpRequest request){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success","回调成功！");
        return jsonObject;
    }

    /**
     * 获取QQ授权登录URL返回给前端
     *
     * @return
     */
    @GetMapping("/getLoginUrl")
    public String getLoginUrl() {
        return openWechatService.getLoginUrl();
    }
}
