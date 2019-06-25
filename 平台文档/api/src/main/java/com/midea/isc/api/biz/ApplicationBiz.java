package com.midea.isc.api.biz;

import com.midea.isc.api.mapper.ApplicationMapper;
import com.midea.isc.api.model.Application;
import com.midea.isc.api.param.ApplicationParam;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.model.Profile;
import com.midea.isc.common.sys.IscException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationBiz extends BaseBiz<ApplicationMapper, Application, ApplicationParam> {
    @Autowired
    private UserBiz userBiz;

    /**
     * 获取某人是否有权限
     * @return
     */
    public boolean hasPermission(String application, Profile profile) throws IscException {
        int userId = userBiz.getld(profile.get__userName());

        List<Application> apps =  mapper.getAccessibleApp(userId);

        if(apps.size() == 0){
            throw new IscException("ISC-04442");
        }
        return true;
    }

    /**
     * 获取某人是否有权限
     * @return
     */
    public List<String> getAccessibleApp(String userName) throws IscException {
        int userId = userBiz.getld(userName);

        List<Application> apps =  mapper.getAccessibleApp(userId);
        List<String> ret = new ArrayList<>();

        for (Application app : apps){
            ret.add(app.getApplication());
        }

        return ret;
    }
}

