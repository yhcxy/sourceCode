package com.midea.isc.api.vo;

import com.midea.isc.api.model.Module;
import lombok.Data;

import java.util.List;

@Data
public class ModuleVo extends Module {
    private List<String> codes;
}
