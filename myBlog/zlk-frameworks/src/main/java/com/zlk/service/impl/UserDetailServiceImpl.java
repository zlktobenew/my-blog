package com.zlk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.zlk.constants.SystemConstants;
import com.zlk.domain.entity.LoginUser;
import com.zlk.domain.entity.User;
import com.zlk.mapper.MenuMapper;
import com.zlk.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

//前台查询密码账户通过数据库查询
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    //通过userMapper查询数据库
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user=userMapper.selectOne(queryWrapper);
        //判断是否查到用户，如果没查到，抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //返回用户信息
        //查询权限信息封装
        //如果是后台用户才需要查询权限封装
        if(user.getType().equals(SystemConstants.ADMAIN)){
            //拿到权限list集合
            List<String> list = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user,list);
        }
        //不能直接返回User,因为它的返回类型要是UserDetails类型，而它是一个接口
        //这里可以重新创建一个LoginUser类去实现这个接口,把查询到的username和password封装到这里
        return new LoginUser(user,null);//无权限
    }
}
