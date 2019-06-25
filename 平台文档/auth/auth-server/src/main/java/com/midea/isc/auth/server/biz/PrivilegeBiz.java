package com.midea.isc.auth.server.biz;

import org.springframework.stereotype.Service;
import com.midea.isc.auth.server.model.Privilege;
import com.midea.isc.auth.server.param.PrivilegeParam;
import com.midea.isc.auth.server.mapper.PrivilegeMapper;
import com.midea.isc.common.biz.BaseBiz;

@Service
public class PrivilegeBiz extends BaseBiz<PrivilegeMapper,Privilege,PrivilegeParam> {
	
}

