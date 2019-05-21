package org.alvin.dyn_impl_inter.utils;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

import java.util.Map;

/**
 * 动态对象创建，动态类创建的工具类
 */
public class DynBeanUtils {
	/**
	 * 动态实现接口，返回实现了接口的对象
	 *
	 * @param clazz
	 * @param callback
	 * @return
	 */
	public static Object dynImplInter(Class clazz, Callback callback) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		// 回调方法
		enhancer.setCallback(callback);
		// 创建代理对象
		return enhancer.create();
	}

	/**
	 * 动态实现接口，返回实现了接口的类
	 * @param clazz
	 * @param callback
	 * @return
	 */
	public static  Class dynImplInterClass(Class clazz, Callback callback) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		// 回调方法
		enhancer.setCallback(callback);
		// 创建代理对象
		return enhancer.createClass();
	}

	/**
	 * 动态创建对象，返回实例对象
	 * @param superClass
	 * @param propertyMap
	 * @return
	 */
	public static Object generatorObj(Class superClass, Map<String, Class> propertyMap) {
		BeanGenerator beanGenerator = new BeanGenerator();
		if (superClass != null) {
			beanGenerator.setSuperclass(superClass);
		}
		// 动态指定属性
		propertyMap.forEach(beanGenerator::addProperty);
		return beanGenerator.create();
	}

	/**
	 * 动态创建对象，返回对象的类
	 * @param superClass
	 * @param propertyMap
	 * @return
	 */
	public static Object generatorClass(Class superClass, Map<String, Class> propertyMap) {
		BeanGenerator beanGenerator = new BeanGenerator();
		if (superClass != null) {
			beanGenerator.setSuperclass(superClass);
		}
		// 动态指定属性
		propertyMap.forEach(beanGenerator::addProperty);
		return beanGenerator.createClass();
	}


}
