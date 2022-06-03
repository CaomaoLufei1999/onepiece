package com.onepiece.common.utils;

import com.onepiece.common.enums.UserAuthStatusEnums;
import com.onepiece.common.vo.JsonResult;

/**
 * @描述 返回Json构造体工具
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-02
 */
public class JsonResultUtil {
    public static JsonResult success() {
        return new JsonResult(true);
    }

    public static <T> JsonResult<T> success(T data) {
        return new JsonResult(true, data);
    }

    public static JsonResult fail() {
        return new JsonResult(false);
    }

    public static JsonResult fail(UserAuthStatusEnums resultEnum) {
        return new JsonResult(false, resultEnum);
    }
}
