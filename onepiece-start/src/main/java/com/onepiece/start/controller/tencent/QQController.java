package com.onepiece.start.controller.tencent;

import com.onepiece.common.dto.QQUserInfoDTO;
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
    public String fallback(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        logger.info("用户点击登录链接，向腾讯开放平台申请登录校验成功后，获取到的code：{}", code);

        String accessToken = (String) qqService.getAccessToken(code).get("access_token");
        String openId = (String) qqService.getOpenIdByAccessToken(accessToken).get("openid");
        QQUserInfoDTO qqUserInfoDTO = qqService.getUserInfo(accessToken, openId);
        // QQ登录
        Integer userId = qqService.QQLogin(openId, qqUserInfoDTO);
        if (userId != null){
            return "登录成功,userId为：: " + userId;
        }
        return "登录失败";
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
