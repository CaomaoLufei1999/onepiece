package com.onepiece.start.service;

/**
 * @描述 对接微信开放平台相关接口
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-08
 */
public interface OpenWechatService {

    /**
     * 获取微信授权登录的URL链接
     *
     * @return
     */
    String getLoginUrl();
}
