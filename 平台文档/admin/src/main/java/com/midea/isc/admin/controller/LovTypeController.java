package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.LovTypeBiz;
import com.midea.isc.admin.model.LovType;
import com.midea.isc.admin.param.LovTypeParam;
import com.midea.isc.admin.vo.LovTypeParamVo;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/lovType")
public class LovTypeController extends BasicController<LovTypeBiz, LovType, LovTypeParam> {
    @PostMapping("/enable")
	public void enbale(@RequestBody LovTypeParamVo vo){
	    baseBiz.enable(vo);
    }

    @PostMapping("/updateType")
    public void updateType(@RequestBody LovTypeParamVo vo){
        baseBiz.updateType(vo);
    }

    @PostMapping("/refresh")
    public void refresh(@RequestBody LovTypeParamVo vo){
        baseBiz.refreshCache(vo);
    }
}