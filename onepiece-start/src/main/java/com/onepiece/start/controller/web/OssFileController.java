package com.onepiece.start.controller.web;

import com.alibaba.fastjson.JSONObject;
import com.onepiece.common.enums.FileOperateStatusEnum;
import com.onepiece.start.service.FileOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @描述 基于阿里云OSS对象存储进行文件相关操作的Controller接口
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-29
 */
@RestController
@RequestMapping("/file")
public class OssFileController {

    @Autowired
    private FileOperateService fileOperateService;

    /**
     * 上传图片文件
     *
     * @param file 图片文件
     * @return
     */
    @PostMapping("/uploadImg")
    public JSONObject uploadImg(@RequestParam("file") MultipartFile file) {
        JSONObject jsonObject = new JSONObject();
        if (file != null) {
            String returnFileUrl = fileOperateService.uploadImg(file);
            if (returnFileUrl.equals("error")) {
                jsonObject.put("error", "文件上传失败！");
                return jsonObject;
            }
            jsonObject.put("success", "文件上传成功！");
            jsonObject.put("returnFileUrl", returnFileUrl);
            return jsonObject;
        } else {
            jsonObject.put("error", "文件上传失败！");
            return jsonObject;
        }
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @param response 响应体
     * @return
     * @throws Exception
     */
    @GetMapping(value = "download/{fileName}")
    public JSONObject download(@PathVariable("fileName") String fileName, HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();
        String status = fileOperateService.download(fileName, response);
        if (status.equals("5000")) {
            jsonObject.put("error", FileOperateStatusEnum.FILE_OPERATE_ERROR.getMsg());
            return jsonObject;
        } else if (status.equals("5001")) {
            jsonObject.put("error", FileOperateStatusEnum.INCORRECT_FILE.getMsg());
        } else {
            jsonObject.put("success", FileOperateStatusEnum.FILE_OPERATE_SUCCESS.getMsg());
            return jsonObject;
        }
        return jsonObject;
    }

    /**
     * 文件删除
     *
     * @param fileName
     * @return
     */
    @GetMapping("/delete/{fileName}")
    public JSONObject DeleteFile(@PathVariable("fileName") String fileName) {
        JSONObject jsonObject = new JSONObject();

        // TODO 数据库操作
        return jsonObject;
    }
}
