package com.midea.isc.common.component;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import com.midea.isc.common.annotation.DS;
import com.midea.isc.common.sys.DataSourceContextHolder;
import com.midea.isc.common.sys.DataSourceKey;

import java.lang.reflect.Method;

/**
 * 自定义注解 + AOP的方式实现数据源动态切换。 Created by wangxk7 on 2018-11-22.
 */
@Aspect
@Order(1) 
public class DynamicDataSourceAspect {

	private static final Logger log = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

	@Before("@annotation(DS)")
	public void beforeSwitchDS(JoinPoint point, DS DS) {
		// 获得当前访问的class
		Class<?> className = point.getTarget().getClass();
		// 获得访问的方法名
		String methodName = point.getSignature().getName();
		// 得到方法的参数的类型
		Class[] argClass = ((MethodSignature) point.getSignature()).getParameterTypes();
		String dataSource = DataSourceContextHolder.DEFAULT_DS;
		try {
			// 得到访问的方法对象
			Method method = className.getMethod(methodName, argClass);
			// 判断是否存在@DS注解
			if (method.isAnnotationPresent(DS.class)) {
				DS annotation = method.getAnnotation(DS.class);
				// 取出注解中的数据源名
				dataSource = annotation.value();
				if (dataSource.equals(DataSourceKey.slave.name())) {
					dataSource = DataSourceContextHolder.getSlaveDB();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		// 切换数据源
		DataSourceContextHolder.setDB(dataSource);
	}

	@After("@annotation(DS)")
	public void afterSwitchDS(JoinPoint point, DS DS) {
		DataSourceContextHolder.clearDB();
	}
}
