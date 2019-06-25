package com.midea.isc.auth.server.mapper;

import java.util.List;

import com.midea.isc.auth.server.model.GateClient;
import com.midea.isc.auth.server.param.GateClientParam;
import com.midea.isc.common.mapper.BaseMapper;

public interface GateClientMapper extends BaseMapper<GateClient, GateClientParam> {
	public List<String> selectAllowedClient(String serviceId);
    public int lockService(GateClientParam param);
}
