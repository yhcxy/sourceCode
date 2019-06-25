package com.midea.isc.admin.mapper;

import com.midea.isc.admin.model.LovType;
import com.midea.isc.admin.param.LovTypeParam;
import com.midea.isc.admin.vo.LovTypeParamVo;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface LovTypeMapper extends BaseMapper<LovType, LovTypeParam>{
	void enable(LovTypeParamVo vo);

	List<LovType> list4Refresh(LovTypeParamVo vo);
}

