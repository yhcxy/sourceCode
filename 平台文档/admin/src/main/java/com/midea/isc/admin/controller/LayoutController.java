package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.LayoutBiz;
import com.midea.isc.admin.model.Layout;
import com.midea.isc.admin.param.LayoutParam;
import com.midea.isc.admin.vo.LayoutParamVo;
import com.midea.isc.admin.vo.LayoutVo;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/layout")
public class LayoutController extends BasicController<LayoutBiz, Layout, LayoutParam> {
	@PostMapping("/get")
    public LayoutVo get(@RequestBody LayoutParam param){
        param.setLoginId(param.getProfile().get__userName());
        return baseBiz.getVo(param);
    }

    @PostMapping("/save")
    public void save(@RequestBody LayoutParamVo vo){
       vo.setLoginId(vo.getProfile().get__userName());
       baseBiz.save(vo);
    }
}