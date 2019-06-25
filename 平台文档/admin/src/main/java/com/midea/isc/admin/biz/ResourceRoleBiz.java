package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.ResourceRoleMapper;
import com.midea.isc.admin.model.ResourceRole;
import com.midea.isc.admin.param.ResourceRoleParam;
import com.midea.isc.admin.vo.MenuParamVo;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.util.BaseContextHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceRoleBiz extends BaseBiz<ResourceRoleMapper,ResourceRole,ResourceRoleParam> {


    public void batchDelete(MenuParamVo param){
        mapper.batchDelete(param);
    }

    public void clear(MenuParamVo param){
        mapper.clear(param);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updatePrivilege(MenuParamVo param){
//        List<Integer> resourceIds = param.getResourceRoles();
//
//        if(null == resourceIds || 0 == resourceIds.size()){
//            resourceRoleBiz.clear(param);
//            resourceIds = mapper.listIds(param.getAll());
//        }else{
//            resourceRoleBiz.batchDelete(param);
//        }
//
//        List<ResourceRoleParam> adds = new ArrayList<>();
//
//        for(Integer resourceId : resourceIds){
//            ResourceRoleParam rr = new ResourceRoleParam();
//            rr.setProfile(param.getProfile());
//            rr.setGrantFlag("Y");
//            rr.setApplication(param.getApplication());
//            rr.setResourceId(resourceId);
//            rr.setRoleId(param.getRoleId());
//            adds.add(rr);
//        }
//
//        if(adds.size() > 0)
//            resourceRoleBiz.batchInsert(adds);
        List<ResourceRole> vos = param.getResourceRoles();
        List<ResourceRole> adds = new ArrayList<>();
        List<ResourceRole> updates = new ArrayList<>();

        for(ResourceRole vo : vos){
            vo.setProfile(BaseContextHandler.getProfile());
            vo.setRoleId(param.getRoleId());
            vo.setApplication(param.getApplication());
            if(null == vo.getResourceRoleId()){
                super.setWho(vo);
                adds.add(vo);
            }else {
                super.setWhoForUpdateInfo(vo);
                updates.add(vo);
            }
        }

        if(!updates.isEmpty())
            mapper.updateBulk(updates);
        if(!adds.isEmpty())
            mapper.insertBulk(adds);
    }

}

