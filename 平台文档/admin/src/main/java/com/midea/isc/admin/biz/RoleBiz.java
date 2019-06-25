package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.RoleMapper;
import com.midea.isc.admin.model.Role;
import com.midea.isc.admin.param.RoleParam;
import com.midea.isc.admin.vo.RoleParamVo;
import com.midea.isc.common.annotation.DS;
import com.midea.isc.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class RoleBiz extends BaseBiz<RoleMapper,Role,RoleParam> {
    /**
     * 删除app对应的Role
     * @param app
     * @return
     */
    public int clearRole(String app) {
        return mapper.clearRole(app);
    }

    public int batchInsert(List<Role> roles) {
        return mapper.batchInsert(roles);
    }

    @DS("slave")
    public String selectRoleCloneCount(String app){
        return mapper.selectRoleCloneCount(app);
    }

    public List<Map<String, Object>> selectRoleList(RoleParam param) {
        if (StringUtils.isEmpty(param.getApplication())) {
            param.setApplication(param.getProfile().get__app());
        }
        return mapper.selectRoleList(param);
    }

    public Integer deleteByRoleIds(RoleParamVo roleParamVo) {
        return mapper.deleteByRoleIds(roleParamVo);
    }
}

