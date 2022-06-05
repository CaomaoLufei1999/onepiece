package com.onepiece.common.utils;

import com.onepiece.common.enums.UserAuthStatusEnums;
import com.onepiece.common.vo.JsonResult;

/**
 * @描述 返回Json构造体工具
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-02
 */
public class JsonResultBuilder {

    public static JsonResult success() {
        return new JsonResult(true);
    }

    public static JsonResult success(String msg) {
        return new JsonResult(true, msg);
    }

    public static <T> JsonResult<T> success(T data) {
        return new JsonResult(true, data);
    }

    public static JsonResult error() {
        return new JsonResult(false);
    }

    public static JsonResult error(String msg) {
        return new JsonResult(false, msg);
    }

    public static JsonResult error(UserAuthStatusEnums resultEnum) {
        return new JsonResult(false, resultEnum);
    }
}
