package com.midea.isc.auth.common.vo;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {
	private static final long serialVersionUID = 1344168842608578419L;
	
	private final String token;

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
