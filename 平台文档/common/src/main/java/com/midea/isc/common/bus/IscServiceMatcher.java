package com.midea.isc.common.bus;

import org.springframework.cloud.bus.ServiceMatcher;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;
import org.springframework.util.PathMatcher;

public class IscServiceMatcher extends ServiceMatcher {
    public IscServiceMatcher(PathMatcher matcher, String id) {
        super(matcher, id);
    }

    public boolean isFromSelfGroup(RemoteApplicationEvent event) {
        String originService = event.getOriginService();
        String serviceId = this.getServiceId();

        if("**".equals(originService)){
            return true;
        }

        originService = originService.split(":")[0];
        serviceId = serviceId.split(":")[0];
        return serviceId.equals(originService);
    }
}
