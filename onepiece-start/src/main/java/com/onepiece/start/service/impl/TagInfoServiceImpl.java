package com.onepiece.start.service.impl;

import com.onepiece.common.pojo.TagInfo;
import com.onepiece.start.mapper.TagInfoMapper;
import com.onepiece.start.service.TagInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @描述 文章标签相关Service接口实现类
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-04
 */
@Service
public class TagInfoServiceImpl implements TagInfoService {

    @Autowired
    private TagInfoMapper tagInfoMapper;

    @Override
    public List<TagInfo> getTagList() {
        return tagInfoMapper.getTagList();
    }

    @Override
    public TagInfo addTag(String tagName, Integer categoryId, Integer status) {
        TagInfo tagInfo = new TagInfo()
                .setTagName(tagName)
                .setCategoryId(categoryId)
                .setIsDelete(0)
                .setStatus(status)
                .setCreateTime(new Date());

        Integer integer = tagInfoMapper.addTag(tagInfo);
        return tagInfo;
    }

    @Override
    public TagInfo getTagByName(String tagName) {
        return tagInfoMapper.getTagByName(tagName);
    }

    @Override
    public List<TagInfo> getTagListByCategoryId(Integer categoryId) {
        return tagInfoMapper.getTagListByCategoryId(categoryId);
    }

    @Override
    public Boolean deleteTag(Integer tagId) {
        return tagInfoMapper.deleteTag(tagId) > 0;
    }
}
