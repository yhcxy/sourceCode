package org.alvin.dyn_impl_inter.services;

import net.sf.cglib.proxy.MethodInterceptor;
import org.alvin.dyn_impl_inter.utils.DynBeanUtils;
import org.alvin.mini_inject.annotations.MiniComponent;

import java.util.HashMap;
import java.util.Map;

@MiniComponent
public class DynImplContext  {

	private Map<String, Object> dynEnhancerMap = new HashMap<>();
	private Map<String, Object> synGeneratorMap = new HashMap<>();

	public void enhancer(Class clazz, String id,MethodInterceptor methodInterceptor) {
		dynEnhancerMap.put(id, DynBeanUtils.dynImplInter(clazz, methodInterceptor));
	}

	public void generator(Class superClass, Map<String, Class> propertyMap, String id) {
		synGeneratorMap.put(id, DynBeanUtils.generatorObj(superClass, propertyMap));
	}

	/**
	 * 动态接口实现的实例
	 *
	 * @param id
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public <T> T getDynImplInter(String id, Class<T> clazz) {
		return (T) dynEnhancerMap.get(id);
	}

	/**
	 * 动态创建对象的实例
	 *
	 * @param id
	 * @return
	 */
	public Object getGeneratorObj(String id) {
		return this.synGeneratorMap.get(id);
	}

//	@MiniRun
//	public void run() {
//		//测试动态实现接口
//		enhancer(TestInterFace.class, TestInterFace.class.getSimpleName(), new MethodInterceptor() {
//			@Override
//			public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//				return null;
//			}
//		});
//		TestInterFace testInterFace = getDynImplInter(TestInterFace.class.getSimpleName(), TestInterFace.class);
//		try {
//			testInterFace.test();
//		} catch (Exception ex) {
//			System.out.println(ex.getMessage());
//		}
//		//测试动态创建类
//		Map<String, Class> map = new HashMap<>();
//		map.put("name", String.class);
//		map.put("id", Integer.class);
//
//		generator(null, map, "testbean");
//
//		Object bean = getGeneratorObj("testbean");
//		Method setName = null;
//		try {
//			setName = bean.getClass().getMethod("setName", String.class);
//			Object result = setName.invoke(bean, "Sunny");
//			System.out.println("result:" + result);
//
//			Method getName = bean.getClass().getMethod("getName");
//			Object name = getName.invoke(bean);
//			System.out.println("name:" + name);
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		}
//
//	}


}
