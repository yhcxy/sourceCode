package com.midea.isc.api.controller;

import com.midea.isc.api.vo.ViewVo;
import com.midea.isc.common.sys.IscException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.midea.isc.common.web.contoller.BasicController;
import com.midea.isc.api.biz.ViewBiz;
import com.midea.isc.api.model.View;
import com.midea.isc.api.param.ViewParam;

@RestController
@RequestMapping(value="/view")
public class ViewController extends BasicController<ViewBiz, View, ViewParam> {

    /**
     * 获取一个view带filter和layout
     * @param view
     * @return
     */
    @PostMapping("/getOne")
    public ViewVo getOne(@RequestBody View view){
        return baseBiz.getOne(view);
    }

    /**
     * 新增一个view
     * @param view
     * @return
     */
    @PostMapping("/insertOne")
    public void insertOne(@RequestBody ViewVo view) throws IscException {
        baseBiz.insertOne(view);
    }

    /**
     * update一个view
     * @param view
     * @return
     */
    @PostMapping("/updateOne")
    public void updateOne(@RequestBody ViewVo view) throws IscException {
        baseBiz.updateOne(view);
    }
}