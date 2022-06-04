package com.onepiece.start.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.onepiece.common.pojo.CategoryInfo;
import com.onepiece.start.service.CategoryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @描述 文章分类相关操作的Controller接口
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-04
 */
@RestController
@RequestMapping("/category")
public class CategoryInfoController {

    @Autowired
    private CategoryInfoService categoryInfoService;

    private JSONObject result = null;

    /**
     * 获取文章分类列表
     *
     * @return
     */
    @GetMapping("/list")
    public JSONObject getCategoryList() {
        List<CategoryInfo> categoryList = categoryInfoService.getCategoryList();
        result = new JSONObject();
        result.put("categoryList", categoryList);
        return result;
    }

    /**
     * 新增文章分类
     *
     * @param categoryName
     * @param description
     * @return
     */
    @PostMapping("/add")
    public JSONObject addCategory(String categoryName, String description, Integer status) {
        CategoryInfo categoryInfo = categoryInfoService.addCategory(categoryName, description, status);
        result = new JSONObject();
        result.put("categoryInfo", categoryInfo);
        return result;
    }
}
