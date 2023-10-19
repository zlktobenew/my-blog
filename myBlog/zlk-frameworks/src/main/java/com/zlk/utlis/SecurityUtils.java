package com.zlk.utlis;

import com.alibaba.fastjson.JSON;
import com.zlk.domain.ResponseResult;
import com.zlk.domain.entity.LoginUser;

import com.zlk.enums.AppHttpCodeEnum;
import com.zlk.exception.SystemException;
import lombok.extern.flogger.Flogger;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

//从SecurityContextHolder中拿到userid
/**
    token解析完userid封装到SecurityContextHolder
 然后获取SecurityContextHolder的上下文对象，转换成Authentication类型
 然后再获取这个对象的Principal属性强转成loginuser类型
 再通过getloginuser获得loginuser的对象，这个对象有user属性，user属性可以获取id
 */
@Slf4j
public class SecurityUtils
{

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
        Authentication authentication = getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser) {
            return (LoginUser) authentication.getPrincipal();
        }
        //响应给前端
        throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Boolean isAdmin(){
        Long id = getLoginUser().getUser().getId();
        return id != null && id.equals(1L);
    }

    public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }
}