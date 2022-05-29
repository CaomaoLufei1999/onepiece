package com.onepiece.start.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.onepiece.common.config.AliyunOssConfig;
import com.onepiece.common.enums.FileOperateStatusEnum;
import com.onepiece.start.service.FileOperateService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @描述 文件操作相关接口实现类
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-29
 */
@Service
public class FileOperateServiceImpl implements FileOperateService {

    private static final Logger logger = LoggerFactory.getLogger(FileOperateServiceImpl.class);

    /**
     * 允许上传文件(图片)的格式
     */
    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg", ".jpeg", ".gif", ".png"};

    /**
     * 注入阿里云oss文件服务器客户端
     */
    @Autowired
    private OSS ossClient;

    /**
     * 注入阿里云OSS基本配置类
     */
    @Autowired
    private AliyunOssConfig aliyunOssConfig;

    @Override
    public String uploadImg(MultipartFile uploadFile) {
        String bucketName = aliyunOssConfig.getBucketName();
        String endpoint = aliyunOssConfig.getEndPoint();
        // 目标文件夹
        String filehost = aliyunOssConfig.getFileHost();
        // 返回图片上传后返回的url
        String returnImgeUrl = "";

        // 校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(), type)) {
                isLegal = true;
                break;
            }
        }

        if (!isLegal) {
            // 图片格式不合法
            return FileOperateStatusEnum.INCORRECT_FILE.getCode();
        }
        // 获取文件原名称
        String originalFilename = uploadFile.getOriginalFilename();
        // 获取文件类型
        String fileType = null;
        if (originalFilename != null) {
            fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        // 新文件名称
        String newFileName = UUID.randomUUID() + fileType;
        // 构建日期路径, 例如：OSS目标文件夹/2020/10/31/文件名
        String filePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        // 文件上传的路径地址
        String uploadImgeUrl = filehost + "/" + filePath + "/" + newFileName;

        // 获取文件输入流
        InputStream inputStream = null;
        try {
            inputStream = uploadFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * TODO 下面两行代码是重点坑：
         * 现在阿里云OSS 默认图片上传ContentType是image/jpeg
         * 也就是说，获取图片链接后，图片是下载链接，而并非在线浏览链接，
         * 因此，这里在上传的时候要解决ContentType的问题，将其改为image/jpg
         */
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType("image/jpg");

        // 文件上传至阿里云OSS
        ossClient.putObject(bucketName, uploadImgeUrl, inputStream, meta);
        /**
         * TODO 文件上传成功后，数据库中存储文件地址
         */
        // 获取文件上传后的图片返回地址
        returnImgeUrl = "http://" + bucketName + "." + endpoint + "/" + uploadImgeUrl;

        return returnImgeUrl;
    }

    @Override
    public String download(String fileName, HttpServletResponse response) {
        // 文件名以附件的形式下载
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("FileOperateServiceImpl file download error: {}", e.getMessage());
        }
        // 获取oss的Bucket名称
        String bucketName = aliyunOssConfig.getBucketName();
        // 获取oss目标文件夹
        String fileHost = aliyunOssConfig.getFileHost();

        // 日期目录:
        // TODO 注意，这里虽然写成这种固定获取日期目录的形式，逻辑上确实存在问题，但是实际上，filePath的日期目录应该是从数据库查询的
        String filePath = new DateTime().toString("yyyy/MM/dd");

        String fileKey = fileHost + "/" + filePath + "/" + fileName;
        // OSSObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(bucketName, fileKey);
        try {
            // 读取文件内容。
            InputStream inputStream = ossObject.getObjectContent();
            // 把输入流放入缓存流
            BufferedInputStream in = new BufferedInputStream(inputStream);
            ServletOutputStream outputStream = response.getOutputStream();
            // 把输出流放入缓存流
            BufferedOutputStream out = new BufferedOutputStream(outputStream);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
            in.close();
            return FileOperateStatusEnum.FILE_OPERATE_SUCCESS.getCode();
        } catch (Exception e) {
            return FileOperateStatusEnum.FILE_OPERATE_ERROR.getCode();
        }
    }

    @Override
    public String delete(String fileName) {
        String bucketName = aliyunOssConfig.getBucketName();
        String endpoint = aliyunOssConfig.getEndPoint();
        String accessKeySecret = aliyunOssConfig.getAccessKeySecret();
        String accessKeyId = aliyunOssConfig.getAccessKeyId();
        String fileHost = aliyunOssConfig.getFileHost();

        // 日期目录:
        // TODO，这里虽然写成这种固定获取日期目录的形式，逻辑上确实存在问题，但是实际上，filePath的日期目录应该是从数据库查询的
        String filePath = new DateTime().toString("yyyy/MM/dd");

        try {
            /**
             * TODO 注意：在实际项目中，不需要删除OSS文件服务器中的文件，
             * 只需要删除数据库存储的文件路径即可！
             */
            // 建议在方法中创建OSSClient 而不是使用@Bean注入，不然容易出现connection pool shut down
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

            // 根据BucketName,filetName删除文件
            String fileKey = fileHost + "/" + filePath + "/" + fileName;

            try {
                ossClient.deleteObject(bucketName, fileKey);
            } finally {
                ossClient.shutdown();
            }
            return FileOperateStatusEnum.FILE_OPERATE_SUCCESS.getCode();
        } catch (Exception e) {
            e.printStackTrace();
            return FileOperateStatusEnum.FILE_OPERATE_ERROR.getCode();
        }
    }
}
