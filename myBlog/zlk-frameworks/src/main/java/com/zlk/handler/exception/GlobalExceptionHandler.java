package com.zlk.handler.exception;

import com.zlk.domain.ResponseResult;
import com.zlk.enums.AppHttpCodeEnum;
import com.zlk.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


//它相当于@ControllerAdvice+@RequestBody
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //用于SystemException这个自定义异常的捕获，可以抛出更详细的异常信息
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        //打印异常信息
        log.error("出现了异常！ {}",e);
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(e.getCode(),e.getMsg());
    }


    //处理其他没有预料到的异常
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){
        //打印异常信息
        log.error("出现了异常！ {}",e);
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }
}