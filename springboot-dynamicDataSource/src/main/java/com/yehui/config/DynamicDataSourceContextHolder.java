package com.yehui.config;

import com.yehui.sys.DataSourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yehui
 * 根据线程动态切换数据源
 */
@Configuration
public class DynamicDataSourceContextHolder {
    private static final Logger log = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);

    /**
     * 设置默认数据源
     */
    public static  String DEFAULT_DS = "master";
    /**
     *用于轮训计数
     */
    private static int counter = 0;
    /*
     * 当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
     * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */
    private static final ThreadLocal<String> contextHolder = ThreadLocal.withInitial(() -> DataSourceKey.master.name());

    /**
     *用于在切换数据源时保证不会被其他线程修改
     */
    public static Lock lock = new ReentrantLock();

    /**
     * 设置数据源
     */
    public static void setDB(String dbType){
        log.info("切换到{" + dbType + "}数据源");
        contextHolder.set(dbType);
    }

    /**
     * 得到数据源
     *
     */
    public static String getDB(){
        return contextHolder.get();
    }

    /**
     * 使用主数据源
     */
    public static void useMasterDataSource() {
        contextHolder.set(DataSourceKey.master.name());
    }
    /**
     * 移除数据源
     */
    public static void removeDB(){
        contextHolder.remove();
    }
    /**
     * The constant slaveDataSourceKeys.
     */
    public static List<Object> slaveDataSourceKeys = new ArrayList<>();
    /**
     * 当使用只读数据源时通过轮循方式选择要使用的数据源
     */
    public static String getSlaveDB(){
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
