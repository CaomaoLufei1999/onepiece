package com.onepiece.common.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @描述 文章标签实体类
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class TagInfo {
    /**
     * 主键: 标签id
     */
    private Integer tagId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 标签所属分类ID
     */
    private Integer categoryId;

    /**
     * 标签所属分类名称
     */
    private String categoryName;

    /**
     * 是否删除: 0未删除，1已删除
     */
    private Integer isDelete;

    /**
     * 标签状态: 0未生效，1已生效
     */
    private Integer status;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
