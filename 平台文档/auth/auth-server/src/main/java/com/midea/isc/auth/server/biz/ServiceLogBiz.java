package com.midea.isc.auth.server.biz;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.midea.isc.auth.server.model.ServiceLog;
import com.midea.isc.auth.server.param.ServiceLogParam;
import com.midea.isc.auth.common.vo.ClientLog;
import com.midea.isc.auth.server.mapper.ServiceLogMapper;
import com.midea.isc.common.biz.BaseBiz;

@Service
public class ServiceLogBiz extends BaseBiz<ServiceLogMapper, ServiceLog, ServiceLogParam> {
	public void serviceLog(ClientLog clientLog) {
		ServiceLogParam logParam = new ServiceLogParam();
		BeanUtils.copyProperties(clientLog, logParam);
		mapper.insert(logParam);
	}
}
