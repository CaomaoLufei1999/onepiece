package com.onepiece.start.service.impl;

import com.onepiece.common.pojo.CategoryInfo;
import com.onepiece.common.pojo.TagInfo;
import com.onepiece.common.vo.CategoryInfoVO;
import com.onepiece.start.mapper.CategoryInfoMapper;
import com.onepiece.start.mapper.TagInfoMapper;
import com.onepiece.start.service.CategoryInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @描述 文章分类相关Service接口实现类
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-04
 */
@Service
public class CategoryInfoServiceImpl implements CategoryInfoService {

    @Autowired
    private CategoryInfoMapper categoryInfoMapper;

    @Autowired
    private TagInfoMapper tagInfoMapper;

    @Override
    public List<CategoryInfoVO> getCategoryList() {
        List<CategoryInfo> categoryList = categoryInfoMapper.getCategoryList();
        List<CategoryInfoVO> categoryInfoVOList = new ArrayList<>();
        categoryList.forEach(category -> {
            List<TagInfo> tagInfoList = tagInfoMapper.getTagListByCategoryId(category.getCategoryId());
            CategoryInfoVO categoryInfoVO = new CategoryInfoVO();
            categoryInfoVO.setTagInfoList(tagInfoList);

            // 将CategoryInfo的字段值拷贝到CategoryInfoVO
            BeanUtils.copyProperties(category, categoryInfoVO);
            categoryInfoVOList.add(categoryInfoVO);
        });
        return categoryInfoVOList;
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

    @Override
    public List<Map<Integer, String>> getCategoryIds() {
        return categoryInfoMapper.getCategoryIds();
    }
}
