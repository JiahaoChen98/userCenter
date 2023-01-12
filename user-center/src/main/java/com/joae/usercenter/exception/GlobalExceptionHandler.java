package com.joae.usercenter.exception;

import com.joae.usercenter.common.BaseResponse;
import com.joae.usercenter.common.ErrorCode;
import com.joae.usercenter.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 捕获异常并处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("businessException:"+e.getMessage(), e);
        return ResultUtil.error(e.getCode(),e.getMessage(),e.getDescription());


    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse businessExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtil.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");

    }
}
