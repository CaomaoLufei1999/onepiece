package com.onepiece.start.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.onepiece.common.dto.QQUserInfoDTO;
import com.onepiece.common.utils.URLEncodeUtil;
import com.onepiece.start.service.HttpApiService;
import com.onepiece.start.service.QQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

//    public Map<String, Object> getToken(String code) throws Exception {
//        StringBuilder url = new StringBuilder();
//        url.append("https://graph.qq.com/oauth2.0/token?");
//        url.append("grant_type=authorization_code");
//
//        url.append("&client_id=" + appId);
//        url.append("&client_secret=" + appSecret);
//        url.append("&code=" + code);
//        // 回调地址
//        String redirect_uri = redirectUrl;
//        // 转码
//        url.append("&redirect_uri=" + URLEncodeUtil.getURLEncoderString(redirect_uri));
//        // 获得token
//        String result = httpApiService.doGet(url.toString());
//        System.out.println("url: " + url);
//        // 把token保存
//        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(result, "&");
//        String accessToken = StringUtils.substringAfterLast(items[0], "=");
//        Long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
//        String refreshToken = StringUtils.substringAfterLast(items[2], "=");
//        //token信息
//        Map<String, Object> qqProperties = new HashMap<String, Object>();
//        qqProperties.put("accessToken", accessToken);
//        qqProperties.put("expiresIn", String.valueOf(expiresIn));
//        qqProperties.put("refreshToken", refreshToken);
//        return qqProperties;
//    }
//
//    public String getOpenId(Map<String, Object> qqProperties) throws Exception {
//        // 获取保存的用户的token
//        String accessToken = (String) qqProperties.get("accessToken");
//        if (!StringUtils.isNotEmpty(accessToken)) {
//            // return "未授权";
//        }
//        String result = httpApiService.doGet("https://graph.qq.com/oauth2.0/me?" + "access_token=" + accessToken);
//        return StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
//    }
//
//    public QQUserInfoDTO getUserInfo(Map<String, Object> qqProperties) throws Exception {
//        // 取token
//        String accessToken = (String) qqProperties.get("accessToken");
//        String openId = (String) qqProperties.get("openId");
//        if (!StringUtils.isNotEmpty(accessToken) || !StringUtils.isNotEmpty(openId)) {
//            return null;
//        }
//        //拼接url
//        StringBuilder url = new StringBuilder("https://graph.qq.com/user/get_user_info?");
//        url.append("access_token=" + accessToken);
//        url.append("&oauth_consumer_key=" + appId);
//        url.append("&openid=" + openId);
//        // 获取qq相关数据
//        String result = httpApiService.doGet(url.toString());
//        Object json = JSON.parseObject(result, QQUserInfoDTO.class);
//        QQUserInfoDTO userInfo = (QQUserInfoDTO) json;
//        return userInfo;
//    }
//
//    public Map<String, Object> refreshToken(Map<String, Object> qqProperties) throws Exception {
//        // 获取refreshToken
//        String refreshToken = (String) qqProperties.get("refreshToken");
//        StringBuilder url = new StringBuilder("https://graph.qq.com/oauth2.0/token?");
//        url.append("grant_type=refresh_token");
//        url.append("&client_id=" + appId);
//        url.append("&client_secret=" + appSecret);
//        url.append("&refresh_token=" + refreshToken);
//        System.out.println("url:" + url.toString());
//        String result = httpApiService.doGet(url.toString());
//        // 把新获取的token存到map中
//        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(result, "&");
//        String accessToken = StringUtils.substringAfterLast(items[0], "=");
//        Long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
//        String newRefreshToken = StringUtils.substringAfterLast(items[2], "=");
//        //重置信息
//        qqProperties.put("accessToken", accessToken);
//        qqProperties.put("expiresIn", String.valueOf(expiresIn));
//        qqProperties.put("refreshToken", newRefreshToken);
//        return qqProperties;
//    }
//
//    public void qqLogin(String code, HttpServletRequest request) throws Exception {
//        // 获取tocket
//        Map<String, Object> qqProperties = getToken(code);
//        // 获取openId(每个用户的openId都是唯一不变的)
//        String openId = getOpenId(qqProperties);
//        // tocket过期刷新token
//        Map<String, Object> refreshToken = refreshToken(qqProperties);
//        // 获取数据
//        QQUserInfoDTO userInfo = getUserInfo(qqProperties);
//    }
}
