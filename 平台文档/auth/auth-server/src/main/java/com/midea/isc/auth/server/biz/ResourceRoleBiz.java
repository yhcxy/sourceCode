package com.midea.isc.auth.server.biz;

import org.springframework.stereotype.Service;
import com.midea.isc.auth.server.model.ResourceRole;
import com.midea.isc.auth.server.param.ResourceRoleParam;
import com.midea.isc.auth.server.mapper.ResourceRoleMapper;
import com.midea.isc.common.biz.BaseBiz;

@Service
public class ResourceRoleBiz extends BaseBiz<ResourceRoleMapper,ResourceRole,ResourceRoleParam> {
	
}

