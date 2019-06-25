package com.midea.isc.admin.mapper;

import com.midea.isc.admin.model.Layout;
import com.midea.isc.admin.param.LayoutParam;
import com.midea.isc.admin.vo.LayoutParamVo;
import com.midea.isc.admin.vo.LayoutVo;
import com.midea.isc.common.mapper.BaseMapper;

public interface LayoutMapper extends BaseMapper<Layout, LayoutParam>{
	LayoutVo getVo(LayoutParam param);

	int deleteVo(LayoutParamVo vo);
}

