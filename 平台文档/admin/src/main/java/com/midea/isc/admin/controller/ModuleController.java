package com.midea.isc.admin.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.midea.isc.common.web.contoller.BasicController;
import com.midea.isc.admin.biz.ModuleBiz;
import com.midea.isc.admin.model.Module;
import com.midea.isc.admin.param.ModuleParam;

import java.util.List;

@RestController
@RequestMapping(value="/module")
public class ModuleController extends BasicController<ModuleBiz, Module, ModuleParam> {

    /**
     * 获取有权限的module
     */
    @PostMapping("/listUserModule")
    public List<Module> listUserModule(@RequestBody ModuleParam param){
        return baseBiz.listUserModule(param);
    }
}