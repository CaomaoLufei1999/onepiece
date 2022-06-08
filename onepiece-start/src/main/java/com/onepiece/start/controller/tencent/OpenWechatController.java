package com.onepiece.start.controller.tencent;

import com.alibaba.fastjson.JSONObject;
import com.onepiece.common.dto.WeChatUserInfoDTO;
import com.onepiece.common.pojo.UserInfo;
import com.onepiece.common.utils.JwtUtil;
import com.onepiece.start.service.OpenWechatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @描述 微信开放平台对接的Controller
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-07
 */
@RestController
@RequestMapping("/open-wechat")
public class OpenWechatController {

    private static final Logger logger = LoggerFactory.getLogger(OpenWechatController.class);

    @Autowired
    private OpenWechatService openWechatService;

    @RequestMapping("/callback")
    public JSONObject callBack(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();

        String code = request.getParameter("code");
        String state = request.getParameter("state");
        logger.info("向微信开放平台申请扫码登录后，获取到的code：{}", code);

        JSONObject accessTokenJson = openWechatService.getAccessToken(code);

        String accessToken = accessTokenJson.get("access_token").toString();
        String openId = accessTokenJson.get("openid").toString();

        JSONObject userInfoJson = openWechatService.getUserInfo(accessToken, openId);
        WeChatUserInfoDTO weChatUserInfoDTO = userInfoJson.toJavaObject(WeChatUserInfoDTO.class);

        UserInfo userInfo = openWechatService.WeChatLogin(weChatUserInfoDTO);

        if (userInfo != null) {
            String jwtToken = JwtUtil.getJwtToken(userInfo);

            jsonObject.put("success", true);
            jsonObject.put("msg", "登录成功！");
            jsonObject.put("jwtToken", jwtToken);
            jsonObject.put("userInfo", userInfo);
        }else {
            jsonObject.put("success", false);
            jsonObject.put("msg", "登录失败！");
        }
        return jsonObject;
    }

    /**
     * 获取QQ授权登录URL返回给前端
     *
     * @return
     */
    @GetMapping("/getLoginUrl")
    public String getLoginUrl() {
        return openWechatService.getLoginUrl();
    }
}
