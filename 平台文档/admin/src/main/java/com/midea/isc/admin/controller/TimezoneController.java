package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.TimezoneBiz;
import com.midea.isc.admin.model.Timezone;
import com.midea.isc.admin.param.TimezoneParam;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/timezone")
public class TimezoneController extends BasicController<TimezoneBiz, Timezone, TimezoneParam> {

    @RequestMapping("validList")
    public List<Timezone> validList() {
        return baseBiz.validList();
    }
}