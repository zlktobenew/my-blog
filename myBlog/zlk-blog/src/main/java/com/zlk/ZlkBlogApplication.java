package com.zlk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// 使用@EnableScheduling注解开启定时任务功能
@EnableScheduling
@MapperScan("com.zlk.mapper")
@SpringBootApplication
////Swagger启动器
//@EnableSwagger2
public class ZlkBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZlkBlogApplication.class, args);
    }
}
