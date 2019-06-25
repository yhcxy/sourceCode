package com.midea.isc.admin.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.midea.isc.common.web.contoller.BasicController;
import com.midea.isc.admin.biz.CodeConfigcacheBiz;
import com.midea.isc.admin.model.CodeConfigcache;
import com.midea.isc.admin.param.CodeConfigcacheParam;

@RestController
@RequestMapping(value="/codeConfigcache")
public class CodeConfigcacheController extends BasicController<CodeConfigcacheBiz, CodeConfigcache, CodeConfigcacheParam> {
	
}