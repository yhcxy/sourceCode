package com.midea.isc.api.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;


public class HttpAspect {

    //在监控方法执行前前执行
    //此处定义切点，使用正则匹配具体的方法
    @Before("@annotation(org.springframework.web.bind.annotation.*Mapping)")
    public void logBefore(JoinPoint joinPoint) {

        ////log.error("1");
    }


    //在监控方法执行后执行
    @After("execution(public * com.midea..*.rpc.*.*(..))")
    public void logAfter() {
       // log.error("2");
    }
}
