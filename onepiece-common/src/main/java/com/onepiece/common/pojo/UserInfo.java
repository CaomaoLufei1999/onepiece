package com.onepiece.common.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @描述 用户基本信息实体类
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class UserInfo implements Serializable {
    /**
     * 用户id：主键
     */
    private Integer userId;

    /**
     * 微信/QQ唯一标识openid
     */
    private String openid;

    /**
     * 微信开放平台/QQ互联平台用户统一标识
     */
    private String unionid;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别：1为男性，2为女性
     */
    private Integer sex;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 账号绑定类型：WeChat、QQ
     */
    private String accountBindingType;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 密保问题，格式为JSON字符串
     */
    private String securityQuestions;

    /**
     * 兴趣标签，格式为JSON字符串
     */
    private String interestTags;

    /**
     * 友情链接，格式为JSON数组字符串
     */
    private String friendLinks;

    /**
     * 个人介绍
     */
    private String description;

    /**
     * 身份标识：0普通用户，1管理员
     */
    private Integer privilege;

    /**
     * 身份头衔
     */
    private String identity;

    /**
     * 博客等级
     */
    private Integer level;

    /**
     * 活跃积分
     */
    private Integer points;

    /**
     * 注册时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 账号是否可用, 默认为1（可用）
     */
    private String enabled;
}
