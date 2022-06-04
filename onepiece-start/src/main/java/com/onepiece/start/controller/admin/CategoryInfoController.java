package com.onepiece.start.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.onepiece.common.pojo.CategoryInfo;
import com.onepiece.common.utils.JsonResultUtil;
import com.onepiece.common.vo.JsonResult;
import com.onepiece.start.service.CategoryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @描述 文章分类相关操作的Controller接口
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-04
 */
@RestController
@RequestMapping("/category")
@CrossOrigin
public class CategoryInfoController {

    @Autowired
    private CategoryInfoService categoryInfoService;

    /**
     * 获取文章分类列表
     *
     * @return
     */
    @GetMapping("/list")
    public JSONObject getCategoryList() {
        List<CategoryInfo> categoryList = categoryInfoService.getCategoryList();
        JSONObject result = new JSONObject();
        result.put("categoryList", categoryList);
        return result;
    }

    /**
     * 新增文章分类
     *
     * @param params 前端提交的创建文章分类的表单参数
     * @return
     */
    @PostMapping("/add")
    public JsonResult addCategory(@RequestBody JSONObject params) {
        String categoryName = params.get("categoryName").toString();
        String description = params.get("description").toString();
        Integer status = Integer.valueOf(params.get("status").toString());

        // TODO 发布文章分类之前进行查重校验
        CategoryInfo categoryInfo = categoryInfoService.addCategory(categoryName, description, status);
        return JsonResultUtil.success(categoryInfo);
    }
}
