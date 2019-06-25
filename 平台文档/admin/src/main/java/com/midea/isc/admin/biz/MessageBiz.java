package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.MessageMapper;
import com.midea.isc.admin.model.Application;
import com.midea.isc.admin.model.Message;
import com.midea.isc.admin.param.ApplicationParam;
import com.midea.isc.admin.param.MessageParam;
import com.midea.isc.admin.vo.MessageParamVo;
import com.midea.isc.common.annotation.DS;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageBiz extends BaseBiz<MessageMapper, Message, MessageParam> {

    @Autowired
    ApplicationBiz appBiz;

    @DS("master")
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int insert(Message model) {
        this.setWho(model);
        int count = mapper.insert(model);
        if (count > 0) {
            mapper.insertTL(model);
        }

        return count;
    }

    @DS("master")
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int update(Message model) {
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
    public int updateFields(Message model) {
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
    public int updateByOtsLock(Message model) throws IscException {
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
    public int updateFieldsByOtsLock(Message model) throws IscException {
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

    public List<Message> getAppMessage(MessageParamVo param) {
        if (setMessageParam(param)) {
            return mapper.getAppMessage(param);
        }
        else {
            return new ArrayList<Message>();
        }

    }

    public int totalAppMessage(MessageParamVo param) {
        if (setMessageParam(param)) {
            return mapper.totalAppMessage(param);
        }
        else {
            return 0;
        }
    }

    private boolean setMessageParam(MessageParamVo param) {
        ApplicationParam appParam = new ApplicationParam();
        appParam.setProfile(param.getProfile());
        List<Application> appList = appBiz.getAccessibleApp(appParam);
        if (appList != null && !appList.isEmpty()) {
            List<String> apps = new ArrayList<>();
            for (Application app : appList) {
                apps.add(app.getApplication());
            }
            param.setAppList(apps);

            return true;
        }

        return false;
    }
}
