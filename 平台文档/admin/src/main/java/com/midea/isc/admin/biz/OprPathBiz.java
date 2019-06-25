package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.OprPathMapper;
import com.midea.isc.admin.model.OprPath;
import com.midea.isc.admin.param.OprPathParam;
import com.midea.isc.common.annotation.DS;
import com.midea.isc.common.biz.BaseBiz;
import org.springframework.stereotype.Service;

@Service
public class OprPathBiz extends BaseBiz<OprPathMapper,OprPath,OprPathParam> {
    /**
     * 删除app对应的OprPath
     * @param app
     * @return
     */
    public int clearOprPath(String app){
        return mapper.clearOprPath(app);
    }

    @DS("migration")
    public int deleteMigration(OprPathParam pathParam){
        return mapper.delete(pathParam);
    }
}

