package com.midea.isc.admin.controller;

import com.midea.isc.admin.vo.FtpTaskParamVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.midea.isc.common.web.contoller.BasicController;
import com.midea.isc.admin.biz.FtpTaskBiz;
import com.midea.isc.admin.model.FtpTask;
import com.midea.isc.admin.param.FtpTaskParam;

import java.util.List;

@RestController
@RequestMapping(value="/ftpTask")
public class FtpTaskController extends BasicController<FtpTaskBiz, FtpTask, FtpTaskParam> {
    @PostMapping("/insertOne")
    public void insertOne(@RequestBody FtpTaskParam param) throws Exception {
        baseBiz.insertOne(param);
    }

    @PostMapping("/updateOne")
    public void updateOne(@RequestBody FtpTaskParam param)throws Exception{
        baseBiz.updateOne(param);
    }

    @PostMapping("/enable")
    public void enable(@RequestBody FtpTaskParam param)throws Exception{
        baseBiz.enable(param);
    }

    @PostMapping("/list")
    public List<FtpTask> list(@RequestBody FtpTaskParamVo param)throws Exception{
        return baseBiz.list(param);
    }

    @PostMapping("/countNew")
    public int count(@RequestBody FtpTaskParamVo param)throws Exception{
        return baseBiz.count(param);
    }
}