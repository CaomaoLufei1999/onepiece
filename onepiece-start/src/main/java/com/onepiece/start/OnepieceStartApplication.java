package com.onepiece.start;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @描述 SpringBoot主启动类
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-24
 */
@EnableConfigurationProperties
@MapperScan("com.onepiece.start.mapper")
@SpringBootApplication(scanBasePackages = {"com.onepiece.start", "com.onepiece.common"})
public class OnepieceStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnepieceStartApplication.class, args);
    }
}
