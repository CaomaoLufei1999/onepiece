package com.onepiece.start.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.onepiece.common.dto.QQUserInfoDTO;
import com.onepiece.common.enums.AccountBindingTypeEnum;
import com.onepiece.common.pojo.UserInfo;
import com.onepiece.common.utils.URLEncodeUtil;
import com.onepiece.start.mapper.UserInfoMapper;
import com.onepiece.start.service.HttpApiService;
import com.onepiece.start.service.QQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @描述
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-30
 */
@Service
public class QQServiceImpl implements QQService {

    private static final Logger logger = LoggerFactory.getLogger(QQServiceImpl.class);

    @Value("${qq.appId}")
    private String appId;

    @Value("${qq.appSecret}")
    private String appSecret;

    @Value("${qq.redirectUrl}")
    private String redirectUrl;

    /**
     * 获取QQ授权登录的URL
     */
    private static final String GET_QQ_LOGIN_URL = "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=%s&redirect_uri=%s&state=ok";

    /**
     * 向腾讯开放平台申请access_token令牌的URL
     */
    private static final String GET_ACCESS_TOKEN_URL = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&redirect_uri=%s&fmt=json";

    /**
     * 向腾讯开放平台申请access_token令牌的URL
     */
    private static final String REFRESH_ACCESS_TOKEN_URL = "https://graph.qq.com/oauth2.0/token?grant_type=refresh_token&client_id=%s&client_secret=%s&refresh_token=%s&fmt=json";

    /**
     * 向腾讯开放平台申请获取用户openid的URL
     */
    private static final String GET_OPEN_ID_URL = "https://graph.qq.com/oauth2.0/me?access_token=%s&fmt=json";

    /**
     * 向腾讯开放平台申请获取登录用户基本信息的URL
     */
    private static final String GET_USERINFO_URL = "https://graph.qq.com/user/get_user_info?access_token=%s&oauth_consumer_key=%s&openid=%s";

    @Autowired
    private HttpApiService httpApiService;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public String getLoginUrl() {
        // 回调链接加密
        String encoderRedirectUrl = URLEncodeUtil.getURLEncoderString(redirectUrl);
        // 构建url请求
        return String.format(GET_QQ_LOGIN_URL, appId, encoderRedirectUrl);
    }

    @Override
    public JSONObject getAccessToken(String code) {
        // 回调链接加密
        String encoderRedirectUrl = URLEncodeUtil.getURLEncoderString(redirectUrl);

        // 构建url请求
        String url = String.format(GET_ACCESS_TOKEN_URL, appId, appSecret, code, encoderRedirectUrl);
        // 发送请求
        String responseStr = httpApiService.doGet(url);

        logger.info("请求腾讯开放平台获取到的令牌信息为: {}", responseStr);

        JSONObject object = JSONObject.parseObject(responseStr);
        // 获取refreshToken
        String refreshToken = (String) object.get("refresh_token");

        // 刷新accessToken有效时间
        return this.refreshToken(refreshToken);
    }

    @Override
    public JSONObject refreshToken(String refreshToken) {
        // 构建url请求
        String url = String.format(REFRESH_ACCESS_TOKEN_URL, appId, appSecret, refreshToken);
        // 发送请求
        String responseStr = httpApiService.doGet(url);

        logger.info("请求腾讯开放平台获取到的令牌信息为: {}", responseStr);
        return JSONObject.parseObject(responseStr);
    }

    @Override
    public JSONObject getOpenIdByAccessToken(String accessToken) {
        // 构建url请求
        String url = String.format(GET_OPEN_ID_URL, accessToken);
        // 发送请求
        String responseStr = httpApiService.doGet(url);

        logger.info("请求腾讯开放平台获取登录用户的openId: {}", responseStr);
        return JSONObject.parseObject(responseStr);
    }

    @Override
    public QQUserInfoDTO getUserInfo(String accessToken, String openId) {
        // 构建url请求
        String url = String.format(GET_USERINFO_URL, accessToken, appId, openId);
        // 发送请求
        String responseStr = httpApiService.doGet(url);

        QQUserInfoDTO qqUserInfoDTO = JSONObject.parseObject(responseStr, QQUserInfoDTO.class);
        logger.info("请求腾讯开放平台获取登录用户的基本信息: {}", qqUserInfoDTO);
        return qqUserInfoDTO;
    }

    @Override
    public Integer QQLogin(String openId, QQUserInfoDTO userInfoDTO) {

        UserInfo userInfo = userInfoMapper.getUserInfoByOpenId(openId);
        if (userInfo != null) {
            return userInfo.getUserId();
        } else {
            userInfo = new UserInfo()
                    .setOpenid(openId)
                    .setUnionid(openId)
                    .setNickname(userInfoDTO.getNickname())
                    .setSex(1)
                    .setCountry("中国")
                    .setProvince(null)
                    .setCity(null)
                    .setAddress(null)
                    .setAccountBindingType(AccountBindingTypeEnum.QQ.getType())
                    .setHeadImg(userInfoDTO.getFigureUrlQQ2())
                    .setPhone(null)
                    .setEmail(null)
                    .setUsername(null)
                    .setPassword(null)
                    .setSecurityQuestions(null)
                    .setInterestTags(null)
                    .setFriendLinks(null)
                    .setDescription(null)
                    .setPrivilege(0)
                    .setIdentity(null)
                    .setLevel(0)
                    .setPoints(0)
                    .setCreateTime(new Date());

            Integer result = userInfoMapper.QQRegister(userInfo);
            return userInfo.getUserId();
        }
    }
}
