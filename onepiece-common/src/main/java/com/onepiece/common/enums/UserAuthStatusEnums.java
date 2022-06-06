package com.onepiece.common.enums;

/**
 * @描述 用户登录授权状态枚举
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-02
 */
public enum UserAuthStatusEnums {

    /**
     * 授权成功
     **/
    SUCCESS(2000, "成功"),

    /**
     * 授权失败
     **/
    COMMON_FAIL(4000, "失败"),

    /**
     * 参数错误：1000～1999
     **/
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    /**
     * 用户信息错误
     **/
    USER_NOT_LOGIN(4001, "用户未登录"),
    USER_ACCOUNT_EXPIRED(4002, "账号已过期"),
    USER_CREDENTIALS_ERROR(4003, "密码错误"),
    USER_CREDENTIALS_EXPIRED(4004, "密码过期"),
    USER_ACCOUNT_DISABLE(4005, "账号不可用"),
    USER_ACCOUNT_LOCKED(4006, "账号被锁定"),
    USER_ACCOUNT_NOT_EXIST(4007, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(4008, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(4009, "账号下线"),

    /**
     * 权限错误
     **/
    NO_PERMISSION(3001, "没有权限");

    private Integer code;
    private String message;

    UserAuthStatusEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 根据code获取message
     *
     * @param code
     * @return
     */
    public static String getMessageByCode(Integer code) {
        for (UserAuthStatusEnums ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }
}
