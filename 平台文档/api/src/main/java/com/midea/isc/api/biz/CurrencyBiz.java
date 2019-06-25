package com.midea.isc.api.biz;

import com.fasterxml.jackson.core.type.TypeReference;
import com.midea.isc.api.model.Currency;
import com.midea.isc.api.param.CurrencyParam;
import com.midea.isc.api.vo.FormatVo;
import com.midea.isc.common.util.JacksonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.midea.isc.api.mapper.CurrencyMapper;
import com.midea.isc.common.biz.BaseBiz;

@Service
@Slf4j
public class CurrencyBiz extends BaseBiz<CurrencyMapper, Currency, CurrencyParam> {

    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    @Value("${spring.cloud.config.profile}")
    private String envTag;

    private String key() {
        return "currency:country:format:" + envTag;
    }

    public FormatVo selectOneWithCache(CurrencyParam param) {
        CurrencyCache cache = new CurrencyCache();
        FormatVo vo = new FormatVo();
        if (StringUtils.isNotEmpty(param.getCode())) {
            if (redisTemplate.opsForHash().hasKey(key(), param.getCode())) {
                String json = (String) redisTemplate.opsForHash().get(key(), param.getCode());
                try {
                    cache = JacksonUtils.json2Object(json, new TypeReference<CurrencyCache>() {
                    });
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        if (cache != null && StringUtils.isEmpty(cache.getCode())) {
            Currency entity = this.mapper.selectOne(param);
            if (entity != null) {
                BeanUtils.copyProperties(entity, cache);
                redisTemplate.opsForHash().put(key(), param.getCode(), JacksonUtils.obj2json(cache));
            }
        }
        if (StringUtils.isNotEmpty(cache.getCode())) {
            BeanUtils.copyProperties(cache, vo);
            vo.setCurrencyCode(cache.getCode());
            vo.setCurrencySymbol(cache.getSymbol());
        }
        return vo;
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

