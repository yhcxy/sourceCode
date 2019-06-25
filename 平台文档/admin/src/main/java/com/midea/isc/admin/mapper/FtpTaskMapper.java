package com.midea.isc.admin.mapper;

import com.midea.isc.admin.model.FtpConfig;
import com.midea.isc.admin.model.FtpTask;
import com.midea.isc.admin.param.FtpTaskParam;
import com.midea.isc.admin.vo.FtpTaskParamVo;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface FtpTaskMapper extends BaseMapper<FtpTask, FtpTaskParam>{
    /**
     * 启用/禁用
     * @param param
     */
    void enable(FtpTaskParam param);

    /**
     * 获取list
     * @param param
     */
    List<FtpTask> list(FtpTaskParamVo param);

    int count(FtpTaskParamVo param);
}

