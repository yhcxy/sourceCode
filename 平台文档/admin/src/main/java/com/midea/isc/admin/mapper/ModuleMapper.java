package com.midea.isc.admin.mapper;

import com.midea.isc.admin.model.Application;
import com.midea.isc.admin.model.Module;
import com.midea.isc.admin.param.ModuleParam;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module, ModuleParam> {

    // 多语言操作

    int insertTL(Module model);

    int updateTL(Module model);

    public int updateFieldsTL(Module model);

    int copySourceLang(Module model);

    List<Module> listUserModule(List<Application> apps);
}
