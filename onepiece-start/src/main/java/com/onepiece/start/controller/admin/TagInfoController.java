package com.onepiece.start.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.onepiece.common.pojo.TagInfo;
import com.onepiece.common.utils.JsonResultBuilder;
import com.onepiece.common.vo.JsonResult;
import com.onepiece.start.service.TagInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @描述 文章标签相关操作的Controller接口
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-04
 */
@RestController
@RequestMapping("/tag")
@CrossOrigin
public class TagInfoController {

    @Autowired
    private TagInfoService tagInfoService;

    /**
     * 获取文章标签列表
     *
     * @return
     */
    @GetMapping("/list")
    public JsonResult getCategoryList() {
        List<TagInfo> tagInfoList = tagInfoService.getTagList();
        return JsonResultBuilder.success(tagInfoList);
    }

    /**
     * 新增文章标签
     *
     * @param params 前端提交的创建文章分类的表单参数
     * @return
     */
    @PostMapping("/add")
    public JsonResult addCategory(@RequestBody JSONObject params) {
        String tagName = params.get("tagName").toString();
        Integer categoryId = Integer.valueOf(params.get("categoryId").toString());
        Integer status = Integer.valueOf(params.get("status").toString());

        // 创建标签之前进行查重校验
        TagInfo tagInfoTemp = tagInfoService.getTagByName(tagName);
        if (tagInfoTemp != null){
            return JsonResultBuilder.error("该标签已经存在！");
        }else {
            // 新增标签
            TagInfo tagInfo = tagInfoService.addTag(tagName, categoryId, status);
            return JsonResultBuilder.success(tagInfo);
        }
    }
}
