package com.atguigu.yygh.common.exception;

import com.atguigu.yygh.common.result.ResultCodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 自定义全局异常类
 */

@Data
@Schema(defaultValue = "自定义异常类")
public class YyghException extends RuntimeException{
    @Schema(description = "异常状态码")
    private Integer code;
    public YyghException(String message,Integer code){
        super(message);
        this.code=code;
    }
    public YyghException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

}
