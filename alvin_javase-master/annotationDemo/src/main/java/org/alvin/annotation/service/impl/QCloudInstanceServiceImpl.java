package org.alvin.annotation.service.impl;

import org.alvin.annotation.commons.ClusterService;
import org.alvin.annotation.commons.ServiceType;
import org.alvin.annotation.service.IInstanceService;

@ClusterService(ServiceType.QCLOUD)
public class QCloudInstanceServiceImpl implements IInstanceService {

	@Override
	public void create() {
		System.out.println("qq yun create");
	}

}
