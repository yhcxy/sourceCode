package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.PrivilegeMapper;
import com.midea.isc.admin.model.Privilege;
import com.midea.isc.admin.param.PrivilegeParam;
import com.midea.isc.admin.vo.PrivilegeParamVo;
import com.midea.isc.common.annotation.DS;
import com.midea.isc.common.biz.BaseBiz;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivilegeBiz extends BaseBiz<PrivilegeMapper, Privilege, PrivilegeParam> {
    /**
     * 删除app对应的Privilege
     *
     * @param app
     * @return
     */
    public int clearPrivilege(String app) {
        return mapper.clearPrivilege(app);
    }

    @DS("migration")
    public List<Privilege> selectListMigration(PrivilegeParam param) {
        return mapper.selectList(param);
    }

    public int insertByName(PrivilegeParamVo param) {
        this.setWho(param);
        int count = mapper.insertByName(param);
        return count;
    }

    /**
     * 更新Menu Role授权关系
     */
    public int updatePrivilege(PrivilegeParamVo param) {
        int result = 0;
        for (Privilege item : param.getPrivileges()) {
            if (!item.getOperation().isEmpty()) {
                item.setProfile(param.getProfile());
                super.setWhoForUpdateInfo(item);
                int count = mapper.updateGrantFlag(item);
                if (count == 0) {
                    super.setWho(item);
                    count = mapper.insert(item);
                }
                result += count;
            }
        }
        return result;
    }

}

