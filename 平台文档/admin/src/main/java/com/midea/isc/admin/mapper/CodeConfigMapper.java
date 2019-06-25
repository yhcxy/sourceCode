package com.midea.isc.admin.mapper;

import com.midea.isc.admin.model.CodeConfig;
import com.midea.isc.admin.param.CodeConfigParam;
import com.midea.isc.admin.vo.CodeConfigParamVo;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface CodeConfigMapper extends BaseMapper<CodeConfig, CodeConfigParam>{

    public List<CodeConfig> getCodeConfig(CodeConfigParamVo param);

    public int totalCodeConfig(CodeConfigParamVo param);
	
}

