package com.midea.isc.admin.mapper;

import com.midea.isc.admin.model.Application;
import com.midea.isc.admin.param.ApplicationParam;
import com.midea.isc.admin.vo.ApplicationVo;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface ApplicationMapper extends BaseMapper<Application, ApplicationParam>{
    /**
     * 获取某人有权限的app
     * @param param
     * @return
     */
    List<Application> getAccessibleApp(ApplicationParam param);

    List<ApplicationVo> list(ApplicationParam param);

    void updateBatch(List<ApplicationParam> applications);

    void insertBatch(List<ApplicationParam> applications);

    void deleteBatch(List<Application> applications);
}

