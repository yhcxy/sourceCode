package com.midea.isc.auth.server.biz;

import org.springframework.stereotype.Service;
import com.midea.isc.auth.server.model.ClientService;
import com.midea.isc.auth.server.param.ClientServiceParam;
import com.midea.isc.auth.server.mapper.ClientServiceMapper;
import com.midea.isc.common.biz.BaseBiz;

@Service
public class ClientServiceBiz extends BaseBiz<ClientServiceMapper, ClientService, ClientServiceParam> {

}
