package com.onepiece.common.enums;

/**
 * @描述 账号绑定类型枚举类
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-01
 */
public enum AccountBindingTypeEnum {

    QQ(0001, "QQ"),
    WECHAT(0002, "WeChat");

    private Integer code;
    private String type;

    AccountBindingTypeEnum(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
