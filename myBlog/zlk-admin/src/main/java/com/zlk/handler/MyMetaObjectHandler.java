package com.zlk.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zlk.utlis.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

//mybatisplus字段填充类——————发送评论
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    //关于什么时候填充这些字段，是插入时填充还是更新时填充该字段，在Comment类中有解释
    //执行插入操作会执行到这里，然后会给下列4个字段设置值
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = null;
        //如果是注册用户，没有用户id，所以会抛出异常
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
            userId = -1L;//表示是自己创建
        }
        // 获取当前时间
        Date currentDate = new Date();

        // 创建 Calendar 对象并设置为当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // 将时间增加7个小时
        calendar.add(Calendar.HOUR, 8);

        // 获取增加后的时间
        Date newDate = calendar.getTime();

        //如果不需要增加7个小时，直接把获取当前时间到这里全部删除即可，newDate改为new Date(),下面类似
        this.setFieldValByName("createTime", newDate, metaObject);
        this.setFieldValByName("createBy",userId , metaObject);
        this.setFieldValByName("updateTime", newDate, metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
    }




    @Override
    public void updateFill(MetaObject metaObject) {
        // 获取当前时间
        Date currentDate = new Date();

        // 创建 Calendar 对象并设置为当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // 将时间增加7个小时
        calendar.add(Calendar.HOUR, 8);
        try {
            Long userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 获取增加后的时间
        Date newDate = calendar.getTime();
        this.setFieldValByName("updateTime", newDate, metaObject);
        this.setFieldValByName("updateBy", SecurityUtils.getUserId(), metaObject);
}}