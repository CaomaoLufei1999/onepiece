package com.onepiece.common.enums;

/**
 * @描述 基类枚举
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-05
 */
public enum BaseEnums {

    SUCCESS("操作成功", 2000),
    ERROR("操作失败", 5000);

    private String msg;
    private Integer code;

    BaseEnums(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
