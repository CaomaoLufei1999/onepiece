package com.onepiece.common.vo;

import com.onepiece.common.enums.UserAuthStatusEnums;
import lombok.Data;

import java.io.Serializable;

/**
 * @描述 json返回体
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-02
 */
@Data
public class JsonResult<T> implements Serializable {

    private Boolean success;
    private Integer code;
    private String msg;
    private T data;

    public JsonResult() {
    }

    public JsonResult(boolean success) {
        this.success = success;
        this.code = success ? UserAuthStatusEnums.SUCCESS.getCode() : UserAuthStatusEnums.COMMON_FAIL.getCode();
        this.msg = success ? UserAuthStatusEnums.SUCCESS.getMessage() : UserAuthStatusEnums.COMMON_FAIL.getMessage();
    }

    public JsonResult(boolean success, UserAuthStatusEnums resultEnum) {
        this.success = success;
        this.code = success ? UserAuthStatusEnums.SUCCESS.getCode() : (resultEnum == null ? UserAuthStatusEnums.COMMON_FAIL.getCode() : resultEnum.getCode());
        this.msg = success ? UserAuthStatusEnums.SUCCESS.getMessage() : (resultEnum == null ? UserAuthStatusEnums.COMMON_FAIL.getMessage() : resultEnum.getMessage());
    }

    public JsonResult(boolean success, T data) {
        this.success = success;
        this.code = success ? UserAuthStatusEnums.SUCCESS.getCode() : UserAuthStatusEnums.COMMON_FAIL.getCode();
        this.msg = success ? UserAuthStatusEnums.SUCCESS.getMessage() : UserAuthStatusEnums.COMMON_FAIL.getMessage();
        this.data = data;
    }

    public JsonResult(boolean success, UserAuthStatusEnums resultEnum, T data) {
        this.success = success;
        this.code = success ? UserAuthStatusEnums.SUCCESS.getCode() : (resultEnum == null ? UserAuthStatusEnums.COMMON_FAIL.getCode() : resultEnum.getCode());
        this.msg = success ? UserAuthStatusEnums.SUCCESS.getMessage() : (resultEnum == null ? UserAuthStatusEnums.COMMON_FAIL.getMessage() : resultEnum.getMessage());
        this.data = data;
    }
}
