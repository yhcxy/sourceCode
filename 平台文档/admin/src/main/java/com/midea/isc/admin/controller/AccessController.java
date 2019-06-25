package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.AccessBiz;
import com.midea.isc.admin.biz.UserBiz;
import com.midea.isc.admin.model.Access;
import com.midea.isc.admin.param.AccessParam;
import com.midea.isc.admin.vo.AccessParamVo;
import com.midea.isc.admin.vo.UserVo;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/access")
public class AccessController extends BasicController<AccessBiz, Access, AccessParam> {

    @Autowired
    private UserBiz userBiz;

    @PostMapping("list")
    public List<UserVo> list(@RequestBody AccessParamVo param) {
        return userBiz.listAccess(param);
    }

    @PostMapping("countList")
    public int countList(@RequestBody AccessParamVo param) {
        return userBiz.countAccess(param);
    }

    @PostMapping("/updateAccess")
    public void updateAccess(@RequestBody AccessParamVo param) {
        baseBiz.deleteByUserId(param);
        baseBiz.insertByApp(param);
    }

    /*
     * @IgnoreClientToken
     * 
     * @PostMapping("/test") public void ss(MultipartFile file) throws Exception
     * { FTPUtil.upload(file); }
     */
}