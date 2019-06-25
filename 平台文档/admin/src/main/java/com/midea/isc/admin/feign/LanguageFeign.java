package com.midea.isc.admin.feign;

import com.midea.isc.common.model.BasicResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "${auth.serviceId}", fallback = LanguageFeignFailure.class)
public interface LanguageFeign {
    @RequestMapping(value = "/language/setAppLanguage", method = RequestMethod.POST)
    BasicResult<?> setAppLanguage(@RequestParam("app") String app, @RequestParam("languages[]") List<String> languages);
}
