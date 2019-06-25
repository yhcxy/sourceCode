package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.FtpLogBiz;
import com.midea.isc.admin.model.FtpLog;
import com.midea.isc.admin.param.FtpLogParam;
import com.midea.isc.admin.vo.FtpLogParamVo;
import com.midea.isc.admin.vo.FtpLogVo;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/ftpLog")
public class FtpLogController extends BasicController<FtpLogBiz, FtpLog, FtpLogParam> {
    @PostMapping("list")
    public List<FtpLogVo> list(FtpLogParamVo param){
        return baseBiz.list(param);
    }

    @PostMapping("countNew")
    public int countNew(FtpLogParamVo param){
        return baseBiz.countNew(param);
    }
}