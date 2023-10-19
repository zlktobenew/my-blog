package com.zlk.annotation;
//自定义注解
//需要元注解

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//代表注解保持到运行时
@Target({ElementType.METHOD})//代表注解可以加上方法上面
public @interface SystemLog {
    //用于指定业务的名字
    String businessName();
}
