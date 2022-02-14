package com.atguigu.yygh.common.exception;

import com.atguigu.yygh.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {
//    统一处理异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
//    返回的数据为json形式
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }


//    自定义异常处理
    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public Result error(YyghException e){
        e.printStackTrace();
        return Result.fail();
    }
}
