package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.AccessMapper;
import com.midea.isc.admin.model.Access;
import com.midea.isc.admin.param.AccessParam;
import com.midea.isc.admin.vo.AccessParamVo;
import com.midea.isc.common.biz.BaseBiz;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessBiz extends BaseBiz<AccessMapper, Access, AccessParam> {
 
    public List<AccessParam> selectListAccess(AccessParamVo param) {
        return mapper.selectListAccess(param);
    }

    public Integer deleteAccessByAccessId(List<AccessParam> accessesList) {
        return mapper.deleteAccessByAccessId(accessesList);
    }

    public void deleteByUserId(AccessParamVo param) {
        mapper.deleteByUserId(param);
    }

    public void insertByApp(AccessParamVo param) {
        super.setWho(param);
        mapper.insertByApp(param);
    }

    public Integer updatePrimaryPosition(Access access) {
        this.setWhoForUpdateInfo(access);
        return mapper.updatePrimaryPosition(access);
    }
}

