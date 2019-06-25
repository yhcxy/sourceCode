package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.TerritoryMapper;
import com.midea.isc.admin.model.Territory;
import com.midea.isc.admin.param.TerritoryParam;
import com.midea.isc.common.annotation.DS;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.constant.DateFormatConstants;
import com.midea.isc.common.sys.IscException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class TerritoryBiz extends BaseBiz<TerritoryMapper, Territory, TerritoryParam> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @DS("master")
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int insert(Territory model) {
        this.verifyDateFormat(model);
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
    public int update(Territory model) {
        this.verifyDateFormat(model);
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
    public int updateFields(Territory model) {
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
    public int updateByOtsLock(Territory model) throws IscException {
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
    public int updateFieldsByOtsLock(Territory model) throws IscException {
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

    /**
     * 时间格式校验
     */
    private void verifyDateFormat(Territory model){
        try {
            String fmt = model.getDateFormatText() + " " + model.getTimeFormatText();
            new SimpleDateFormat(fmt).format(new Date());
        } catch (Exception e) {
            //TODO 暂时写死999，以后补上对应的消息码
            throw new IscException("ISC-999","日期时间格式有误");
        }
    }

    /**
     * 国家时间格式缓存刷新
     */
    public void refreshCache() {
        //删除旧缓存
        redisTemplate.delete(DateFormatConstants.COUNTRY_DATE_FORMAT_CACHE_KEY);
        //查询国家列表
        List<Territory> territoryList = this.selectList(null);
        for (Territory territory : territoryList) {
            redisTemplate.opsForHash().put(DateFormatConstants.COUNTRY_DATE_FORMAT_CACHE_KEY, territory.getCode(), territory.getDateFormatText() + " " + territory.getTimeFormatText());
        }
    }
}
