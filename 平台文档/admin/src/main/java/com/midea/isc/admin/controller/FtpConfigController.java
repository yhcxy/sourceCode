package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.FtpConfigBiz;
import com.midea.isc.admin.model.FtpConfig;
import com.midea.isc.admin.param.FtpConfigParam;
import com.midea.isc.admin.vo.FtpConfigParamVo;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/ftpConfig")
public class FtpConfigController extends BasicController<FtpConfigBiz, FtpConfig, FtpConfigParam> {
	@PostMapping("/insertOne")
    public void insertOne(@RequestBody FtpConfigParam param) throws Exception {
        baseBiz.insertOne(param);
    }

    @PostMapping("/updateOne")
    public void updateOne(@RequestBody FtpConfigParam param)throws Exception{
        baseBiz.updateOne(param);
    }

    @PostMapping("/enable")
    public void enable(@RequestBody FtpConfigParam param)throws Exception{
        baseBiz.enable(param);
    }

    @PostMapping("/resetPwd")
    public void resetPwd(@RequestBody FtpConfigParamVo param)throws Exception{
        baseBiz.resetPassword(param);
    }

    @PostMapping("/refresh")
    public void refresh(@RequestBody FtpConfigParam param)throws Exception{
        baseBiz.refresh(param);
    }

    @PostMapping("/list")
    public List<FtpConfig> list(@RequestBody FtpConfigParamVo param)throws Exception{
        return baseBiz.list(param);
    }

    @PostMapping("/countNew")
    public int count(@RequestBody FtpConfigParamVo param)throws Exception{
       return baseBiz.count(param);
    }


}