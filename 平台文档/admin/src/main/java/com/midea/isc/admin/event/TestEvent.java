package com.midea.isc.admin.event;

import org.springframework.cloud.bus.event.RefreshRemoteApplicationEvent;

public class TestEvent extends RefreshRemoteApplicationEvent {
    public TestEvent(Object source, String originService, String destinationService) {
        super(source, originService, destinationService);
    }
}
