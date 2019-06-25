package com.midea.isc.api.biz;

import com.midea.isc.api.mapper.CodeConfigcacheMapper;
import com.midea.isc.api.model.CodeConfigcache;
import com.midea.isc.api.param.CodeConfigcacheParam;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.util.BaseContextHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeConfigcacheBiz extends BaseBiz<CodeConfigcacheMapper, CodeConfigcache, CodeConfigcacheParam> {

    //插入DB重试次数
    private final static Integer retryCount = 5;

    public int getSequenceFromDB(Integer configId, String configCode, String expression, int quantity) {
        CodeConfigcache cache = getCacheByParam(configId, configCode, expression);
        if (null != cache) {
            return updateCacheByOtsLock(cache, quantity, retryCount);
        } else {
            return createSequence(configId, configCode, expression, quantity);
        }
    }

    private CodeConfigcache getCacheByParam(Integer configId, String configCode, String expression) {
        CodeConfigcacheParam param = new CodeConfigcacheParam();
        param.setConfigId(configId);
        param.setConfigCode(configCode);
        param.setExpression(expression);
        CodeConfigcache codeConfigcache = selectOne(param);
        //如果没有用户登录信息，默认设置系统操作人
        if (null != codeConfigcache && null == BaseContextHandler.getProfile()) {
            codeConfigcache.setCreatedBy("admin");
            codeConfigcache.setLastUpdatedBy("admin");
        }
        return codeConfigcache;
    }

    /**
     * @param configId   id
     * @param configCode code
     * @param expression 表达式
     * @param quantity   数量
     * @return
     */
    private int createSequence(Integer configId, String configCode, String expression, int quantity) {
        CodeConfigcache cache = new CodeConfigcache();
        cache.setConfigId(configId);
        cache.setConfigCode(configCode);
        cache.setExpression(expression);
        cache.setSequence(quantity);
        int inserResult = insert(cache);
        //插入失败时，重试查询
        if (0 == inserResult) {
            try {
                //休眠1秒
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                //系统异常，请联系IT管理员
                throw new IscException("CG-999", e.getMessage());
            }
            cache = getCacheByParam(configId, configCode, expression);
            if (null == cache) {
                //code创建序列号失败
                throw new IscException("CG-007", configCode);
            }
            return updateCacheByOtsLock(cache, quantity, retryCount);
        }
        return cache.getSequence();
    }

    /**
     * @param cache
     * @param quantity   数量
     * @param retryCount 重试次数
     * @return
     */
    public int updateCacheByOtsLock(CodeConfigcache cache, int quantity, int retryCount) {
        if (retryCount == 0) {
            //超过重试次数，更新序列号失败
            throw new IscException("CG-008", cache.getConfigCode());
        } else {
            retryCount = retryCount - 1;
        }
        cache.setSequence(cache.getSequence() + quantity);
        int sequence = updateFieldsByOtsLock(cache);
        if (sequence == 0) {
            try {
                Thread.sleep(1000);
                return updateCacheByOtsLock(cache, quantity, retryCount);
            } catch (InterruptedException e) {
                //系统异常，请联系IT管理员
                throw new IscException("CG-999", e.getMessage());
            }
        }
        return cache.getSequence();
    }

    public List<CodeConfigcache> getCacheFromDB(Integer configId) {
        CodeConfigcacheParam param = new CodeConfigcacheParam();
        param.setConfigId(configId);
        return find(param);
    }
}

