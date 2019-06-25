package com.midea.isc.common.interceptor;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
@Component
public class StatementInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) processTarget(invocation.getTarget());
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        // 只拦截delete方法
        if (!SqlCommandType.DELETE.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }
        String statementId = mappedStatement.getId();
        if (statementId.substring(statementId.lastIndexOf(".") + 1).equals("delete")) {
            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
            // 获取到sql
            // String originalSql = boundSql.getSql();
            // 可以对originalSql进行改写
            // log.info("拦截器StatementInterceptor:" + originalSql);
            // metaStatementHandler.setValue("delegate.boundSql.sql",
            // originalSql);
            // Object parameterObject = boundSql.getParameterObject();
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            if (parameterMappings == null || parameterMappings.isEmpty()) {
                throw new Exception("Full table deletion risk");
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 获取代理的原始对象
     *
     * @param target
     * @return
     */
    private static Object processTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject mo = SystemMetaObject.forObject(target);
            return processTarget(mo.getValue("h.target"));
        }
        return target;
    }
}
