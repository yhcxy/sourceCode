package com.midea.isc.admin.mapper;

import com.midea.isc.admin.model.LayoutDetail;
import com.midea.isc.admin.param.LayoutDetailParam;
import com.midea.isc.admin.vo.LayoutParamVo;
import com.midea.isc.common.mapper.BaseMapper;

public interface LayoutDetailMapper extends BaseMapper<LayoutDetail, LayoutDetailParam>{
    void deleteBatch(LayoutParamVo param);

    void insertBatch(LayoutParamVo vo);
}

