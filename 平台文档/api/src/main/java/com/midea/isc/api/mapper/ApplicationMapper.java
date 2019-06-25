package com.midea.isc.api.mapper;

import com.midea.isc.api.model.Application;
import com.midea.isc.api.param.ApplicationParam;
import com.midea.isc.api.param.LovTypeParam;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface ApplicationMapper extends BaseMapper<Application, ApplicationParam>{
    /**
     * 获取某人有权限的app
     * @param param
     * @return
     */
    List<Application> getAccessibleApp(Integer userId);
}

