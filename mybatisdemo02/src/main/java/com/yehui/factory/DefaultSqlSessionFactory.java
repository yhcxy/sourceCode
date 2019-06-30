package com.yehui.factory;

import com.yehui.executor.SimpleExecutor;
import com.yehui.session.Configuration;

public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        // 只用默认的sqlSqlSession
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        return new DefaultSqlSession(configuration,simpleExecutor);
    }
}
