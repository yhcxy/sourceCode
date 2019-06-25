package com.midea.isc.admin.controller;


import com.midea.isc.admin.biz.AssigmentBiz;
import com.midea.isc.admin.biz.RoleBiz;
import com.midea.isc.admin.biz.UserBiz;
import com.midea.isc.admin.model.Assigment;
import com.midea.isc.admin.param.AssigmentParam;
import com.midea.isc.admin.vo.*;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/assigment")
public class AssigmentController extends BasicController<AssigmentBiz, Assigment, AssigmentParam> {

    @Autowired
    private RoleBiz roleBiz;
    @Autowired
    private UserBiz userBiz;
    @PostMapping("/list")
    public List<AssigmentVO> selectAssigmentList(@RequestBody AssigmentParamVO param){
        return this.baseBiz.selectAssigmentList(param);
    }

    @PostMapping("/deleteBatchs")
    public Integer deleteBatchs(@RequestBody AssigmentParamVO assigmentVO){
        return this.baseBiz.deleteBatchs(assigmentVO);
    }

    @PostMapping("/roleListAndUserList")
    public Map<String, Object> selectRoleListAndUserList(@RequestBody AssigmentParamVO param) {
        RoleParamVo roleVo = new RoleParamVo();
        UserParamVo userVo = new UserParamVo();
        if (StringUtils.isEmpty(param.getApplication())) {
            roleVo.setApplication(param.getProfile().get__app());
            userVo.setApplication(param.getProfile().get__app());
        } else {
            roleVo.setApplication(param.getApplication());
            userVo.setApplication(param.getApplication());
        }
        List<Map<String, Object>> roleList = roleBiz.selectRoleList(roleVo);
        List<UserVo> userList = userBiz.selectUserList(userVo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("roleList", roleList);
        map.put("userList", userList);
        return map;
    }
}