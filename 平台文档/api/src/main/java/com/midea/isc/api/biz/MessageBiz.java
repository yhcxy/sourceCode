package com.midea.isc.api.biz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.midea.isc.common.annotation.DS;
import com.midea.isc.api.model.Message;
import com.midea.isc.api.param.MessageParam;
import com.midea.isc.api.mapper.MessageMapper;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;

@Service
public class MessageBiz extends BaseBiz<MessageMapper, Message, MessageParam> {

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

}
