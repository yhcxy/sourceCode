package com.midea.isc.api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.midea.isc.common.web.contoller.BasicController;
import com.midea.isc.api.biz.ModuleBiz;
import com.midea.isc.api.model.Module;
import com.midea.isc.api.param.ModuleParam;

@RestController
@RequestMapping(value="/module")
public class ModuleController extends BasicController<ModuleBiz, Module, ModuleParam> {
	
}