package com.onepiece.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @描述 从微信开放平台获取到的登录用户基本信息
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class WeChatUserInfoDTO {

    /**
     * 微信用户唯一标识
     */
    private String openid;

    /**
     * 微信开放平台统一标识
     */
    private String unionid;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别
     */
    private Integer sex;

    private String country;
    private String province;
    private String city;

    /**
     * 头像
     */
    @JSONField(name = "headimgurl")
    private String headImgUrl;

    private String language;
    private String privilege;
}
