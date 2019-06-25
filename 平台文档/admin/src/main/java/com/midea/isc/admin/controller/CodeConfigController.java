package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.CodeConfigBiz;
import com.midea.isc.admin.model.CodeConfig;
import com.midea.isc.admin.param.CodeConfigParam;
import com.midea.isc.admin.vo.CodeConfigParamVo;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/codeConfig")
public class CodeConfigController extends BasicController<CodeConfigBiz, CodeConfig, CodeConfigParam> {

    @RequestMapping(value = "/getCodeConfig", method = RequestMethod.POST)
    public List<CodeConfig> getCodeConfig(@RequestBody CodeConfigParamVo param) {
        return baseBiz.getCodeConfig(param);
    }

    @RequestMapping(value = "/totalCodeConfig", method = RequestMethod.POST)
    public int totalCodeConfig(@RequestBody CodeConfigParamVo param) {
        return baseBiz.totalCodeConfig(param);
    }

    @RequestMapping(value = "/saveCodeConfig", method = RequestMethod.POST)
    public int saveCodeConfig(@RequestBody CodeConfig param) {
        return baseBiz.saveCodeConfig(param);
    }


}