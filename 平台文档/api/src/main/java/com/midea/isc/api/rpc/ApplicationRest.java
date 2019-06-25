package com.midea.isc.api.rpc;

import com.midea.isc.api.biz.ApplicationBiz;
import com.midea.isc.api.biz.ModuleBiz;
import com.midea.isc.common.web.rpc.BasicRest;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(value="应用API", tags={"application","application"})
@RestController
@RequestMapping("application")
public class ApplicationRest extends BasicRest {

    @Autowired
    private ApplicationBiz applicationBiz;
 
    @Autowired
    private ModuleBiz moduleBiz;

    @RequestMapping(value = "/getAccessibleApp", method = RequestMethod.POST)
    public List<String> getAccessibleApp(String userName){
        return applicationBiz.getAccessibleApp(userName);
    }

    @PostMapping("/getAccessibleModule")
    public Map<String, List<String>> getAccessibleModule(String userName){
        return moduleBiz.getAccessibleModule(userName);
    }
}
