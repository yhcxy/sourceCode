package com.midea.isc.common.event;

public class IscGroupEvent<T> extends IscBaseEvent<T> {
    public IscGroupEvent() {
    }

    public IscGroupEvent(Object source){
        super(source, null,null);
    }

    public IscGroupEvent(Object source, String originService, String destinationService){
        super(source, originService, destinationService);
    }
}
