package com.midea.isc.api.biz;

import com.alibaba.fastjson.JSON;
import com.midea.isc.api.mapper.CodeConfigMapper;
import com.midea.isc.api.model.CodeConfig;
import com.midea.isc.api.model.CodeConfigcache;
import com.midea.isc.api.model.CodeConfigitem;
import com.midea.isc.api.param.CodeConfigParam;
import com.midea.isc.api.param.CodeConfigcacheParam;
import com.midea.isc.api.vo.CodeInfoVo;
import com.midea.isc.api.vo.SequenceCodeParamVo;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CodeConfigBiz<main> extends BaseBiz<CodeConfigMapper, CodeConfig, CodeConfigParam> {

    @Value("${spring.cloud.config.profile}")
    private String envTag;
    @Autowired
    private CodeConfigitemBiz codeConfigitemBiz;
    @Autowired
    private CodeConfigcacheBiz codeConfigcacheBiz;
    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    //编码重置周期-永久
    private static final String CG_CONF_CYCLE_PERMANENT = "PERMANENT";
    //编码重置周期-年
    private static final String CG_CONF_CYCLE_YEAR = "YEAR";
    //编码重置周期-月
    private static final String CG_CONF_CYCLE_MONTH = "MONTH";
    //编码重置周期-日
    private static final String CG_CONF_CYCLE_DAY = "DAY";

    //编码缓存类型-数据库
    private static final String CG_CONF_CACHE_MODE_DB = "DB";
    //编码缓存类型-REDIS
    private static final String CG_CONF_CACHE_MODE_REDIS = "REDIS";


    /**
     * 生成序列号
     *
     * @param vo
     * @return
     */
    public List<String> createSequenceCode(SequenceCodeParamVo vo) {
        CodeInfoVo info = getCodeInfoVo(vo.getApplication(), vo.getCode());
        //检查items规则是否正确
        codeConfigitemBiz.checkItems(info.getItems(), vo.getCode(), vo.getParams());
        //表达式
        String expression = codeConfigitemBiz.createExpression(info.getItems(), vo.getParams());
        // HashKEY  code:表达式
        String hashKey = vo.getCode() + ":" + expression;

        //检查是否含有序列的规则
        int sequence = 0;
        long serialTypeCount = info.getItems().stream().filter(item -> item.getType().equals(codeConfigitemBiz.CG_CONF_ITEM_TYPE_SERIAL)).count();
        if (serialTypeCount >= 1l) {
            if (info.getCodeConfig().getCacheMode().equals(CG_CONF_CACHE_MODE_DB)) {
                //从数据库获取序列号
                sequence = codeConfigcacheBiz.getSequenceFromDB(info.getCodeConfig().getConfigId(), info.getCodeConfig().getCode(), expression, vo.getQuantity());
            } else if (info.getCodeConfig().getCacheMode().equals(CG_CONF_CACHE_MODE_REDIS)) {
                //编码规则实例缓存 cg:application:yyyymmdd:env
                String cacheKey = getCodeConfigCacheKey(vo.getApplication(), info.getCodeConfig().getCycle());

                //从REDIS获取序列号 field code+表达式
                sequence = getSequenceFromRedis(cacheKey, hashKey, info.getCodeConfig().getCycle(), vo.getQuantity());
            }
        }
        return codeConfigitemBiz.createSequenceCode(info.getItems(), vo.getParams(), sequence, vo.getQuantity());
    }

    /**
     * @param application
     * @param code
     * @return
     */
    public List<CodeConfigcache> getInstanceSequence(String application, String code) {
        List<CodeConfigcache> caches = new ArrayList<>();
        CodeInfoVo info = getCodeInfoVo(application, code);
        if (info.getCodeConfig().getCacheMode().equals(CG_CONF_CACHE_MODE_REDIS)) {
            //编码规则实例缓存 cg:application:yyyymmdd:env
            String cacheKey = getCodeConfigCacheKey(application, info.getCodeConfig().getCycle());
            String hashKey = code + ":*";
            Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(cacheKey, ScanOptions.scanOptions().match(hashKey).build());
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = cursor.next();
                CodeConfigcache codeConfigcache = new CodeConfigcache();
                codeConfigcache.setConfigId(info.getCodeConfig().getConfigId());
                codeConfigcache.setConfigCode(code);
                codeConfigcache.setExpression(entry.getKey().toString());
                codeConfigcache.setSequence(Integer.parseInt(entry.getValue().toString()));
                caches.add(codeConfigcache);
            }
        } else if (info.getCodeConfig().getCacheMode().equals(CG_CONF_CACHE_MODE_DB)) {
            caches = codeConfigcacheBiz.getCacheFromDB(info.getCodeConfig().getConfigId());
        }
        return caches;
    }

    public boolean synchronize(String application, String code) {
        //单据编码规则配置缓存 cg:rule:application:code:env
        String codeKey = getCodeConfigRuleKey(application, code);
        return redisTemplate.delete(codeKey);
    }

    public boolean resetSeequence(String application, String code, String expression, int sequence) {
        boolean result = true;
        CodeInfoVo info = getCodeInfoVo(application, code);
        if (info.getCodeConfig().getCacheMode().equals(CG_CONF_CACHE_MODE_REDIS)) {
            String cacheKey = getCodeConfigCacheKey(application, info.getCodeConfig().getCycle());
            redisTemplate.opsForHash().put(cacheKey, expression, sequence);
        } else if (info.getCodeConfig().getCacheMode().equals(CG_CONF_CACHE_MODE_DB)) {
            CodeConfigcacheParam param = new CodeConfigcacheParam();
            param.setConfigId(info.getCodeConfig().getConfigId());
            param.setConfigCode(code);
            param.setExpression(expression);
            CodeConfigcache cache = codeConfigcacheBiz.selectOne(param);
            cache.setSequence(sequence);
            if (codeConfigcacheBiz.updateCacheByOtsLock(cache, 0, 5) == 0) {
                result = false;
            }
        }
        return result;
    }

    /**
     * 获取规则
     */
    private CodeInfoVo getCodeInfoVo(String application, String code) {
        //单据编码规则配置缓存 cg:rule:application:code:env
        String codeKey = getCodeConfigRuleKey(application, code);

        CodeInfoVo info = new CodeInfoVo();
        //从redis缓存读取
        Object o = redisTemplate.opsForValue().get(codeKey);
        if (null != o) {
            info = JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(o)), CodeInfoVo.class);
        } else {
            //从DB读取规则
            info = getCodeInfoVoFromBD(application, code);
        }
        //24小时后过期
        redisTemplate.opsForValue().set(codeKey, info, 24, TimeUnit.HOURS);
        return info;
    }

    /**
     * 从 DB读取规则
     *
     * @param application
     * @param code
     * @return
     */
    private CodeInfoVo getCodeInfoVoFromBD(String application, String code) {
        CodeInfoVo info = new CodeInfoVo();
        CodeConfigParam param = new CodeConfigParam();
        param.setApplication(application);
        param.setCode(code);
        CodeConfig codeConfig = selectOne(param);
        if (null == codeConfig) {
            //code编码不存在
            throw new IscException("CG-003", code);
        }
        info.setCodeConfig(codeConfig);

        List<CodeConfigitem> items = codeConfigitemBiz.getItemsByConfigId(codeConfig.getConfigId());
        info.setItems(items);
        return info;
    }


    /**
     * 从redis获取序列
     *
     * @param redisKey redisKey
     * @param hashKey  hashKey
     * @param cycle    重置周期
     * @param quantity 数量
     * @return
     */
    private int getSequenceFromRedis(String redisKey, String hashKey, String cycle, int quantity) {
        int sequence = 0;
        Object o = redisTemplate.opsForHash().get(redisKey, hashKey);
        redisTemplate.opsForHash().increment(redisKey, hashKey, quantity);
        if (null != o) {
            sequence = Integer.parseInt(o.toString()) + quantity;
        } else {
            //创建过期时间
            redisTemplate.expireAt(redisKey, getCycleDate(cycle));
            sequence = quantity;
        }
        return sequence;
    }

    /**
     * 获取规则key
     *
     * @param application 系统名称
     * @param code        编码
     * @return
     */
    private String getCodeConfigRuleKey(String application, String code) {
        //单据编码规则配置缓存 cg:rule:application:code:env
        StringBuilder codeKey = new StringBuilder();
        codeKey.append("CG").append(":").append("RULE").append(":").append(application).append(":").append(code).append(":").append(envTag);
        return codeKey.toString();
    }

    /**
     * 获取规则序列缓存key
     *
     * @return
     */
    private String getCodeConfigCacheKey(String application, String cycle) {
        //编码规则实例缓存 cg:application:yyyymmdd:env
        StringBuilder sequenceKey = new StringBuilder();
        String sequenceKeyDate = getDataFormat(cycle);
        sequenceKey.append("CG").append(":").append(application).append(":").append(sequenceKeyDate).append(":").append(envTag);
        return sequenceKey.toString();
    }

    private String getDataFormat(String cycle) {
        if (cycle.equals(CG_CONF_CYCLE_YEAR)) {
            return new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
        } else if (cycle.equals(CG_CONF_CYCLE_MONTH)) {
            return new SimpleDateFormat("yyyyMM").format(Calendar.getInstance().getTime());
        } else if (cycle.equals(CG_CONF_CYCLE_DAY)) {
            return new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        }
        return "";
    }

    private Date getCycleDate(String cycle) {
        Calendar calendar = Calendar.getInstance();
        //当前日期：2019-06-24
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (cycle.equals(CG_CONF_CYCLE_YEAR)) {
            // 下一年日期：2020-01-02
            calendar.add(Calendar.YEAR, 1);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_YEAR, 2);
        } else if (cycle.equals(CG_CONF_CYCLE_MONTH)) {
            // 下个月日期：2019-07-02
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 2);
        } else if (cycle.equals(CG_CONF_CYCLE_DAY)) {
            //两天后 2019-06-26
            calendar.add(Calendar.DAY_OF_MONTH, 2);
        }
        return calendar.getTime();
    }


}

