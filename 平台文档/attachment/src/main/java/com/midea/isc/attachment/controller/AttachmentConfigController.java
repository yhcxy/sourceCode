package com.midea.isc.attachment.controller;

import com.midea.isc.attachment.biz.AttachmentConfigBiz;
import com.midea.isc.attachment.model.AttachmentConfig;
import com.midea.isc.attachment.param.AttachmentConfigParam;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/attachmentConfig")
public class AttachmentConfigController extends BasicController<AttachmentConfigBiz, AttachmentConfig, AttachmentConfigParam> {
    /**
     * 获取有权设置的app对应的附件配置列表
     * @param param
     * @return
     */
    @PostMapping("/getAccessConfigs")
    public List<AttachmentConfig> getAccessConfigs(@RequestBody AttachmentConfigParam param){
        return baseBiz.getAccessConfigs(param);
    }
}