package com.midea.isc.auth.server.feign;

import org.springframework.stereotype.Component;

import com.midea.isc.common.model.BasicResult;

@Component
public class UserServiceFailure implements UserService {
    private String serviceName = "isc-admin";

    @Override
    public BasicResult<String> testHystrix(String username) {
        return new BasicResult<String>("ISC-930", serviceName); // 断路器
        // return new BasicResult<String>("ISC-000","Hello " + username + " by
        // error!"); // 自定义默认返回值
        // return "Hello " + username + " by error!";
    }
}
