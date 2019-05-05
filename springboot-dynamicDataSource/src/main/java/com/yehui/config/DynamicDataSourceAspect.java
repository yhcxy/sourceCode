package com.yehui.config;


import com.yehui.annontaion.DBSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author yehui
 * 自定义注解 + AOP的方式实现数据源动态切换。
 */
@Aspect
@Component
public class DynamicDataSourceAspect {
    private static final Logger log = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    @Before("@annotation(DBSource)")
    public void beforeSwitchDB(JoinPoint joinPoint,DBSource DBSource){
        //获取目标类的方法
        Class<?> aClass = joinPoint.getTarget().getClass();
        //获得访问的方法名
        String methodName = joinPoint.getSignature().getName();
        //得到方法的参数类型
        Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        String dataSource = DynamicDataSourceContextHolder.DEFAULT_DS;
        try {
            Method method = aClass.getMethod(methodName, parameterTypes);
            if(method.isAnnotationPresent(DBSource.class)){
                DBSource db = method.getAnnotation(DBSource.class);
                //指定数据源
                dataSource = db.value();
            }else{
                //轮训设置数据源
                dataSource = DynamicDataSourceContextHolder.getSlaveDB();
            }
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage(), e);
        }
        //设置数据源
        DynamicDataSourceContextHolder.setDB(dataSource);
    }

    @After("@annotation(DBSource)")
    public void afterSwitchDB(DBSource DBSource){
        DynamicDataSourceContextHolder.removeDB();
    }
}
