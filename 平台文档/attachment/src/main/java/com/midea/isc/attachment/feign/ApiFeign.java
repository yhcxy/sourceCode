package com.midea.isc.attachment.feign;

import com.midea.isc.common.model.BasicResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "isc-api", fallback = ApiFeignFailure.class)
public interface ApiFeign {
    @RequestMapping(value = "/application/getAccessibleApp", method = RequestMethod.POST)
    BasicResult<List<String>> getAccessibleApp(String userName);
}
