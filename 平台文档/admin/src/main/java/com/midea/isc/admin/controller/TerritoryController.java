package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.TerritoryBiz;
import com.midea.isc.admin.model.Territory;
import com.midea.isc.admin.param.TerritoryParam;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/territory")
public class TerritoryController extends BasicController<TerritoryBiz, Territory, TerritoryParam> {

    @RequestMapping("/refreshCache")
    public void refreshCache(){
        baseBiz.refreshCache();
    }
}