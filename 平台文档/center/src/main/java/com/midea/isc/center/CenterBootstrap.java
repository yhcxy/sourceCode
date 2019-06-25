package com.midea.isc.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 注册中心
 *
 * @author wangxk7
 * @create 2018-11-19 17:00
 */
@EnableEurekaServer  //启动一个服务注册中心提供给其他应用进行对话
@SpringBootApplication
public class CenterBootstrap 
{
    public static void main( String[] args )
    {
    	 SpringApplication.run(CenterBootstrap.class, args);
    }
}
