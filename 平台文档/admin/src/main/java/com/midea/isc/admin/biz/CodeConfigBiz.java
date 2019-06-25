package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.CodeConfigMapper;
import com.midea.isc.admin.model.Application;
import com.midea.isc.admin.model.CodeConfig;
import com.midea.isc.admin.param.ApplicationParam;
import com.midea.isc.admin.param.CodeConfigParam;
import com.midea.isc.admin.vo.CodeConfigParamVo;
import com.midea.isc.common.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CodeConfigBiz extends BaseBiz<CodeConfigMapper, CodeConfig, CodeConfigParam> {

    @Autowired
    ApplicationBiz applicationBiz;

    /**
     * 查询
     *
     * @param param
     * @return
     */
    public List<CodeConfig> getCodeConfig(CodeConfigParamVo param) {
        if (setCodeConfigParam(param)) {
            return mapper.getCodeConfig(param);
        } else {
            return new ArrayList<CodeConfig>();
        }
    }

    /**
     * 获取总数
     *
     * @param param
     * @return
     */
    public int totalCodeConfig(CodeConfigParamVo param) {
        if (setCodeConfigParam(param)) {
            return mapper.totalCodeConfig(param);
        } else {
            return 0;
        }
    }

    /**
     * 保存
     *
     * @param codeConfig
     * @return
     */
    public int saveCodeConfig(CodeConfig codeConfig) {
        if (null != codeConfig) {
            if(null != codeConfig.getConfigId()){
                update(codeConfig);
            }else{
                insert(codeConfig);
            }
            return codeConfig.getConfigId();
        } else {
            return 0;
        }
    }

    private boolean setCodeConfigParam(CodeConfigParamVo param) {
        ApplicationParam appParam = new ApplicationParam();
        appParam.setProfile(param.getProfile());
        List<Application> appList = applicationBiz.getAccessibleApp(appParam);
        if (appList != null && !appList.isEmpty()) {
            List<String> apps = new ArrayList<>();
            for (Application app : appList) {
                apps.add(app.getApplication());
            }
            param.setApplicationList(apps);
            return true;
        }
        return false;
    }
}

