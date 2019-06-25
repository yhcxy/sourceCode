package com.midea.isc.admin.biz;

import org.springframework.stereotype.Service;
import com.midea.isc.admin.model.ClientService;
import com.midea.isc.admin.param.ClientServiceParam;
import com.midea.isc.admin.mapper.ClientServiceMapper;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;

@Service
public class ClientServiceBiz extends BaseBiz<ClientServiceMapper,ClientService,ClientServiceParam> {
	
}

