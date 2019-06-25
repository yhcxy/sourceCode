package com.midea.isc.api.vo;

import com.midea.isc.api.model.LovValue;
import lombok.Data;

@Data
public class LovValueVo extends LovValue {
    private String language;
    private String application;
}

