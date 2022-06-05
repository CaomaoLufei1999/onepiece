package com.onepiece.start.service;

import com.onepiece.common.pojo.TagInfo;

import java.util.List;

/**
 * @描述 文章标签相关Service接口
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-04
 */
public interface TagInfoService {

    /**
     * 获取文章标签列表
     *
     * @return
     */
    List<TagInfo> getTagList();

    /**
     * 新增文章标签
     * @param tagName
     * @param categoryId
     * @param status
     * @return
     */
    TagInfo addTag(String tagName, Integer categoryId, Integer status);

    /**
     * 根据分类名称获取标签信息
     *
     * @param tagName
     * @return
     */
    TagInfo getTagByName(String tagName);

    /**
     * 根据分类ID获取标签列表
     * @param categoryId
     * @return
     */
    List<TagInfo> getTagListByCategoryId(Integer categoryId);
}
