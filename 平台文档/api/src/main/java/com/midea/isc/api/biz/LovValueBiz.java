package com.midea.isc.api.biz;

import com.midea.isc.api.mapper.LovValueMapper;
import com.midea.isc.api.model.LovValue;
import com.midea.isc.api.param.LovValueParam;
import com.midea.isc.api.vo.LovValueParamVo;
import com.midea.isc.common.annotation.DS;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LovValueBiz extends BaseBiz<LovValueMapper, LovValue, LovValueParam> {

    @DS("master")
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int insert(LovValue model) {
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
    public int update(LovValue model) {
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
    public int updateFields(LovValue model) {
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
    public int updateByOtsLock(LovValue model) throws IscException {
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
    public int updateFieldsByOtsLock(LovValue model) throws IscException {
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

    @DS("master")
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void insert4Synchronize(LovValueParamVo param) {
        this.setWho(param);
        mapper.insert(param);

        if (null != param.getTls() && param.getTls().size() > 0) {
            param.getTls().forEach(o -> {
                o.setProfile(param.getProfile());
                o.setParentLabel(param.getParentLabel());
            });

            mapper.insertTL4Synchronize(param.getTls());
        }
    }

    public List<LovValue> selectByApp(LovValueParamVo param) {
        return mapper.selectByApp(param);
    }
}
