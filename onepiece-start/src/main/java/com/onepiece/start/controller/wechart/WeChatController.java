package com.onepiece.start.controller.wechart;

import com.alibaba.fastjson.JSONObject;
import com.onepiece.start.service.HttpApiService;
import com.onepiece.start.service.WeChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @描述 对接微信相关Controller
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-24
 */
@RestController
@RequestMapping("/wechat")
public class WeChatController {

    private static final Logger logger = LoggerFactory.getLogger(WeChatController.class);

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private HttpApiService httpApiService;

    /**
     * 验证消息的确来自微信服务器
     *
     * @param signature 微信加密签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     */
    @GetMapping("/verify")
    public String verify(String signature, String timestamp, String nonce, String echostr) {

        // sha1加密后得到的字符串
        String verify = weChatService.verify(signature, timestamp, nonce, echostr);

        // 加密后的字符串和signature进行对比校验
        if (verify.equals(signature)) {
            logger.info("===================签名对比校验成功，微信授权接入生效===================");
            return echostr;
        } else {
            logger.error("===================签名对比校验失败，微信授权接入失败===================");
            return null;
        }
    }

    /**
     * 向微信服务器索要临时二维码ticket凭据
     *
     * @return
     */
    @GetMapping("/getQrcodeTicket")
    public JSONObject getQrcodeTicket(){
        JSONObject response = weChatService.getAccessToken();
        String accessToken = response.get("access_token").toString();

        logger.info("获取到的 access_token 值为：{}", accessToken);

        return weChatService.getQrcodeTicket(accessToken);
    }
}
