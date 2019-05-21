package org.alvin.annotation;

import org.alvin.annotation.commons.ClusterService;
import org.alvin.annotation.commons.ServiceObjectFactory;
import org.alvin.annotation.commons.ServiceType;
import org.alvin.annotation.service.IInstanceService;

/**
 * Created by tangzhichao on 2016/8/3.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        //扫描一个包下面的所有类
        ServiceObjectFactory.doScanService("org.alvin.annotation");
        //根据类型返回一个业务实体
        IInstanceService service = ServiceObjectFactory.getInstance(ServiceType.QCLOUD, IInstanceService.class);
        service.create();
    }
}
