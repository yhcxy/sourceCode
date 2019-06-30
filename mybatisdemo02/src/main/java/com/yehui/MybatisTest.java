package com.yehui;

import com.yehui.builder.SqlSessionFactoryBuilder;
import com.yehui.factory.SqlSession;
import com.yehui.factory.SqlSessionFactory;
import com.yehui.mapper.UserMapper;

public class MybatisTest {
    public static void main(String[] args) throws Exception {
        String resource = "mybatis.properties";
        //2、得到SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resource);
        //3、获取SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //4、执行getMapper方法
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        System.out.println(mapper.getUser(1));

    }
}
