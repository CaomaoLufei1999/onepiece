package com.onepiece.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @描述  从QQ互联获取到的登录用户基本信息
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class QQUserInfoDTO {
    private Integer ret;
    private String msg;

    @JSONField(name = "is_lost")
    private Integer isLost;

    private String nickname;
    private String gender;
    private String province;
    private String city;
    private String year;
    private String constellation;

    @JSONField(name = "figureurl")
    private String figureUrl;

    @JSONField(name = "figureurl_1")
    private String figureUrl1;

    @JSONField(name = "figureurl_2")
    private String figureUrl2;

    @JSONField(name = "figureurl_qq")
    private String figureUrlQQ;

    @JSONField(name = "figureurl_qq_1")
    private String figureUrlQQ1;

    @JSONField(name = "figureurl_qq_2")
    private String figureUrlQQ2;

    @JSONField(name = "is_yellow_vip")
    private String isYellowVip;

    private String vip;

    @JSONField(name = "yellow_vip_level")
    private String yellowVipLevel;

    private String level;

    @JSONField(name = "is_yellow_year_vip")
    private String isYellowYearVip;
}
