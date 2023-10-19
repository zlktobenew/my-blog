package com.zlk.service.impl;

import com.zlk.domain.ResponseResult;
import com.zlk.domain.entity.LoginUser;
import com.zlk.domain.entity.User;
import com.zlk.domain.vo.BlogUserLoginVo;
import com.zlk.domain.vo.UserInfoVo;
import com.zlk.service.BlogLoginService;
import com.zlk.service.LoginService;
import com.zlk.utlis.BeanCopyUtils;
import com.zlk.utlis.JwtUtil;
import com.zlk.utlis.RedisCache;
import com.zlk.utlis.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SystemLoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        //1.authenticationManager会自动调用userDetailService进行数据库查询
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        //这里重新创建UserDetail的实现类，我们要从数据库查询username和password
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //2.判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }

        //3.获取userId生成token
        LoginUser loginUser= (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        //由于这里要String类型，所以需要把前面的userId转换成字符串
        String jwt = JwtUtil.createJWT(userId);

        //4.把用户信息存入redis
        redisCache.setCacheObject("adminlogin"+userId,loginUser);

        //把token封装 返回
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        //获取当前登录的用户id
        Long userId = SecurityUtils.getUserId();
        //删除redis中对应的值
        redisCache.deleteObject("adminlogin"+userId);
        return ResponseResult.okResult();
    }

}
