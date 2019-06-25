package com.midea.isc.common.sys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.midea.isc.common.sys.DataSourceContextHolder;

public class DynamicDataSource extends AbstractRoutingDataSource {
	private static final Logger log = LoggerFactory.getLogger(DynamicDataSource.class);

	@Override
	protected Object determineCurrentLookupKey() {
		log.info("Current DataSource is " + DataSourceContextHolder.getDB());
		return DataSourceContextHolder.getDB();
	}
}