package com.midea.isc.attachment.feign;

import com.midea.isc.common.model.BasicResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiFeignFailure  implements ApiFeign {
    private String serviceName = "isc-api";
    
    @Override
    public BasicResult<List<String>> getAccessibleApp(String userName){
        return new BasicResult<>("ISC-930", serviceName); // 断路器
    }
}
