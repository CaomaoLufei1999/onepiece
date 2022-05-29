package com.onepiece.start.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @描述 文件操作相关接口
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-29
 */
public interface FileOperateService {

    /**
     * 文件上传: 图片
     *
     * @param uploadFile 文件
     * @return 返回文件URL地址
     */
    String uploadImg(MultipartFile uploadFile);

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @param response 响应结果
     * @return
     */
    String download(String fileName, HttpServletResponse response);

    /**
     * 文件删除
     *
     * @param fileName 文件名称
     * @return
     */
    String delete(String fileName);
}
