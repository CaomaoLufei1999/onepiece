package com.onepiece.start.controller;

import com.onepiece.common.config.AliyunSmsConfig;
import com.onepiece.common.utils.MailClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @描述 Demo测试
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-24
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private AliyunSmsConfig aliyunSmsConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MailClientUtil mailClientUtil;

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    /**
     * 测试短信发送
     *
     * @param phone
     * @return
     */
    @GetMapping("/send/{phone}")
    public String sendSms(@PathVariable("phone") String phone) {
        // 根据手机号从redis中拿验证码
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return phone + " : " + code + "已经存在，还没有过期！";
        }

        // 如果redis 中根据手机号拿不到验证码，则生成6位随机验证码
        code = String.valueOf(UUID.randomUUID().toString().hashCode()).substring(0, 6);

        // 验证码存入codeMap
        Map<String, Object> codeMap = new HashMap<>();
        codeMap.put("code", code);

        // 调用aliyunSendSmsService发送短信
        Boolean bool = aliyunSmsConfig.sendMessage(phone,  codeMap);

        if (bool) {
            // 如果发送成功，则将生成的4位随机验证码存入redis缓存,5分钟后过期
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return phone + " ： " + code + "发送成功！";
        } else {
            return phone + " ： " + code + "发送失败！";
        }
    }

    /**
     * 测试邮件发送
     *
     * @param mail
     * @return
     */
    @GetMapping("/sendMail/{mail}")
    public String sendMail(@PathVariable("mail") String mail) {

        Boolean result = mailClientUtil.sendMail(mail, "测试邮件发送", "xxx");
        if (result){
            return "测试邮件发送成功！";
        }
        return "测试邮件发送失败！";
    }
}
