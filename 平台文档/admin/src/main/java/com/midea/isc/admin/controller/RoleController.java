package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.RoleBiz;
import com.midea.isc.admin.model.Role;
import com.midea.isc.admin.param.RoleParam;
import com.midea.isc.admin.vo.RoleParamVo;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/role")
public class RoleController extends BasicController<RoleBiz, Role, RoleParam> {

    @PostMapping("/roleList")
    public List<Map<String,Object>> roleList(@RequestBody RoleParam param){
        return this.baseBiz.selectRoleList(param);
    }
    @PostMapping("/deleteByRoleIds")
    public Integer deleteByRoleIds(@RequestBody RoleParamVo roleParamVo){
    return this.baseBiz.deleteByRoleIds(roleParamVo);
}
}