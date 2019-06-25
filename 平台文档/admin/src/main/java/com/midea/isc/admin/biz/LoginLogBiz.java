package com.midea.isc.admin.biz;

import org.springframework.stereotype.Service;
import com.midea.isc.admin.model.LoginLog;
import com.midea.isc.admin.param.LoginLogParam;
import com.midea.isc.admin.mapper.LoginLogMapper;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;

@Service
public class LoginLogBiz extends BaseBiz<LoginLogMapper,LoginLog,LoginLogParam> {
	
}

