package com.onepiece.common.enums;

/**
 * @描述 文件操作结果状态枚举类
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-29
 */
public enum FileOperateStatusEnum {

    FILE_OPERATE_SUCCESS("文件操作成功", "2000"),
    FILE_OPERATE_ERROR("文件操作失败", "5000"),
    INCORRECT_FILE("文件格式不满足要求", "5001");

    private String msg;
    private String code;

    FileOperateStatusEnum(String msg, String code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
