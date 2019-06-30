package com.yehui.executor;

import com.yehui.statement.MappedStatement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseExecutor {

    private static Map<Object,Object> cache = new HashMap<>();
    public <E> List<E> query(MappedStatement ms){
        //1、先从1级缓存查询，已经有了一级缓存就返回默认的数据
        List<E>  list = (List<E>) cache.get(ms.getMappedStatementKey());
        //1、如果一级缓存没有数据就查询数据库
        if(list!=null){
            return list;
        }
        return this.queryFromDatabase(ms);
    }

    private <E> List<E> queryFromDatabase(MappedStatement ms) {
        List<E> list = doQuery(ms);
        return list;
    }
    protected abstract <E> List<E> doQuery(MappedStatement ms);
}
