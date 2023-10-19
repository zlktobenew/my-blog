package com.zlk.filter;

import com.alibaba.fastjson.JSON;
import com.zlk.domain.ResponseResult;
import com.zlk.domain.entity.LoginUser;
import com.zlk.enums.AppHttpCodeEnum;
import com.zlk.utlis.JwtUtil;
import com.zlk.utlis.RedisCache;
import com.zlk.utlis.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //获取请求头中的token
        String token = request.getHeader("token");
        if(!StringUtils.hasText(token)){
            //说明该接口不需要登录，直接放行,也就是未登录状态，请求头为空
            filterChain.doFilter(request,response);
            return;

        }
        //解析获取userid
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token超时 token非法
            //响应告诉前端需要重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        //这一步可以拿到加密的信息
        String userId = claims.getSubject();

        //从redis中获取用户信息
        LoginUser loginUser = redisCache.getCacheObject("bloglogin" + userId);
        //如果获取不到
        if(Objects.isNull(loginUser)){
            //说明登录过期 提示重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }


        //认证状态要3个参数进行封装，未认证状态要2个参数
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(loginUser,null,null);
        //因为它要的参数要Authentication，所以先封装一个实现类
        //存入SecurityControllerHolder,因为后面的过滤器会从这里获取用户信息
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //过滤器处理完，放行
        filterChain.doFilter(request,response);
    }
}
