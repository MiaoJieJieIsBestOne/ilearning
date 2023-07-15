package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常
     * @param exception
     * @return
     */
    @ExceptionHandler(XueChengPlusException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse customException(XueChengPlusException exception){
        log.error("系统异常: {}",exception.getErrMessage(),exception);
        return new RestErrorResponse(exception.getErrMessage());
    }

    /**
     * JSR303校验异常
     * @param exception
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(MethodArgumentNotValidException exception){
        BindingResult bindingResult = exception.getBindingResult();
        ArrayList<String> errors = new ArrayList<>();
        bindingResult.getFieldErrors().stream()
                .forEach(e->errors.add(e.getDefaultMessage()));
        String errMessage = StringUtils.join(errors, ",");

        log.error("系统异常: {}{}",exception.getMessage(),errMessage);
        return new RestErrorResponse(CommonError.JSR303_ERROR.getErrMessage()+"-->"+errMessage);
    }

    /**
     * 系统异常
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(Exception exception){
        log.error("系统异常: {}",exception.getMessage(),exception);
        return new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage()+"-->"+exception.getMessage());
    }

}
