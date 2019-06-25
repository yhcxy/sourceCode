package com.midea.isc.auth.server.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.midea.isc.auth.server.config.FeignConfiguration;
import com.midea.isc.common.model.BasicResult;
import com.midea.isc.common.model.UserInfo;
import com.midea.isc.common.sys.IscException;

/**
 *
 * @author wangxk7
 * @create 2018-11-28
 */
@FeignClient(value = "isc-admin", configuration = FeignConfiguration.class, fallback = UserServiceFailure.class)
public interface UserService {
    // @RequestMapping(value = "/api/user/validate", method =
    // RequestMethod.POST)
    // public UserInfo validate(@RequestParam("username") String username,
    // @RequestParam("password") String password);
    //
    // @RequestMapping(value = "/api/user/ssoUser", method = RequestMethod.POST)
    // public UserInfo ssoUser(@RequestParam("username") String username);

    @RequestMapping(value = "/api/user/testHystrix", method = RequestMethod.POST)
    public BasicResult<String> testHystrix(@RequestParam("username") String username) throws IscException;
}