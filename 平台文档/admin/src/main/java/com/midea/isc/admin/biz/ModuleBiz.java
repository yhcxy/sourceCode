package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.ApplicationMapper;
import com.midea.isc.admin.model.Application;
import com.midea.isc.admin.vo.ApplicationParamVo;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.midea.isc.common.annotation.DS;
import com.midea.isc.admin.model.Module;
import com.midea.isc.admin.param.ModuleParam;
import com.midea.isc.admin.mapper.ModuleMapper;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;

import java.util.List;

@Service
public class ModuleBiz extends BaseBiz<ModuleMapper, Module, ModuleParam> {

    @Autowired
    private ApplicationMapper applicationMapper;

    @DS("master")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(Module model) {
        this.setWho(model);
        int count = mapper.insert(model);
        if (count > 0) {
            mapper.insertTL(model);
        }

        return count;
    }

    @DS("master")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Module model) {
        this.setWhoForUpdateInfo(model);
        int count = mapper.update(model);
        int countTL = mapper.updateTL(model);
        if (countTL == 0) {
            mapper.copySourceLang(model);
            countTL = mapper.updateTL(model);
        }

        return count;
    }

    @DS("master")
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int updateFields(Module model) {
        this.setWhoForUpdateInfo(model);
        int count = mapper.updateFields(model);
        int countTL = mapper.updateFieldsTL(model);
        if (countTL == 0) {
            mapper.copySourceLang(model);
            countTL = mapper.updateFieldsTL(model);
        }

        return count;
    }

    @DS("master")
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int updateByOtsLock(Module model) throws IscException {
        this.setWhoForUpdateInfo(model);
        int count = mapper.updateByOtsLock(model);
        if (count == 0) {
            throw new IscException("ISC-929");
        }
        int countTL = mapper.updateTL(model);
        if (countTL == 0) {
            mapper.copySourceLang(model);
            countTL = mapper.updateTL(model);
        }

        return count;
    }

    @DS("master")
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int updateFieldsByOtsLock(Module model) throws IscException {
        this.setWhoForUpdateInfo(model);
        int count = mapper.updateFieldsByOtsLock(model);
        if (count == 0) {
            throw new IscException("ISC-929");
        }
        int countTL = mapper.updateFieldsTL(model);
        if (countTL == 0) {
            mapper.copySourceLang(model);
            countTL = mapper.updateFieldsTL(model);
        }

        return count;
    }

    public List<Module> listUserModule(ModuleParam param) {
        ApplicationParamVo p = new ApplicationParamVo();
        p.setProfile(p.getProfile());
        List<Application> apps = applicationMapper.getAccessibleApp(p);

        if (null != apps && apps.size() > 0) {
            return mapper.listUserModule(apps);
        }

        return Lists.newArrayList();
    }
}