package com.midea.isc.attachment.biz;

import com.midea.isc.attachment.feign.ApiFeign;
import com.midea.isc.attachment.mapper.AttachmentConfigMapper;
import com.midea.isc.attachment.model.AttachmentConfig;
import com.midea.isc.attachment.param.AttachmentConfigParam;
import com.midea.isc.common.biz.BaseBiz;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttachmentConfigBiz extends BaseBiz<AttachmentConfigMapper,AttachmentConfig,AttachmentConfigParam> {

    @Autowired
    private ApiFeign apiFeign;

    public List<AttachmentConfig> getAccessConfigs(AttachmentConfigParam param) {
        List<AttachmentConfig> result = new ArrayList<>();
        List<String> appList = apiFeign.getAccessibleApp(param.getProfile().get__userName()).checkResult();
        if (appList != null && !appList.isEmpty()) {
            param.setApplication(StringUtils.join(appList,","));
            param.setApplicationCond("me");
            result = mapper.find(param);
        }
        return result;
    }
}

