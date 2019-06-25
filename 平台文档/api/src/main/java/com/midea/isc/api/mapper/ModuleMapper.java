package com.midea.isc.api.mapper;

import com.midea.isc.api.model.Module;
import com.midea.isc.api.param.ApplicationParam;
import com.midea.isc.api.param.ModuleParam;
import com.midea.isc.api.vo.ModuleVo;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module, ModuleParam>{
	List<ModuleVo> getAccessibleModule(Integer userId);
}

