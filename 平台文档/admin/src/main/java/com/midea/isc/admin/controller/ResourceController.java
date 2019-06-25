package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.ResourceBiz;
import com.midea.isc.admin.biz.ResourceRoleBiz;
import com.midea.isc.admin.model.Resource;
import com.midea.isc.admin.param.ResourceParam;
import com.midea.isc.admin.vo.MenuParamVo;
import com.midea.isc.admin.vo.ResourceParamVo;
import com.midea.isc.admin.vo.ResourceVo;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/resource")
public class ResourceController extends BasicController<ResourceBiz, Resource, ResourceParam> {

    @Autowired
    private ResourceRoleBiz resourceRoleBiz;

//	@PostMapping("list")
//    public MenuNode list(@RequestBody  ResourceRoleParam param){
//	    return menuBiz.listWithResource(param);
//    }

    @PostMapping("list")
    public List<ResourceVo> list(@RequestBody ResourceParamVo param){
	    return baseBiz.listWithFlags(param);
    }

    /**
     * 更新角色权限
     * @param param
     */
    @PostMapping("updatePrivilege")
    public void updatePrivilege(@RequestBody MenuParamVo param) {
        resourceRoleBiz.updatePrivilege(param);
    }
}