package com.midea.isc.admin.feign;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import com.midea.isc.common.model.BasicResult;

@Component
public class LanguageFeignFailure implements LanguageFeign {
    private String serviceName = "isc-auth";

    @Override
    public BasicResult<?> setAppLanguage(@RequestParam("app") String app,
            @RequestParam("languages[]") List<String> languages) {
        return new BasicResult<>("ISC-930", serviceName); // 断路器
    }
}
