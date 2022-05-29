package com.onepiece.common.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @描述 阿里云OSS对象存储配置类
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-29
 */
@Configuration // 声明配置类，放入Spring容器
@PropertySource(value = {"classpath:application-aliyun-oss.properties"}) // 指定配置文件位置
@ConfigurationProperties(prefix = "aliyun")
@Data
@Accessors(chain = true)
public class AliyunOssConfig {

    /**
     * oss地域节点
     */
    private String endPoint;

    /**
     * 密钥id
     */
    private String accessKeyId;

    /**
     * 密钥
     */
    private String accessKeySecret;

    /**
     * oss的bucket名称
     */
    private String bucketName;

    /**
     * bucket域名
     */
    private String urlPrefix;

    /**
     * 目标文件夹
     */
    private String fileHost;

    /**
     * 将OSS 客户端交给Spring容器托管
     * @return
     */
    @Bean
    public OSS OSSClient() {
        return new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
    }
}
