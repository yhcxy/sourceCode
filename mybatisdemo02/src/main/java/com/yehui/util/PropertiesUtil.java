package com.yehui.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private static    Properties properties = new Properties();
    public static Properties getProperties(String path){
        InputStream in = Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(path);
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
    public static String getProperty(String key){
        return properties.getProperty(key);
    }
}
