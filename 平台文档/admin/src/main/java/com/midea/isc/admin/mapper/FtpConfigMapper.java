package com.midea.isc.admin.mapper;

import com.midea.isc.admin.model.FtpConfig;
import com.midea.isc.admin.param.FtpConfigParam;
import com.midea.isc.admin.vo.FtpConfigParamVo;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface FtpConfigMapper extends BaseMapper<FtpConfig, FtpConfigParam>{
    /**
     * 启用/禁用
     * @param param
     */
    void enable(FtpConfigParam param);

    /**
     * 修改密码
     * @param param
     */
    void resetPwd(FtpConfigParam param);

    /**
     * 获取list
     * @param param
     */
    List<FtpConfig> list(FtpConfigParamVo param);

   int count(FtpConfigParamVo param);
}

