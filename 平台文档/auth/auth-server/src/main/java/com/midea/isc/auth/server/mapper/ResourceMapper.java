package com.midea.isc.auth.server.mapper;

import com.midea.isc.auth.server.model.Resource;
import com.midea.isc.auth.server.param.ResourceParam;
import com.midea.isc.auth.server.vo.ResourceVo;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface ResourceMapper extends BaseMapper<Resource, ResourceParam>{
	List<ResourceVo> listPermission(ResourceVo vo);
}

