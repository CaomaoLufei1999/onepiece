package com.onepiece.start.mapper;

import com.onepiece.common.pojo.CategoryInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @描述 文章分类相关Mapper接口
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-04
 */
@Repository
public interface CategoryInfoMapper {

    /**
     * 获取文章分类列表
     *
     * @return
     */
    List<CategoryInfo> getCategoryList();

    /**
     * 新增文章分类
     *
     * @param categoryInfo
     * @return
     */
    Integer addCategory(CategoryInfo categoryInfo);
}
