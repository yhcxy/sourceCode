package com.midea.isc.api.vo;

import com.midea.isc.api.param.LovValueParam;
import lombok.Data;

import java.util.List;

@Data
public class LovValueParamVo extends LovValueParam {
    private List<Long> ids;
    private String language;
    private String application;
    private String parentLabel;

    private List<LovValueParamVo> children;
    private List<LovValueParamVo> tls;
}

