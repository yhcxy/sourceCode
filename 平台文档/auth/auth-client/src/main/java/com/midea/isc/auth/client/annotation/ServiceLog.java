package com.midea.isc.auth.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务调用日志(IgnoreClientTokens时不起作用，获取不到client信息) Created by wangxk7 on
 * 2018/12/28.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
public @interface ServiceLog {
	/**
	 * 描述
	 *
	 * @return
	 */
	String des() default "";
}
