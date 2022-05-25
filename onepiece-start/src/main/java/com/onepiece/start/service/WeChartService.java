package com.onepiece.start.service;

/**
 * @描述 对接微信相关接口
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-24
 */
public interface WeChartService {
    /**
     * 验证消息的确来自微信服务器
     *
     * @param signature 微信加密签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     */
    String verify(String signature, String timestamp, String nonce, String echostr);
}
