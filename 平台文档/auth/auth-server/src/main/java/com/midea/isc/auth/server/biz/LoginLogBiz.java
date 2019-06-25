package com.midea.isc.auth.server.biz;

import org.springframework.stereotype.Service;
import com.midea.isc.auth.server.model.LoginLog;
import com.midea.isc.auth.server.param.LoginLogParam;
import com.midea.isc.auth.server.mapper.LoginLogMapper;
import com.midea.isc.common.biz.BaseBiz;

@Service
public class LoginLogBiz extends BaseBiz<LoginLogMapper,LoginLog,LoginLogParam> {
	
}

