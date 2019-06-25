package com.midea.isc.admin.mapper;

import com.midea.isc.admin.model.FtpLog;
import com.midea.isc.admin.param.FtpLogParam;
import com.midea.isc.admin.vo.FtpLogParamVo;
import com.midea.isc.admin.vo.FtpLogVo;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface FtpLogMapper extends BaseMapper<FtpLog, FtpLogParam>{
	List<FtpLogVo> list(FtpLogParamVo param);

    int countNew(FtpLogParamVo param);
}

