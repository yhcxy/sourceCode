package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.LocaleBiz;
import com.midea.isc.admin.model.Locale;
import com.midea.isc.admin.param.LocaleParam;
import com.midea.isc.common.web.contoller.BasicController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value="/locale")
public class LocaleController extends BasicController<LocaleBiz, Locale, LocaleParam> {

    @PostMapping("/add")
    public void add(@RequestBody LocaleParam param) throws Exception {
        baseBiz.add(param);
    }

    @PostMapping("/list")
    public  List<Locale> list(@RequestBody LocaleParam param) {
        return baseBiz.selectList(param);
    }
}