package com.midea.isc.api.mapper;

import com.midea.isc.api.model.View;
import com.midea.isc.api.param.ViewParam;
import com.midea.isc.api.vo.ViewVo;
import com.midea.isc.common.mapper.BaseMapper;

public interface ViewMapper extends BaseMapper<View, ViewParam>{
    /**
     * 获取一个view带filter和layout
     * @param view
     * @return
     */
    ViewVo getOne(View view);

    /**
     * 获取default
     * @param view
     * @return
     */
    String getDefault(View view);
}

