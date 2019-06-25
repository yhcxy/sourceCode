package com.midea.isc.admin.mapper;

import com.midea.isc.admin.model.LovValue;
import com.midea.isc.admin.param.LovValueParam;
import com.midea.isc.admin.vo.LovTypeParamVo;
import com.midea.isc.admin.vo.LovValueParamVo;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface LovValueMapper extends BaseMapper<LovValue, LovValueParam> {

    // 多语言操作

    int insertTL(LovValue model);

    int updateTL(LovValue model);

    public int updateFieldsTL(LovValue model);

    int copySourceLang(LovValue model);

    void enable(LovValueParamVo vo);

    List<LovValue> listParent(LovValueParamVo vo);

    void updateValue(LovValueParamVo vo);

    void updateTypeCode(LovTypeParamVo vo);

    void insertTL4Synchronize(List<LovValueParamVo> list);

    /**
     * 带排序
     * @param vo
     * @return
     */
    List<LovValue> selectByApp(LovValueParamVo vo);
}
