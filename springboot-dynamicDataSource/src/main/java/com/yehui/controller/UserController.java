package com.yehui.controller;

import com.yehui.entity.TbUser;
import com.yehui.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @RequestMapping("/findUser")
    public List<TbUser> findUser(){
        return userService.findUser();
    }
}
