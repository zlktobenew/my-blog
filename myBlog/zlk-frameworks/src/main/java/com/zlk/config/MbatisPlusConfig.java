package com.zlk.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**

 这段代码是一个 Spring Boot 配置类，用于配置 MyBatis-Plus 的分页插件
 并将其添加到 MyBatis-Plus 的拦截器链中。
 */
@Configuration
public class MbatisPlusConfig {

    /**
     * 3.4.0之后版本
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }
}
/*
* @Configuration 注解表示这是一个 Spring 配置类，用于配置应用程序的一些组件。
mybatisPlusInterceptor() 方法使用 @Bean 注解，它会被 Spring 容器管理，并返回一个 MybatisPlusInterceptor 对象。
在 mybatisPlusInterceptor() 方法中，首先创建了一个 MybatisPlusInterceptor 对象 mybatisPlusInterceptor。
然后，通过 mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor())
* 添加了一个内部拦截器 PaginationInnerInterceptor 到 mybatisPlusInterceptor 中。这个内部拦截器是 MyBatis-Plus 提供的分页插件，
* 用于在查询中自动处理分页操作。
最后，将配置好的 mybatisPlusInterceptor 返回。*/