package com.onepiece.start.controller.tencent;

import com.alibaba.fastjson.JSONObject;
import com.onepiece.common.dto.QQUserInfoDTO;
import com.onepiece.common.pojo.UserInfo;
import com.onepiece.common.utils.JwtUtil;
import com.onepiece.start.service.QQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @描述 对接QQ相关的Controller接口
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-30
 */
@RestController
@RequestMapping("/qq")
public class QQController {

    private static final Logger logger = LoggerFactory.getLogger(QQController.class);

    @Autowired
    private QQService qqService;

    @GetMapping("/auth/callback")
    public JSONObject fallback(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        String code = request.getParameter("code");
        logger.info("用户点击登录链接，向腾讯开放平台申请登录校验成功后，获取到的code：{}", code);

        String accessToken = (String) qqService.getAccessToken(code).get("access_token");
        String openId = (String) qqService.getOpenIdByAccessToken(accessToken).get("openid");
        QQUserInfoDTO qqUserInfoDTO = qqService.getUserInfo(accessToken, openId);
        // QQ登录
        UserInfo userInfo = qqService.QQLogin(openId, qqUserInfoDTO);
        if (userInfo != null) {
            String jwtToken = JwtUtil.getJwtToken(userInfo);

            jsonObject.put("success", true);
            jsonObject.put("msg", "登录成功！");
            jsonObject.put("jwtToken", jwtToken);
            jsonObject.put("userInfo", userInfo);
        } else {
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
        return qqService.getLoginUrl();
    }
}
