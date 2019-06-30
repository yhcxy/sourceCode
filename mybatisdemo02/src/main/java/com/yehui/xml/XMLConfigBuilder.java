package com.yehui.xml;

import com.yehui.builder.BaseBuilder;
import com.yehui.session.Configuration;
import com.yehui.session.DataSource;
import com.yehui.util.ClassUtil;
import com.yehui.util.PropertiesUtil;

import java.util.List;
import java.util.Properties;

/**
 * 解析配置文件properties
 */
public class XMLConfigBuilder extends BaseBuilder {

    private String path;
    public XMLConfigBuilder(String path){
        this.path = path;
        this.configuration = new Configuration();
    }

    /**
     * 解析配置文件
     */
    public Configuration parse(){
        //解析配置文件获取DataSource
        Properties properties = PropertiesUtil.getProperties(path);
        String driver = properties.getProperty("driver");
        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        String mappers = properties.getProperty("mappers");
        //String url,String driver,String username,String password
        DataSource dataSource = new DataSource(url,driver,username,password);
        this.configuration .setDataSource(dataSource);
        //解析mapper注册
        this.configuration.setMappers(mappers);
        //通过反射机制获取所有的接口并注册到configuration
        List<Class> classes = ClassUtil.doScanPackage(mappers);
        for (Class aClass : classes) {
            this.configuration.addMapper(aClass);
        }
        return this.configuration;
    }
}
