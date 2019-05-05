package com.yehui.service.impl;

import com.yehui.annontaion.DBSource;
import com.yehui.entity.TbUser;
import com.yehui.mapper.UserDataSourceMapper;
import com.yehui.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDataSourceMapper userMapper;

    @Override
    @DBSource("slave")//使用数据源打上注解即可
    public List<TbUser> findUser() {
        return userMapper.findUser();
    }
}
