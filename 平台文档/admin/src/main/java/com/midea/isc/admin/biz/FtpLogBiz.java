package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.FtpLogMapper;
import com.midea.isc.admin.model.FtpLog;
import com.midea.isc.admin.param.ApplicationParam;
import com.midea.isc.admin.param.FtpLogParam;
import com.midea.isc.admin.vo.FtpLogParamVo;
import com.midea.isc.admin.vo.FtpLogVo;
import com.midea.isc.common.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FtpLogBiz extends BaseBiz<FtpLogMapper,FtpLog,FtpLogParam> {
    @Autowired
    private ApplicationBiz applicationBiz;

   public List<FtpLogVo> list(FtpLogParamVo param){
       ApplicationParam p = new ApplicationParam();
       p.setProfile(param.getProfile());
       param.setApps(applicationBiz.getAccessibleAppString(p));

       return mapper.list(param);
    }

    public int countNew(FtpLogParamVo param){
        ApplicationParam p = new ApplicationParam();
        p.setProfile(param.getProfile());
        param.setApps(applicationBiz.getAccessibleAppString(p));
       return mapper.countNew(param);
    }
}

