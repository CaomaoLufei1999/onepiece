package com.onepiece.common.vo;

import com.onepiece.common.pojo.CategoryInfo;
import com.onepiece.common.pojo.TagInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @描述 文章分类详情VO
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class CategoryInfoVO extends CategoryInfo {
    List<TagInfo> tagInfoList;

}
