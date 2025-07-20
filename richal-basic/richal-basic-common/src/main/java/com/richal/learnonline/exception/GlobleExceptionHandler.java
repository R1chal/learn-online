package com.richal.learnonline.exception;

import com.richal.learnonline.constant.ErrorCode;
import com.richal.learnonline.result.JSONResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author Richal
 * @since 2025/07/20
 */
@RestControllerAdvice
public class GlobleExceptionHandler {

    /**
     * 拦截异常
     *
     * @param e e
     * @return JSONResult
     */
    @ExceptionHandler(GlobleBusinessException.class)
    public JSONResult globleException(GlobleBusinessException e) {
        e.printStackTrace();
        return JSONResult.error(e.getMessage());
    }

    /**
     * 拦截器其他异常
     *
     * @param e e
     * @return JSONResult
     */
    @ExceptionHandler(Exception.class)
    public JSONResult exception(Exception e) {
        e.printStackTrace();
        return JSONResult.error(ErrorCode.NETWORK_ERROR);
    }

    /**
     * 拦截空值异常
     *
     * @param e e
     * @return JSONResult
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JSONResult methodNotValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        String message = e.getBindingResult().getAllErrors()
                .stream()
                .map(s -> "[" + s.getDefaultMessage() + "]")
                .collect(Collectors.joining());
        return JSONResult.error(message);
    }

}
