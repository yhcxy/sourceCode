package com.midea.isc.auth.server.biz;

import org.springframework.stereotype.Service;
import com.midea.isc.auth.server.model.Role;
import com.midea.isc.auth.server.param.RoleParam;
import com.midea.isc.auth.server.mapper.RoleMapper;
import com.midea.isc.common.biz.BaseBiz;

@Service
public class RoleBiz extends BaseBiz<RoleMapper,Role,RoleParam> {
	
}

