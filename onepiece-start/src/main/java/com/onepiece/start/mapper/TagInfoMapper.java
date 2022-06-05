package com.onepiece.start.mapper;

import com.onepiece.common.pojo.TagInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @描述 文章标签相关Mapper接口
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-04
 */
@Repository
public interface TagInfoMapper {

    /**
     * 获取文章标签列表
     *
     * @return
     */
    List<TagInfo> getTagList();

    /**
     * 新增文章标签
     *
     * @param tagInfo
     * @return
     */
    Integer addTag(TagInfo tagInfo);

    /**
     * 根据标签名称获取标签信息
     *
     * @param tagName
     * @return
     */
    TagInfo getTagByName(String tagName);
}
