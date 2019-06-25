package com.midea.isc.common.config;

import com.midea.isc.common.component.DynamicDataSourceAspect;
import com.midea.isc.common.interceptor.StatementInterceptor;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.*;

@EnableAspectJAutoProxy(/* proxyTargetClass = true, */exposeProxy = true)
public class BeanConfig {
    @Bean
    public DynamicDataSourceAspect dynamicDataSourceAspect() {
        return new DynamicDataSourceAspect();
    }

    @Bean
    public String mybatisInterceptor(SqlSessionFactory sqlSessionFactory) {
        sqlSessionFactory.getConfiguration().addInterceptor(new StatementInterceptor());
        return "interceptor";
    }
}
