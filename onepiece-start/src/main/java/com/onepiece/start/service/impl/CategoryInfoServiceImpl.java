package com.onepiece.start.service.impl;

import com.onepiece.common.pojo.CategoryInfo;
import com.onepiece.start.mapper.CategoryInfoMapper;
import com.onepiece.start.service.CategoryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @描述 文章分类相关Service接口实现类
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-04
 */
@Service
public class CategoryInfoServiceImpl implements CategoryInfoService {

    @Autowired
    private CategoryInfoMapper categoryInfoMapper;

    @Override
    public List<CategoryInfo> getCategoryList() {
        return categoryInfoMapper.getCategoryList();
    }

    @Override
    public CategoryInfo addCategory(String categoryName, String description, Integer status) {
        CategoryInfo categoryInfo = new CategoryInfo()
                .setCategoryName(categoryName)
                .setDescription(description)
                .setIsDelete(0)
                .setStatus(status)
                .setCreateTime(new Date());

        Integer integer = categoryInfoMapper.addCategory(categoryInfo);
        return categoryInfo;
    }

    @Override
    public CategoryInfo getCategoryByName(String categoryName) {
        return categoryInfoMapper.getCategoryByName(categoryName);
    }
}
