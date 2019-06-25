package com.midea.isc.auth.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midea.isc.auth.server.biz.UserBiz;
import com.midea.isc.auth.server.model.User;
import com.midea.isc.auth.server.param.UserParam;
import com.midea.isc.common.web.contoller.BasicController;

@RestController
@RequestMapping(value = "/user")
public class UserController extends BasicController<UserBiz, User, UserParam> {
}