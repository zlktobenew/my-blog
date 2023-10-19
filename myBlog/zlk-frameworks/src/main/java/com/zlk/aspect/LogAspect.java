package com.zlk.aspect;

import com.alibaba.fastjson.JSON;
import com.zlk.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;


@Slf4j
@Component
@Aspect//切面类
public class LogAspect {

    //指定切点
    @Pointcut("@annotation(com.zlk.annotation.SystemLog)")
    public void pt(){

    }

    //通知方法：环绕通知，可以在目标方法之前之后都可以进行增强
    @Around("pt()")//joinPoint指被增强的方法封装的对象
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //调用目标方法
        Object ret;
        //无论有没有异常，都要打印
        try {
            handleBefore(joinPoint);
            ret = joinPoint.proceed();
            handleAfter(ret);
        } finally {
            // 结束后换行
            log.info("=======End=======" + System.lineSeparator());
        }

        return ret;
    }

    private void handleAfter(Object ret) {
        // 打印出参
        log.info("Response       : {}", JSON.toJSON(ret));
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {
        //要拿到对应的url地址，就要调用RequestContextHolder,再实现这个接口从而拿到request,而url在request当中
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //获取被增强方法的注解对象,这里主要是获得被增强方法的注解对象，从而拿到描述信息
        //所以要把被增强方法的信息传进来，就是joinPoint
        SystemLog systemLog=getSystemLog(joinPoint);

        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}",systemLog.businessName() );
        // 打印 Http method
        log.info("HTTP Method    : {}",request.getMethod() );
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}",joinPoint.getSignature().getDeclaringTypeName(),((MethodSignature) joinPoint.getSignature()).getName());
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSON(joinPoint.getArgs()));
    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        //jointPoint的信息封装在Signature里面，要实现这个接口需要用MethodSignature实现
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        这个signature里面封装了以下方法
       /*
        @PutMapping("/userInfo")
    @SystemLog(businessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }*/
        SystemLog systemLog = signature.getMethod().getAnnotation(SystemLog.class);
        return systemLog;
    }


}
