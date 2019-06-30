package com.yehui.session;

import com.yehui.factory.MapperProxyFactory;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置类
 */
@Data
public class Configuration {
    private DataSource dataSource;
    private String mappers;
    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<Class<?>, MapperProxyFactory<?>>();
    public <T> void addMapper(Class<T> type) {
        knownMappers.put(type,new MapperProxyFactory(type));
    }

    public <T> T getMapper(Class<T> type){
        return (T) knownMappers.get(type);
    }

}
