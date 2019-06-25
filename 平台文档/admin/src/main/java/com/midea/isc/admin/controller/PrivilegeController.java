package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.PrivilegeBiz;
import com.midea.isc.admin.model.Privilege;
import com.midea.isc.admin.param.PrivilegeParam;
import com.midea.isc.admin.vo.PrivilegeParamVo;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/privilege")
public class PrivilegeController extends BasicController<PrivilegeBiz, Privilege, PrivilegeParam> {

    /**
     * 更新Menu Role
     */
    @PostMapping("/updatePrivilege")
    public Integer updatePrivilege(@RequestBody PrivilegeParamVo param) {
//        baseBiz.updatePrivilege(param);
        return baseBiz.updatePrivilege(param);
    }
}