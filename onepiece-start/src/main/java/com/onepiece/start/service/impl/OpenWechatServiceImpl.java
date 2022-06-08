package com.onepiece.start.service.impl;

import com.onepiece.common.utils.URLEncodeUtil;
import com.onepiece.start.service.OpenWechatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    /**
     * 获取微信授权登录的URL
     */
    private static final String GET_WECHAT_LOGIN_URL =
            "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect";

    @Override
    public String getLoginUrl() {
        // 回调链接加密
        String encoderRedirectUrl = URLEncodeUtil.getURLEncoderString(redirectUrl);
        // 构建url请求
        return String.format(GET_WECHAT_LOGIN_URL, appId, encoderRedirectUrl,UUID.randomUUID());
    }
}
