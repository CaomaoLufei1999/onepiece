package com.onepiece.start.controller.wechart;

import com.alibaba.fastjson.JSONObject;
import com.onepiece.start.service.HttpApiService;
import com.onepiece.start.service.WeChartService;
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
@RequestMapping("/wechart")
public class WeChartController {

    private static final Logger logger = LoggerFactory.getLogger(WeChartController.class);

    @Autowired
    private WeChartService weChartService;

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
        String verify = weChartService.verify(signature, timestamp, nonce, echostr);

        // 加密后的字符串和signature进行对比校验
        if (verify.equals(signature)) {
            logger.info("===================签名对比校验成功，微信授权接入生效===================");
            return echostr;
        } else {
            logger.error("===================签名对比校验失败，微信授权接入失败===================");
            return null;
        }
    }

    @RequestMapping("/httpclient")
    public JSONObject test() throws Exception {
        JSONObject response = weChartService.getAccessToken("client_credential", "wx92d68f8c12fddbbb", "eaaa2c0b1940ec3851d24b60189240f4");
        return response;
    }
}
