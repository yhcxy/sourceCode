package com.midea.isc.admin.biz;

import com.fasterxml.jackson.core.type.TypeReference;
import com.midea.isc.admin.mapper.CurrencyMapper;
import com.midea.isc.admin.model.Currency;
import com.midea.isc.admin.param.CurrencyParam;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.util.JacksonUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CurrencyBiz extends BaseBiz<CurrencyMapper, Currency, CurrencyParam> {

    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    @Value("${spring.cloud.config.profile}")
    private String envTag;

    private String key() {
        return "currency:format:" + envTag;
    }

    public int insert(Currency param) {
        setWho(param);
        int result = mapper.insert(param);
        return result;
    }

    public Currency selectOneWithCache(CurrencyParam param) {
        Currency entity = null;
        CurrencyCache cache = new CurrencyCache();
        if (StringUtils.isNotEmpty(param.getCode())) {
            if (redisTemplate.opsForHash().hasKey(key(), param.getCode())) {
                String json = (String) redisTemplate.opsForHash().get(key(), param.getCode());
                cache = JacksonUtils.json2Object(json, new TypeReference<CurrencyCache>() {
                });
                entity = new Currency();
                BeanUtils.copyProperties(cache, entity);
            }
        }
        if (entity == null) {
            entity = super.selectOne(param);
            BeanUtils.copyProperties(entity, cache);
            redisTemplate.opsForHash().put(key(), param.getCode(), JacksonUtils.obj2json(cache));
        }
        return entity;
    }

    public int update(Currency param) {
        setWhoForUpdateInfo(param);
        int result = mapper.update(param);
        if (redisTemplate.opsForHash().hasKey(key(), param.getCode())) {
            redisTemplate.opsForHash().delete(key(), param.getCode());
        }
        return result;
    }

    public int delete(Currency param) {
        CurrencyParam currencyParam = new CurrencyParam();
        currencyParam.setCurrencyId(param.getCurrencyId());
        Currency entity = this.selectOne(currencyParam);
        if (redisTemplate.opsForHash().hasKey(key(), entity.getCode())) {
            redisTemplate.opsForHash().delete(key(), entity.getCode());
        }
        return mapper.delete(param);
    }

    @Data
   static class CurrencyCache {
        private Integer currencyId;
        private String code;
        private String symbol;
        private String description;
        private Integer currencyPrecision;
        private String thousandsSeparator;
        private String decimalSeparator;
        private Integer calculatedPrecision;
        private String enabled;
    }


}

