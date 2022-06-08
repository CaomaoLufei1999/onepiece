package com.onepiece.start.service;

import com.alibaba.fastjson.JSONObject;
import com.onepiece.common.dto.WeChatUserInfoDTO;
import com.onepiece.common.pojo.UserInfo;

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

    /**
     * 向微信服务器索要accessToken访问了令牌
     *
     * @param code
     * @return
     */
    JSONObject getAccessToken(String code);

    /**
     * 刷新令牌有效时间
     *
     * @param refreshToken
     * @return
     */
    JSONObject refreshToken(String refreshToken);

    /**
     * 获取微信登录用户的基本信息
     *
     * @param accessToken
     * @param openId
     * @return
     */
    JSONObject getUserInfo(String accessToken, String openId);

    /**
     * 微信登录/注册
     *
     * @param weChatUserInfoDTO
     * @return
     */
    UserInfo WeChatLogin(WeChatUserInfoDTO weChatUserInfoDTO);
}
