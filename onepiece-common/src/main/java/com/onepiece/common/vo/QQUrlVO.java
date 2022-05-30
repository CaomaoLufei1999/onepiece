package com.onepiece.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @描述
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QQUrlVO implements Serializable {
    private String url;
}
