package com.midea.isc.auth.server.controller;

//import com.midea.isc.auth.client.annotation.IgnoreClientToken;
import com.midea.isc.auth.server.biz.MenuBiz;
import com.midea.isc.auth.server.model.Menu;
import com.midea.isc.auth.server.param.MenuParam;
import com.midea.isc.auth.server.vo.MenuNode;
import com.midea.isc.auth.server.vo.MenuParamVo;
import com.midea.isc.common.web.contoller.BasicController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/menu")
public class MenuController extends BasicController<MenuBiz, Menu, MenuParam> {
    @Autowired
    private MenuBiz menuBiz;

    //@IgnoreClientToken
    @PostMapping(value = "/tree")
    public List<MenuNode> tree(@RequestBody MenuParamVo param) {
        return menuBiz.tree(param);
    }
}