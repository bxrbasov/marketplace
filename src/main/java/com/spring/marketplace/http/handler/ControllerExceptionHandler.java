package com.spring.marketplace.http.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@ControllerAdvice(basePackages = "com.spring.marketplace.http.controller")
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    @RequestMapping("/error")
    public String exceptionHandler(Exception exception) {
        log.error(exception.getMessage(), exception);
        return "error/error500";
    }
}
