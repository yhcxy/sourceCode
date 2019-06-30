package com.yehui.factory;

import com.yehui.statement.MappedStatement;

import java.util.List;

public interface SqlSession {

    <T> T getMapper(Class<T> type) throws Exception;
    <T> T selectOne(MappedStatement mappedStatement);
    <E> List<E> selectList(MappedStatement mappedStatement);
}
