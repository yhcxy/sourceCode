package com.yehui.statement;

import com.yehui.annotation.Select;
import com.yehui.session.Configuration;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Data
public class MappedStatement {

    private Configuration configuration;
    /**
     * sql语句
     */
    private String sqlValue;
    /**
     * sql语句类型
     */
    private Annotation sqlType;
    /**
     * 返回值
     */
    private Class<?> resultType;
    /**
     * 参数
     */
    private Object[] args;

    public MappedStatement(Method method, Object[] args) {
        Annotation annotation = method.getAnnotation(Select.class);
        this.sqlType = annotation;
        this.args = args;
        this.resultType = method.getReturnType();
        this.setSqlValue();
    }
    public void setSqlValue(){
        if( this.sqlType instanceof Select){
            Select select = (Select)this.sqlType;
            this.sqlValue = select.value();
        }
    }
    public String getSqlValue() {
        return sqlValue;
    }

    public void setSqlValue(String sqlValue) {
        this.sqlValue = sqlValue;
    }

    public Annotation getSqlType() {
        return sqlType;
    }

    public void setSqlType(Annotation sqlType) {
        this.sqlType = sqlType;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
    public String getMappedStatementKey(){
        return sqlValue+args==null?"":arrtoString()+resultType;
    }
    public  String arrtoString(){
        String str = "";
        for (int i = 0; i < args.length; i++) {
            str+= args[i]+"";
        }
        return str;
    }
}
