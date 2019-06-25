package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.ApplicationBiz;
import com.midea.isc.admin.model.Application;
import com.midea.isc.admin.model.Module;
import com.midea.isc.admin.param.ApplicationParam;
import com.midea.isc.admin.vo.ApplicationParamVo;
import com.midea.isc.admin.vo.ApplicationVo;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.vo.BulkStruct;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value="/application")
public class ApplicationController extends BasicController<ApplicationBiz, Application, ApplicationParam> {

    @Autowired
    private DiscoveryClient discovery;

    /**
     * 新增app(因为还要insert别的表所以要用这个)
     * @param param
     * @return
     */
    @PostMapping("/insertApp")
    public Integer insertApp(@RequestBody ApplicationParamVo param){
        return baseBiz.insertApp(param);
    }

    /**
     * 获取某人有权限的app
     * @param param
     * @return
     */
    @PostMapping("/getAccessibleApp")
    public List<Application> getAccessibleApp(@RequestBody ApplicationParam param){
        return baseBiz.getAccessibleApp(param);
    }
    
    /**
     * 获取某人有权限的module
     * @param param
     * @return
     */
    @PostMapping("/getAccessibleModule")
    public HashMap<String, List<Module>> getAccessibleModule(@RequestBody ApplicationParam param){
        return baseBiz.getAccessibleModule(param);
    }

    @PostMapping("/list")
    public List<ApplicationVo> list(@RequestBody ApplicationParam param){
        return baseBiz.list(param);
    }

    @PostMapping("/listService")
    public List<String> listService(){
        List<String> names = new ArrayList<>();
        discovery.getServices().forEach(name ->{
            names.add(name);
        });

        return names;
    }

    @PostMapping("/deleteAll")
    public void deleteBatch(@RequestBody ApplicationParamVo vo)throws IscException {
        baseBiz.saveAll(vo);
    }

    @PostMapping("/save")
    public void save(@RequestBody BulkStruct<ApplicationVo> data)throws IscException {
        //baseBiz.saveAll(vo);
        int i = 1;
    }

}