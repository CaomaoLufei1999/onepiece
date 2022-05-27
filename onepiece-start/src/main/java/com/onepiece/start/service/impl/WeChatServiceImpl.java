package com.onepiece.start.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.onepiece.start.service.HttpApiService;
import com.onepiece.start.service.WeChatService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @描述 对接微信相关接口实现类
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-24
 */
@Service
public class WeChatServiceImpl implements WeChatService {

    private static final Logger logger = LoggerFactory.getLogger(WeChatServiceImpl.class);

    @Value("${wechat.token}")
    private String token;

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.secret}")
    private String secret;

    @Value("${wechat.grantType}")
    private String grantType;

    @Value("${wechat.expireSeconds}")
    private String expireSeconds;

    @Value("${wechat.actionName}")
    private String actionName;

    @Value("${wechat.sceneId}")
    private String sceneId;

    @Value("${wechat.sceneStr}")
    private String sceneStr;

    @Autowired
    private HttpApiService httpApiService;

    /**
     * 用于获取access_token的url
     */
    private static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=%s&appid=%s&secret=%s";

    /**
     * 获取临时二维码ticket的url
     */
    private static final String GET_QRCODE_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";

    /**
     * 获取临时二维码的url
     */
    private static final String GET_QRCODE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s";

    /**
     * 获取微信用户基本信息url
     */
    private static final String GET_USERINFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";

    @Override
    public String verify(String signature, String timestamp, String nonce, String echostr) {
        List<String> list = new ArrayList();
        list.add(nonce);
        list.add(timestamp);
        list.add(token);

        // 将token、timestamp、nonce三个参数进行字典序排序
        Collections.sort(list);
        logger.info("微信服务器发送身份验证请求参数：{}", list);

        // 将排序后的三个参数字拼接成一个字符串并进行sha1加密
        return DigestUtils.sha1Hex(list.get(0) + list.get(1) + list.get(2));
    }

    @Override
    public JSONObject getAccessToken() {
        // 构建url请求
        String url = String.format(GET_ACCESS_TOKEN_URL, grantType, appId, secret);
        // 发送请求
        String responseStr = httpApiService.doGet(url);

        logger.info("获取access_token访问令牌请求结果：{}", responseStr);
        return JSONObject.parseObject(responseStr);
    }

    @Override
    public JSONObject getQrcodeTicket(String accessToken) {
        JSONObject requestJson = new JSONObject();
        requestJson.put("expire_seconds", expireSeconds);
        requestJson.put("action_name", actionName);

        Map<String, Object> actionInfo = new HashMap<>();
        Map<String, String> scene = new HashMap<>();
        scene.put("scene_str", sceneStr);
        actionInfo.put("scene", scene);

        requestJson.put("action_info", actionInfo);

        // 构建url请求
        String url = String.format(GET_QRCODE_TICKET_URL, accessToken);
        // 发送请求
        String responseStr = httpApiService.doPost(url, requestJson.toJSONString());
        logger.info("获取登录二维码ticket凭据请求结果：{}", responseStr);

        return JSONObject.parseObject(responseStr);
    }

    @Override
    public JSONObject getUserInfo(String accessToken, String openId) {
        // 构建url请求
        String url = String.format(GET_USERINFO_URL, accessToken, openId);
        // 发送请求
        String responseStr = httpApiService.doGet(url);

        logger.info("通过accessToken和openId获取微信用户信息：{}", responseStr);

        return JSONObject.parseObject(responseStr);
    }
}
