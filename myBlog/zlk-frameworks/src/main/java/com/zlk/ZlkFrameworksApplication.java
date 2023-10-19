package com.zlk;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.zlk.config")
@MapperScan("com.zlk.mapper")
@SpringBootApplication
public class ZlkFrameworksApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZlkFrameworksApplication.class, args);
    }
}
