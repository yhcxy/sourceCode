package com.midea.isc.common.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceContextHolder {
	private static final Logger log = LoggerFactory.getLogger(DataSourceContextHolder.class);

	/**
	 * 默认数据源
	 */
	public static final String DEFAULT_DS = "master";

	/**
	 * 用于轮循的计数器
	 */
	private static int counter = 0;

	/**
	 * The constant slaveDataSourceKeys.
	 */
	public static List<Object> slaveDataSourceKeys = new ArrayList<>();

	/**
	 * 用于在切换数据源时保证不会被其他线程修改
	 */
	private static Lock lock = new ReentrantLock();

	private static final ThreadLocal<String> contextHolder = ThreadLocal.withInitial(() -> DataSourceKey.master.name());

	// 设置数据源名
	public static void setDB(String dbType) {
		log.info("切换到{" + dbType + "}数据源");
		contextHolder.set(dbType);
	}

	// 获取数据源名
	public static String getDB() {
		return (contextHolder.get());
	}

	// 清除数据源名
	public static void clearDB() {
		contextHolder.remove();
	}

	/**
	 * 当使用只读数据源时通过轮循方式选择要使用的数据源
	 */
	public static String getSlaveDB() {
		lock.lock();
		try {
			int datasourceKeyIndex = counter % slaveDataSourceKeys.size();
			counter++;
			return String.valueOf(slaveDataSourceKeys.get(datasourceKeyIndex));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return "master";
		} finally {
			lock.unlock();
		}
	}
}
