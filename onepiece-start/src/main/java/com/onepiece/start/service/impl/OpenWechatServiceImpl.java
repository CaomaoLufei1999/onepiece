package com.onepiece.start.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.onepiece.common.dto.WeChatUserInfoDTO;
import com.onepiece.common.enums.AccountBindingTypeEnum;
import com.onepiece.common.pojo.UserInfo;
import com.onepiece.common.utils.URLEncodeUtil;
import com.onepiece.start.mapper.UserInfoMapper;
import com.onepiece.start.service.HttpApiService;
import com.onepiece.start.service.OpenWechatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @描述 对接微信开放平台相关接口实现类
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-08
 */
@Service
public class OpenWechatServiceImpl implements OpenWechatService {

    private static final Logger logger = LoggerFactory.getLogger(OpenWechatServiceImpl.class);

    @Value("${open-wechat.appId}")
    private String appId;

    @Value("${open-wechat.secret}")
    private String secret;

    @Value("${open-wechat.redirectUrl}")
    private String redirectUrl;

    @Autowired
    private HttpApiService httpApiService;

    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 获取微信授权登录的URL
     */
    private static final String GET_WECHAT_LOGIN_URL =
            "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect";

    /**
     * 用于获取access_token的url
     */
    private static final String GET_ACCESS_TOKEN_URL =
            "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /**
     * 向微信开放平台申请刷新access_token令牌有效时间的URL
     */
    private static final String REFRESH_ACCESS_TOKEN_URL =
            "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s&lang=zh_CN";

    /**
     * 向微信开放平台索取登录用户基本信息的URL
     */
    private static final String GET_USER_INFO_URL =
            "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";

    @Override
    public String getLoginUrl() {
        // 回调链接加密
        String encoderRedirectUrl = URLEncodeUtil.getURLEncoderString(redirectUrl);
        // 构建url请求
        return String.format(GET_WECHAT_LOGIN_URL, appId, encoderRedirectUrl, UUID.randomUUID());
    }

    @Override
    public JSONObject getAccessToken(String code) {
        String url = String.format(GET_ACCESS_TOKEN_URL, appId, secret, code);

        String responseStr = httpApiService.doGet(url);
        logger.info("获取access_token访问令牌请求结果：{}", responseStr);

        JSONObject object = JSONObject.parseObject(responseStr);
        // 获取refreshToken
        String refreshToken = (String) object.get("refresh_token");

        // 刷新accessToken有效时间
        return this.refreshToken(refreshToken);
    }

    @Override
    public JSONObject refreshToken(String refreshToken) {
        String url = String.format(REFRESH_ACCESS_TOKEN_URL, appId, refreshToken);

        String responseStr = httpApiService.doGet(url);
        logger.info("刷新access_token访问令牌有效时间后的请求结果：{}", responseStr);

        return JSONObject.parseObject(responseStr);
    }

    @Override
    public JSONObject getUserInfo(String accessToken, String openId) {
        String url = String.format(GET_USER_INFO_URL, accessToken, openId);

        String responseStr = httpApiService.doGet(url);
        logger.info("根据accessToken和用户openId获取到用户的基本信息：{}", responseStr);

        return JSONObject.parseObject(responseStr);
    }

    @Override
    public UserInfo WeChatLogin(WeChatUserInfoDTO weChatUserInfoDTO) {
        UserInfo userInfo = userInfoMapper.getUserInfoByOpenId(weChatUserInfoDTO.getOpenid());
        if (userInfo != null) {
            return userInfo;
        } else {
            userInfo = new UserInfo()
                    .setOpenid(weChatUserInfoDTO.getOpenid())
                    .setUnionid(weChatUserInfoDTO.getUnionid())
                    .setNickname(weChatUserInfoDTO.getNickname())
                    .setSex(1)
                    .setCountry("中国")
                    .setProvince(null)
                    .setCity(null)
                    .setAddress(null)
                    .setAccountBindingType(AccountBindingTypeEnum.WECHAT.getType())
                    .setHeadImg(weChatUserInfoDTO.getHeadImgUrl())
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

            Integer result = userInfoMapper.register(userInfo);
            return userInfo;
        }
    }
}
