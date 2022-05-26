package com.onepiece.start.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @描述 对接微信相关接口
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-24
 */
public interface WeChatService {
    /**
     * 验证消息的确来自微信服务器
     *
     * @param signature 微信加密签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     */
    String verify(String signature, String timestamp, String nonce, String echostr);

    /**
     * 向微信服务器索要AccessToken访问了令牌
     *
     * @return
     */
    JSONObject getAccessToken();


    /**
     * 通过访问令牌，向微信服务器索要临时二维码ticket凭据
     *
     * @param accessToken 访问令牌
     * @return
     */
    JSONObject getQrcodeTicket(String accessToken);
}
