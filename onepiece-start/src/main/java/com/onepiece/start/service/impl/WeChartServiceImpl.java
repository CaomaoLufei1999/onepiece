package com.onepiece.start.service.impl;

import com.onepiece.start.service.WeChartService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @描述 对接微信相关接口实现类
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-24
 */
@Service
public class WeChartServiceImpl implements WeChartService {

    private static final Logger logger = LoggerFactory.getLogger(WeChartServiceImpl.class);

    @Value("${wechart.token}")
    private String token;

    @Override
    public String verify(String signature, String timestamp, String nonce, String echostr) {
        List<String> list = new ArrayList();
        list.add(nonce);
        list.add(timestamp);
        list.add(token);


        // 将token、timestamp、nonce三个参数进行字典序排序
        Collections.sort(list);
        logger.info("微信服务器发送身份验证请求参数：{}",list);

        // 将排序后的三个参数字拼接成一个字符串并进行sha1加密
        return DigestUtils.sha1Hex(list.get(0) + list.get(1) + list.get(2));
    }


}
