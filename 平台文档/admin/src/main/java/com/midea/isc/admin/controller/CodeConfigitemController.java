package com.midea.isc.admin.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.midea.isc.common.web.contoller.BasicController;
import com.midea.isc.admin.biz.CodeConfigitemBiz;
import com.midea.isc.admin.model.CodeConfigitem;
import com.midea.isc.admin.param.CodeConfigitemParam;

@RestController
@RequestMapping(value="/codeConfigitem")
public class CodeConfigitemController extends BasicController<CodeConfigitemBiz, CodeConfigitem, CodeConfigitemParam> {

}