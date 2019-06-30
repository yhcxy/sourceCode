package com.yehui.factory;

import com.yehui.annotation.Select;
import com.yehui.statement.MappedStatement;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MapperProxy <T> implements InvocationHandler {
    private final SqlSession sqlSession;
    private final Class<T> mapperInterface;
    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Annotation annotation = method.getAnnotation(Select.class);
        if(annotation==null){
            throw new RuntimeException("没有需要执行注解");
        }
        MappedStatement mappedStatement = new MappedStatement(method,args);
        return this.sqlSession.selectOne(mappedStatement);
    }
}
