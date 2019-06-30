package com.yehui.builder;

import com.yehui.factory.DefaultSqlSessionFactory;
import com.yehui.factory.SqlSessionFactory;
import com.yehui.session.Configuration;
import com.yehui.xml.XMLConfigBuilder;

public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(String path) {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(path);
        return build(xmlConfigBuilder.parse());
    }
    public SqlSessionFactory build(Configuration config) {
        return new DefaultSqlSessionFactory(config);
    }
}
