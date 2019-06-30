package com.yehui.factory;


import com.yehui.executor.BaseExecutor;
import com.yehui.session.Configuration;
import com.yehui.statement.MappedStatement;

import java.util.List;

public class DefaultSqlSession implements SqlSession {
    private Configuration configuration;
    BaseExecutor baseExecutor;

    public  DefaultSqlSession(Configuration configuration,BaseExecutor baseExecutor){
        this.configuration = configuration;
        this.baseExecutor = baseExecutor;
    }

    @Override
    public <T> T getMapper(Class<T> type) throws Exception {
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) this.configuration.getMapper(type);
        if (mapperProxyFactory == null) {
            throw new Exception("Type " + type + " is not known to the MapperRegistry.");
        }
        return (T)mapperProxyFactory.newInstance(this);
    }

    @Override
    public <T> T selectOne(MappedStatement mappedStatement) {
        List<T> list = this.<T>selectList(mappedStatement);
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new RuntimeException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public <E> List<E> selectList(MappedStatement mappedStatement) {
        mappedStatement.setConfiguration(configuration);
        return baseExecutor.query(mappedStatement);
    }
}
