package com.midea.isc.api.vo;

import com.midea.isc.api.param.LovTypeParam;
import com.midea.isc.api.param.LovValueParam;
import lombok.Data;

import java.util.List;

@Data
public class LovTypeParamVo extends LovTypeParam {
    private List<Integer> ids;

    private List<LovTypeParamVo> children;

    private List<LovValueParamVo> values;

    private LovValueParam value;

    private List<String> codes;
}
