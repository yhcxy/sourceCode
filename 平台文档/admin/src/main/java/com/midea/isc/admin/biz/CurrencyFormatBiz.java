package com.midea.isc.admin.biz;

import com.midea.isc.admin.model.Currency;
import com.midea.isc.admin.param.CurrencyParam;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.midea.isc.admin.model.CurrencyFormat;
import com.midea.isc.admin.param.CurrencyFormatParam;
import com.midea.isc.admin.mapper.CurrencyFormatMapper;
import com.midea.isc.common.biz.BaseBiz;

@Service
public class CurrencyFormatBiz extends BaseBiz<CurrencyFormatMapper,CurrencyFormat,CurrencyFormatParam> {
    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Autowired
    CurrencyBiz currencyBiz;

    @Value("${spring.cloud.config.profile}")
    private String envTag;

    private String key() {
        return "currency:country:format:" + envTag;
    }


    private String field(CurrencyFormat format){
        return format.getCurrencyCode() +"_" + format.getTerritoryCode();
    }

    public int insert(CurrencyFormat param) {
        CurrencyParam currencyParam = new CurrencyParam();
        currencyParam.setCode(param.getCurrencyCode());
        setWho(param);
        Currency currency =  currencyBiz.selectOneWithCache(currencyParam);
        param.setCurrencySymbol(currency.getSymbol());
        int result = mapper.insert(param);
        return result;
    }

    public int update(CurrencyFormat param) {
        setWhoForUpdateInfo(param);
        int result = mapper.update(param);
        if (redisTemplate.opsForHash().hasKey(key(), field(param))) {
            redisTemplate.opsForHash().delete(key(), field(param));
        }
        return result;
    }

    public int delete(CurrencyFormat param) {
        CurrencyFormatParam currencyParam = new CurrencyFormatParam();
        currencyParam.setFormatId(param.getFormatId());
        CurrencyFormat entity = this.selectOne(currencyParam);
        if (redisTemplate.opsForHash().hasKey(key(), field(entity))) {
            redisTemplate.opsForHash().delete(key(), field(entity));
        }
        return mapper.delete(param);
    }

}

