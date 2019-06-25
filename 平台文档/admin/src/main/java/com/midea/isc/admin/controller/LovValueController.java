package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.LovValueBiz;
import com.midea.isc.admin.model.LovValue;
import com.midea.isc.admin.param.LovValueParam;
import com.midea.isc.admin.vo.LovValueParamVo;
import com.midea.isc.admin.vo.LovValueVo;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/lovValue")
public class LovValueController extends BasicController<LovValueBiz, LovValue, LovValueParam> {
    @PostMapping("/enable")
    public void enable(@RequestBody LovValueParamVo vo){
        baseBiz.enable(vo);
    }

    @PostMapping("/listParent")
    public List<LovValue> listParent(@RequestBody LovValueParamVo vo){
        return baseBiz.listParent(vo);
    }

    @PostMapping("/updateValue")
    public void updateValue(@RequestBody LovValueParamVo vo){
        baseBiz.updateValue(vo);
    }

    @PostMapping("/insertValue")
    public void insertValue(@RequestBody LovValueParamVo vo){
        baseBiz.insertValue(vo);
    }
}