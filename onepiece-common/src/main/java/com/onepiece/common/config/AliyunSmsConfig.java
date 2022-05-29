package com.onepiece.common.config;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

/**
 * @描述 阿里云SMS短信配置类
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-29
 */
@Configuration // 声明配置类，放入Spring容器
@PropertySource(value = {"classpath:application-aliyun-sms.properties"}, encoding="utf-8") // 指定配置文件位置
@ConfigurationProperties(prefix = "aliyun.sms")
@Data
@Accessors(chain = true)
public class AliyunSmsConfig {

    private static final Logger logger = LoggerFactory.getLogger(AliyunSmsConfig.class);

    private String accessKeyId;
    private String accessKeySecret;
    private String templateCode;
    private String signName;

    /**
     * 发送短信验证码
     *
     * @param phone   接收短信的手机号
     * @param codeMap 验证码 map 集合
     * @return
     */
    public Boolean sendMessage(String phone, Map<String, Object> codeMap) {

        /**
         * 连接阿里云SMS客户端，需要三个参数：
         * regionId 不要动，默认使用官方的
         * accessKeyId 自己的用户 accessKeyId
         * accessSecret 自己的用户 accessSecret
         */
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        // 构建请求：
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25"); // 版本号不可修改！
        request.setSysAction("SendSms");

        // 自定义参数：
        request.putQueryParameter("PhoneNumbers", phone);// 手机号
        request.putQueryParameter("SignName", signName);// 短信签名
        request.putQueryParameter("TemplateCode", templateCode);// 短信模版CODE

        // 构建短信验证码
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(codeMap));

        try {
            CommonResponse response = client.getCommonResponse(request);
            logger.info("AliyunSmsConfig sendMessage response: {}", response.getData());
            if (response.getData() != null){
                String dataStr = response.getData();
                JSONObject data = JSONObject.parseObject(dataStr);
                if (data.get("Code").equals("OK")){
                    return response.getHttpResponse().isSuccess();
                }
            }else {
                return false;
            }
        } catch (ClientException e) {
            logger.error("AliyunSmsConfig sendMessage error: {}", e.getMessage());
        }
        return false;
    }
}
