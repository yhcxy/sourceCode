package com.midea.isc.common.event;

public class IscBroadcastEvent extends IscBaseEvent {
    public IscBroadcastEvent() {
    }

    public IscBroadcastEvent(Object source){
        super(source, null,null);
    }

    public IscBroadcastEvent(Object source, String originService, String destinationService){
        super(source, originService, destinationService);
    }
}
