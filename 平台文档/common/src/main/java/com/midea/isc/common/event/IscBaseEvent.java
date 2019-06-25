package com.midea.isc.common.event;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

public class IscBaseEvent<T> extends RemoteApplicationEvent {
    /**
     * 是不是循环消息
     */
    private boolean isCycle = false;

    /**
     * 是不是能响应消息
     */
    private boolean lock = true;

    /**
     * 消息体
     */
    protected T body;

    /**
     * 消息体类型
     */
    protected String bodyType;

    public IscBaseEvent() {
    }

    public IscBaseEvent(Object source){
        super(source, null,null);
    }

    public IscBaseEvent(Object source, String originService, String destinationService){
        super(source, originService, destinationService);
    }

    public void setCycle(boolean isCycle){
        this.isCycle = isCycle;
    }
    public boolean getCycle(){
        return this.isCycle;
    }

    public boolean getLock(){
        return this.lock;
    }

    public void setLock(boolean lock){
        this.lock = lock;
    }

    public void setBody(T body){
        this.body = body;
        this.bodyType = body.getClass().getName();
    }
    public T getBody(){return this.body;}

    public String getBodyType(){return this.bodyType;}
}
