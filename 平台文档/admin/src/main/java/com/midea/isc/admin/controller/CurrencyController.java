package com.midea.isc.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.midea.isc.common.web.contoller.BasicController;
import com.midea.isc.admin.biz.CurrencyBiz;
import com.midea.isc.admin.model.Currency;
import com.midea.isc.admin.param.CurrencyParam;

@RestController
@RequestMapping(value = "/currency")
public class CurrencyController extends BasicController<CurrencyBiz, Currency, CurrencyParam> {

//    @Autowired
//    CurrencyBiz currencyBiz;
//
//    @RequestMapping(value = "/insert", method = RequestMethod.POST)
//    public Integer insert(@RequestBody CurrencyParam param) {
//        return currencyBiz.insert(param);
//    }
//
//    @RequestMapping(value = "/update", method = RequestMethod.POST)
//    public Integer update(@RequestBody CurrencyParam param) {
//        return currencyBiz.update(param);
//    }
//
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    public Integer delete(@RequestBody CurrencyParam param) {
//        return currencyBiz.delete(param);
//    }

}