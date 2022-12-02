package com.yourname.backen.hander;

import com.yourname.backen.common.R;
import com.yourname.backen.exception.BusinessException;
import com.yourname.backen.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description TODO
 * @Author dell
 * @Date 10/18/2022 10:23 AM
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public R exceptionHandler(RuntimeException e){
        log.error("Unknow exception",e);
        if(e instanceof ParamException || e instanceof BusinessException){
            return R.failed(e.getMessage());
        }
        return R.failed("系统异常");
    }

    @ExceptionHandler(value = Error.class)
    @ResponseBody
    public R errorHandler(Error e){
        log.error("Unknow error",e);
        return R.failed("系统异常，请联系管理员");
    }
}
