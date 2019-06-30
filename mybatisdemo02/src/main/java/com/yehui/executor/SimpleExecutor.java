package com.yehui.executor;

import com.yehui.session.Configuration;
import com.yehui.session.DataSource;
import com.yehui.statement.MappedStatement;
import com.yehui.util.MyBatisJDBCUtil;

import java.util.List;

public class SimpleExecutor  extends BaseExecutor {
    @Override
    protected <E> List<E> doQuery(MappedStatement ms) {
        //获取数据库连接
        Configuration configuration = ms.getConfiguration();
        DataSource dataSource = configuration.getDataSource();
        MyBatisJDBCUtil myBatisJDBCUtil = new MyBatisJDBCUtil(dataSource);
        List<E> objects = (List<E>) myBatisJDBCUtil.queryListExecute(ms.getSqlValue(), ms.getArgs(), ms.getResultType());
        return objects;
    }
}
