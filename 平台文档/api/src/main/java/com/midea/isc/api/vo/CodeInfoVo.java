package com.midea.isc.api.vo;

import com.midea.isc.api.model.CodeConfig;
import com.midea.isc.api.model.CodeConfigitem;
import lombok.Data;

import java.util.List;

@Data
public class CodeInfoVo {

    private CodeConfig codeConfig;
    private List<CodeConfigitem> items;

}
