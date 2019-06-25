package com.midea.isc.admin.biz;

import org.springframework.stereotype.Service;
import com.midea.isc.admin.model.ServiceLog;
import com.midea.isc.admin.param.ServiceLogParam;
import com.midea.isc.admin.mapper.ServiceLogMapper;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;

@Service
public class ServiceLogBiz extends BaseBiz<ServiceLogMapper,ServiceLog,ServiceLogParam> {
	
}

