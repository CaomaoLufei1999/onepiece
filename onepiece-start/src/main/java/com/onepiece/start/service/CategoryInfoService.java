package com.onepiece.start.service;

import com.onepiece.common.pojo.CategoryInfo;

import java.util.List;
import java.util.Map;

/**
 * @描述 文章分类相关Service接口
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-04
 */
public interface CategoryInfoService {

    /**
     * 获取文章分类列表
     *
     * @return
     */
    List<CategoryInfo> getCategoryList();

    /**
     * 新增文章分类
     *
     * @param categoryName
     * @param description
     * @param status
     * @return
     */
    CategoryInfo addCategory(String categoryName, String description, Integer status);

    /**
     * 根据分类名称获取分类信息
     *
     * @param categoryName
     * @return
     */
    CategoryInfo getCategoryByName(String categoryName);

    /**
     * 获取文章分类ID集合
     *
     * @return
     */
    List<Map<Integer, String>> getCategoryIds();
}
