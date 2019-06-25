package com.midea.isc.auth.common.vo;

import java.util.List;

import lombok.Data;

@Data
public class ServiceAuthority {
    private boolean needAuth;
    private List<String> allowedClient;
    
    public ServiceAuthority(){
        
    }
    
    public ServiceAuthority(boolean needAuth, List<String> allowedClient) {
        this.needAuth = needAuth;
        this.allowedClient = allowedClient;
    }
}
