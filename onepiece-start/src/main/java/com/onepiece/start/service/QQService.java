package com.onepiece.start.service;

import com.alibaba.fastjson.JSONObject;
import com.onepiece.common.dto.QQUserInfoDTO;
import com.onepiece.common.pojo.UserInfo;

/**
 * @描述 对接QQ相关接口
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-31
 */
public interface QQService {

    /**
     * 获取QQ授权登录的URL链接
     *
     * @return
     */
    String getLoginUrl();

    /**
     * 通过授权码等参数，向腾讯开放平台申请access_token令牌
     *
     * @param code 授权码
     * @return
     */
    JSONObject getAccessToken(String code);

    /**
     * 通过授权码等参数，向腾讯开放平台申请access_token令牌
     *
     * @param refreshToken 刷新令牌
     * @return
     */
    JSONObject refreshToken(String refreshToken);

    /**
     * 根据access_token令牌向腾讯开放平台申请获取登录用户的openId
     *
     * @param accessToken 令牌
     * @return
     */
    JSONObject getOpenIdByAccessToken(String accessToken);

    /**
     * 向腾讯开放平台申请获取登录用户基本信息
     *
     * @param accessToken 令牌
     * @param openId      登录用户openId
     * @return
     */
    QQUserInfoDTO getUserInfo(String accessToken, String openId);

    /**
     * QQ登录/注册
     *
     * @param userInfoDTO
     * @return
     */
    UserInfo QQLogin(String openId, QQUserInfoDTO userInfoDTO);
}
