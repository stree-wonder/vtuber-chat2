package com.xhq.common.Exception;

import com.xhq.common.domain.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler // status = 200
//    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Result handler401(Exception401 e) {
        return Result.error(401, e.getMessage());
    }
}
