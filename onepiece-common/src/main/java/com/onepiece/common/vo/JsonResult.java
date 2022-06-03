package com.onepiece.common.vo;

import com.onepiece.common.enums.UserAuthStatusEnums;

import java.io.Serializable;

/**
 * @描述 json返回体
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-02
 */
public class JsonResult<T> implements Serializable {

    private Boolean success;
    private Integer errorCode;
    private String errorMsg;
    private T data;

    public JsonResult() {
    }

    public JsonResult(boolean success) {
        this.success = success;
        this.errorCode = success ? UserAuthStatusEnums.SUCCESS.getCode() : UserAuthStatusEnums.COMMON_FAIL.getCode();
        this.errorMsg = success ? UserAuthStatusEnums.SUCCESS.getMessage() : UserAuthStatusEnums.COMMON_FAIL.getMessage();
    }

    public JsonResult(boolean success, UserAuthStatusEnums resultEnum) {
        this.success = success;
        this.errorCode = success ? UserAuthStatusEnums.SUCCESS.getCode() : (resultEnum == null ? UserAuthStatusEnums.COMMON_FAIL.getCode() : resultEnum.getCode());
        this.errorMsg = success ? UserAuthStatusEnums.SUCCESS.getMessage() : (resultEnum == null ? UserAuthStatusEnums.COMMON_FAIL.getMessage() : resultEnum.getMessage());
    }

    public JsonResult(boolean success, T data) {
        this.success = success;
        this.errorCode = success ? UserAuthStatusEnums.SUCCESS.getCode() : UserAuthStatusEnums.COMMON_FAIL.getCode();
        this.errorMsg = success ? UserAuthStatusEnums.SUCCESS.getMessage() : UserAuthStatusEnums.COMMON_FAIL.getMessage();
        this.data = data;
    }

    public JsonResult(boolean success, UserAuthStatusEnums resultEnum, T data) {
        this.success = success;
        this.errorCode = success ? UserAuthStatusEnums.SUCCESS.getCode() : (resultEnum == null ? UserAuthStatusEnums.COMMON_FAIL.getCode() : resultEnum.getCode());
        this.errorMsg = success ? UserAuthStatusEnums.SUCCESS.getMessage() : (resultEnum == null ? UserAuthStatusEnums.COMMON_FAIL.getMessage() : resultEnum.getMessage());
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
