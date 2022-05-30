package com.onepiece.start.service.impl;

import com.alibaba.fastjson.JSON;
import com.onepiece.common.dto.QQUserInfoDTO;
import com.onepiece.common.utils.URLEncodeUtil;
import com.onepiece.common.vo.QQUrlVO;
import com.onepiece.start.service.HttpApiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @描述
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-30
 */
@Service
public class QQServiceImpl {
    @Value("${qq.appId}")
    private String appId;

    @Value("${qq.appId}")
    private String appSecret;

    @Value("${qq.redirectUrl}")
    private String redirectUrl;

    @Autowired
    private HttpApiService httpApiService;

    public QQUrlVO getQQUrl() {
        // 拼接url
        StringBuilder url = new StringBuilder();
        url.append("https://graph.qq.com/oauth2.0/authorize?");
        url.append("response_type=code");
        url.append("&client_id=" + appId);
        // 回调地址 ,回调地址要进行Encode转码
        String redirect_uri = redirectUrl;
        // 转码
        url.append("&redirect_uri=" + URLEncodeUtil.getURLEncoderString(redirect_uri));
        url.append("&state=ok");
        // HttpClientUtils.get(url.toString(), "UTF-8");

        QQUrlVO qqUrl = new QQUrlVO();
        qqUrl.setUrl(url.toString());
        return qqUrl;
    }

    public Map<String, Object> getToken(String code) throws Exception {
        StringBuilder url = new StringBuilder();
        url.append("https://graph.qq.com/oauth2.0/token?");
        url.append("grant_type=authorization_code");

        url.append("&client_id=" + appId);
        url.append("&client_secret=" + appSecret);
        url.append("&code=" + code);
        // 回调地址
        String redirect_uri = redirectUrl;
        // 转码
        url.append("&redirect_uri=" + URLEncodeUtil.getURLEncoderString(redirect_uri));
        // 获得token
        String result = httpApiService.doGet(url.toString());
        System.out.println("url: " + url);
        // 把token保存
        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(result, "&");
        String accessToken = StringUtils.substringAfterLast(items[0], "=");
        Long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
        String refreshToken = StringUtils.substringAfterLast(items[2], "=");
        //token信息
        Map<String, Object> qqProperties = new HashMap<String, Object>();
        qqProperties.put("accessToken", accessToken);
        qqProperties.put("expiresIn", String.valueOf(expiresIn));
        qqProperties.put("refreshToken", refreshToken);
        return qqProperties;
    }

    public String getOpenId(Map<String, Object> qqProperties) throws Exception {
        // 获取保存的用户的token
        String accessToken = (String) qqProperties.get("accessToken");
        if (!StringUtils.isNotEmpty(accessToken)) {
            // return "未授权";
        }
        String result = httpApiService.doGet("https://graph.qq.com/oauth2.0/me?" + "access_token=" + accessToken);
        return StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
    }

    public QQUserInfoDTO getUserInfo(Map<String, Object> qqProperties) throws Exception {
        // 取token
        String accessToken = (String) qqProperties.get("accessToken");
        String openId = (String) qqProperties.get("openId");
        if (!StringUtils.isNotEmpty(accessToken) || !StringUtils.isNotEmpty(openId)) {
            return null;
        }
        //拼接url
        StringBuilder url = new StringBuilder("https://graph.qq.com/user/get_user_info?");
        url.append("access_token=" + accessToken);
        url.append("&oauth_consumer_key=" + appId);
        url.append("&openid=" + openId);
        // 获取qq相关数据
        String result = httpApiService.doGet(url.toString());
        Object json = JSON.parseObject(result, QQUserInfoDTO.class);
        QQUserInfoDTO userInfo = (QQUserInfoDTO) json;
        return userInfo;
    }

    public Map<String, Object> refreshToken(Map<String, Object> qqProperties) throws Exception {
        // 获取refreshToken
        String refreshToken = (String) qqProperties.get("refreshToken");
        StringBuilder url = new StringBuilder("https://graph.qq.com/oauth2.0/token?");
        url.append("grant_type=refresh_token");
        url.append("&client_id=" + appId);
        url.append("&client_secret=" + appSecret);
        url.append("&refresh_token=" + refreshToken);
        System.out.println("url:" + url.toString());
        String result = httpApiService.doGet(url.toString());
        // 把新获取的token存到map中
        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(result, "&");
        String accessToken = StringUtils.substringAfterLast(items[0], "=");
        Long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
        String newRefreshToken = StringUtils.substringAfterLast(items[2], "=");
        //重置信息
        qqProperties.put("accessToken", accessToken);
        qqProperties.put("expiresIn", String.valueOf(expiresIn));
        qqProperties.put("refreshToken", newRefreshToken);
        return qqProperties;
    }

    public void qqLogin(String code, HttpServletRequest request) throws Exception {
        // 获取tocket
        Map<String, Object> qqProperties = getToken(code);
        // 获取openId(每个用户的openId都是唯一不变的)
        String openId = getOpenId(qqProperties);
        // tocket过期刷新token
        Map<String, Object> refreshToken = refreshToken(qqProperties);
        // 获取数据
        QQUserInfoDTO userInfo = getUserInfo(qqProperties);
    }
}
