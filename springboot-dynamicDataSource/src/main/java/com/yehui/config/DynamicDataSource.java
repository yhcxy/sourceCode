package com.yehui.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author yehui
 * 多数据源的选择
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final Logger log = LoggerFactory.getLogger(DynamicDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        log.info("Current DataSource is " + DynamicDataSourceContextHolder.getDB());
        return DynamicDataSourceContextHolder.getDB();
    }
}
