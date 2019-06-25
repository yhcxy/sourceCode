package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.ApplicationMapper;
import com.midea.isc.admin.mapper.LanguageMapper;
import com.midea.isc.admin.model.Application;
import com.midea.isc.admin.model.Language;
import com.midea.isc.admin.param.ApplicationParam;
import com.midea.isc.admin.param.LanguageParam;
import com.midea.isc.common.annotation.DS;
import com.midea.isc.common.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
public class LanguageBiz extends BaseBiz<LanguageMapper, Language, LanguageParam> {

    @Autowired
    ApplicationMapper appMapper;

    @DS("slave")
    public List<Language> list(String application) {
        return mapper.list(application);
    }

    @DS("master")
    @Transactional
    public int updateTest1(String attribute1) {
        int result = mapper.updateTest(attribute1);
        ApplicationParam appParam = new ApplicationParam();
        List<Application> appList = appMapper.find(appParam);
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return 0;
    }

    @DS("migration")
    @Transactional
    public int updateTest2(String attribute1) {
        int result = mapper.updateTest(attribute1);
        //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return 0;
    }
}
