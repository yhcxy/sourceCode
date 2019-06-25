package com.midea.isc.api.biz;

import com.midea.isc.api.mapper.ModuleMapper;
import com.midea.isc.api.model.Module;
import com.midea.isc.api.param.ModuleParam;
import com.midea.isc.api.vo.ModuleVo;
import com.midea.isc.common.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleBiz extends BaseBiz<ModuleMapper,Module,ModuleParam> {

    @Autowired
    private UserBiz userBiz;

    public Map<String, List<String>> getAccessibleModule(String userName){
        List<ModuleVo> vos = mapper.getAccessibleModule(userBiz.getld(userName));

        Map<String, List<String>> ret = new HashMap<>();
        for(ModuleVo vo: vos){
            ret.put(vo.getApplication(), vo.getCodes());
        }

        return  ret;
    }
}

