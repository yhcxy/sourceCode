package com.midea.isc.attachment.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.midea.isc.common.sys.DataSourceContextHolder;
import com.midea.isc.common.sys.DataSourceKey;
import com.midea.isc.common.sys.DynamicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = DataSourceConfig.PACKAGE, sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {
	private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

	// master dao 所在的包
	public static final String PACKAGE = "com.midea.isc.attachment.mapper";

	// mapper所在目录
	private static final String MAPPER_LOCATION = "classpath:mapper/attachment/*.xml";

	/**
	 * master DataSource
	 * 
	 * @ConfigurationProperties 注解用于从 application.properties 文件中读取配置，为 Bean 设置属性
	 * @return data source
	 */
	@Bean("master")
	@ConfigurationProperties(prefix = "spring.datasource.druid.master")
	public DataSource master() {
		return DruidDataSourceBuilder.create().build();
	}

	/**
	 * migration DataSource
	 *
	 * @return the data source
	 */
	@Bean("migration")
	@ConfigurationProperties(prefix = "spring.datasource.druid.migration")
	public DataSource migration() {
		try {
			return DruidDataSourceBuilder.create().build();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return master();
		}
	}

	/**
	 * slave DataSource(多个slave库可以复制后修改)
	 *
	 * @return the data source
	 */
	@Bean("slave")
	@ConfigurationProperties(prefix = "spring.datasource.druid.slave")
	public DataSource slave() {
		try {
			return DruidDataSourceBuilder.create().build();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return master();
		}
	}

	/**
	 * Dynamic data source.
	 *
	 * @return the data source
	 */
	@Primary
	@Bean("dynamicDataSource")
	public DataSource dynamicDataSource() {
		DynamicDataSource dynamicRoutingDataSource = new DynamicDataSource();
		Map<Object, Object> dataSourceMap = new HashMap();
		dataSourceMap.put(DataSourceKey.master.name(), master());
		dataSourceMap.put(DataSourceKey.migration.name(), migration());

		// 多个slave数据源在此添加，自定义key，用于轮询
		dataSourceMap.put(DataSourceKey.slave.name() + "1", slave());

		// 将 master 数据源作为默认指定的数据源
		dynamicRoutingDataSource.setDefaultTargetDataSource(master());
		// 将 admin 和 migration 数据源作为指定的数据源
		dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);

		// 将 Slave 数据源的 key 放在集合中，用于轮循
		DataSourceContextHolder.slaveDataSourceKeys.addAll(dataSourceMap.keySet());
		DataSourceContextHolder.slaveDataSourceKeys.remove(DataSourceKey.migration.name());

		return dynamicRoutingDataSource;
	}

	// 创建Session
	@Bean(name = "sqlSessionFactory")
	@Primary
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dynamicDataSource());
		// MapperLocations(Resource[] mapperLocations)
		Resource[] mapperLocations = new PathMatchingResourcePatternResolver()
				.getResources(DataSourceConfig.MAPPER_LOCATION);
		sqlSessionFactoryBean.setMapperLocations(mapperLocations);
		return sqlSessionFactoryBean.getObject();
	}

	// 数据源事务管理器
	@Bean
	@Primary
	public DataSourceTransactionManager masterDataSourceTransactionManager() {
		return new DataSourceTransactionManager(dynamicDataSource());
	}
}
