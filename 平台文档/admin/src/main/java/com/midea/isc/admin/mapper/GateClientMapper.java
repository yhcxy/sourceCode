package com.midea.isc.admin.mapper;

import java.util.List;

import com.midea.isc.admin.model.GateClient;
import com.midea.isc.admin.param.GateClientParam;
import com.midea.isc.admin.vo.GateClientParamVo;
import com.midea.isc.common.mapper.BaseMapper;

public interface GateClientMapper extends BaseMapper<GateClient, GateClientParam> {
    public int lockService(GateClientParam param);

    public List<GateClient> getAppClient(GateClientParamVo param);

    public int totalAppClient(GateClientParamVo param);
    
    public List<GateClient> getAuthClient(GateClientParam param);
    
    public List<GateClient> getAllowClient(GateClientParam param);
}
