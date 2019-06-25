package com.midea.isc.auth.common.event;

import lombok.Data;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

import java.util.List;

/**
 * @author wangxk
 * @create 2018/11/28.
 */
@Data
public class AuthRemoteEvent extends RemoteApplicationEvent {
	private static final long serialVersionUID = -3619181791353678113L;
	
	private boolean needAuth;
	private List<String> allowedClient;

    //jackson序列化反序列化必须有无参构造函数
    public AuthRemoteEvent() {
    }

    public AuthRemoteEvent(Object source, String originService, String destinationService, List<String> allowedClient, boolean needAuth) {
        // source is the object that is publishing the event
        // originService is the unique context ID of the publisher
        super(source, originService, destinationService);
        this.allowedClient = allowedClient;
        this.needAuth = needAuth;
    }


}

