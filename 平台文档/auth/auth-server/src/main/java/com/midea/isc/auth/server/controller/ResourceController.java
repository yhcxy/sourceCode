package com.midea.isc.auth.server.controller;

import com.midea.isc.auth.server.biz.ResourceBiz;
import com.midea.isc.auth.server.model.Resource;
import com.midea.isc.auth.server.param.ResourceParam;
import com.midea.isc.auth.server.vo.ResourceVo;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/resource")
public class ResourceController extends BasicController<ResourceBiz, Resource, ResourceParam> {
    @PostMapping("/listPermission")
	public List<ResourceVo> listPermission(@RequestBody ResourceVo vo){
        return baseBiz.listPermission(vo);
    }
}