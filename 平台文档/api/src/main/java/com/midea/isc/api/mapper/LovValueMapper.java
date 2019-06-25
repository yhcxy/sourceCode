package com.midea.isc.api.mapper;

import com.midea.isc.api.model.LovValue;
import com.midea.isc.api.param.LovValueParam;
import com.midea.isc.api.vo.LovValueParamVo;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface LovValueMapper extends BaseMapper<LovValue, LovValueParam> {

    // 多语言操作

    public int insertTL(LovValue model);

    public int updateTL(LovValue model);

    public int updateFieldsTL(LovValue model);

    public int copySourceLang(LovValue model);

    void insertTL4Synchronize(List<LovValueParamVo> list);

    /**
     * 带排序
     * @param vo
     * @return
     */
    List<LovValue> selectByApp(LovValueParamVo vo);
}
