package com.onepiece.start.controller.wechart;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @描述 对接微信相关接口
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-24
 */
@RestController
@RequestMapping("/wechart")
public class WeChartController {

    private static final Logger logger = LoggerFactory.getLogger(WeChartController.class);

    @Value("${wechart.token}")
    private String token;

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

        List<String> list = new ArrayList();
        list.add(nonce);
        list.add(timestamp);
        list.add(token);

        // 将token、timestamp、nonce三个参数进行字典序排序
        Collections.sort(list);
        logger.info("微信服务器发送身份验证请求参数：{}",list);

        // 将排序后的三个参数字拼接成一个字符串并进行sha1加密
        String sha1Hex = DigestUtils.sha1Hex(list.get(0) + list.get(1) + list.get(2));

        // 加密后的字符串和signature进行对比校验
        if (sha1Hex.equals(signature)) {
            logger.info("===================签名对比校验成功，微信授权接入生效===================");
            return echostr;
        } else {
            logger.error("===================签名对比校验失败，微信授权接入失败===================");
            return null;
        }
    }
}
