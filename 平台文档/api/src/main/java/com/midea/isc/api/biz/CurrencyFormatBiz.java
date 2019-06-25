package com.midea.isc.api.biz;

import com.fasterxml.jackson.core.type.TypeReference;
import com.midea.isc.api.vo.FormatVo;
import com.midea.isc.common.util.JacksonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.midea.isc.api.model.CurrencyFormat;
import com.midea.isc.api.param.CurrencyFormatParam;
import com.midea.isc.api.mapper.CurrencyFormatMapper;
import com.midea.isc.common.biz.BaseBiz;

@Service
@Slf4j
public class CurrencyFormatBiz extends BaseBiz<CurrencyFormatMapper,CurrencyFormat,CurrencyFormatParam> {

    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    @Value("${spring.cloud.config.profile}")
    private String envTag;

    private String key() {
        return "currency:country:format:" + envTag;
    }

    private String field(CurrencyFormat format){
        return format.getCurrencyCode() +"_" + format.getTerritoryCode();
    }

    public FormatVo selectOneWithCache(CurrencyFormatParam param){
        FormatVo cache = new FormatVo();
        if(param.getCurrencyCode() != null && param.getTerritoryCode()!=null){
           if( redisTemplate.opsForHash().hasKey(key(), field(param))) {
               String json = (String) redisTemplate.opsForHash().get(key(), field(param));
               cache = JacksonUtils.json2Object(json, new TypeReference<FormatVo>() {
               });
           }
        }
        if(StringUtils.isEmpty(cache.getCurrencyCode())) {
            CurrencyFormat entity = this.mapper.selectOne(param);
            if(entity!=null) {
                BeanUtils.copyProperties(entity, cache);
                redisTemplate.opsForHash().put(key(), field(param), JacksonUtils.obj2json(cache));
            }
        }
        return  cache;
    }

}

