package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.LovTypeMapper;
import com.midea.isc.admin.mapper.LovValueMapper;
import com.midea.isc.admin.model.LovType;
import com.midea.isc.admin.model.LovValue;
import com.midea.isc.admin.param.LovTypeParam;
import com.midea.isc.admin.vo.LovTypeParamVo;
import com.midea.isc.admin.vo.LovValueParamVo;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class LovTypeBiz extends BaseBiz<LovTypeMapper,LovType,LovTypeParam> {

    @Autowired
    private LovValueBiz lovValueBiz;

    @Autowired
    private LovValueMapper lovValueMapper;

    @Value("${spring.cloud.config.profile}")
    private String envTag;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private LocaleBiz localeBiz;

    private static ReentrantLock lock;
    private static Queue<Condition> queue = new LinkedList<>();
    private static int runningCount = 0;

	public void enable(LovTypeParamVo vo){
        List<LovType> types = mapper.list4Refresh(vo);
        super.setWhoForUpdateInfo(vo);
	    mapper.enable(vo);

        List<String> langs = localeBiz.getLanguages(vo.getApplication());
        if("Y".equals(vo.getEnable())){
            for(String lang : langs){
                for(LovType type : types)
                    this.refreshCache(vo.getApplication(), type.getCode(), lang, null);
            }
        }else {
            for(String lang : langs){
                for(LovType type : types)
                    this.refreshCache(vo.getApplication(), null, lang, type.getCode());
            }
        }
    }

    public void updateType(LovTypeParamVo vo){
        super.setWhoForUpdateInfo(vo);
        LovTypeParam param = new LovTypeParam();
        param.setTypeId(vo.getTypeId());
        LovType type = mapper.selectOne(vo);
        List<String> langs = localeBiz.getLanguages(vo.getApplication());

        mapper.update(vo);
        lovValueMapper.updateTypeCode(vo);


        if(null != type){
            if(!type.getCode().equals(vo.getCode())){
                for(String lang : langs){
                    this.refreshCache(vo.getApplication(), vo.getCode(), lang, type.getCode());
                }
            }else {
                for(String lang : langs){
                    this.refreshCache(vo.getApplication(), vo.getCode(), lang, null);
                }
            }
        }
    }

    @Transactional
    public void synchronize(LovTypeParamVo vo){
	    vo.setEnable("Y");
        this.insert(vo);

        if(null != vo.getChildren() && !vo.getChildren().isEmpty()){
            for(LovTypeParamVo c : vo.getChildren()){
                c.setEnable("Y");
                c.setParentId(vo.getTypeId());
                c.setParentCode(vo.getCode());
                c.setProfile(vo.getProfile());
                c.setApplication(vo.getApplication());
                this.synchronize(c);
            }
        }

        if(null != vo.getValues() && !vo.getValues().isEmpty()){
            for(LovValueParamVo c : vo.getValues()){
                c.setEnable("Y");
                c.setTypeId(vo.getTypeId());
                c.setTypeCode(vo.getCode());
                this.synchronizeValue(c);
            }
        }
    }

    @Transactional
    public void synchronizeValue(LovValueParamVo vo){
	    lovValueBiz.insert4Synchronize(vo);
        if(null != vo.getChildren() && !vo.getChildren().isEmpty()){
            for(LovValueParamVo c : vo.getChildren()){
                c.setTypeCode(vo.getTypeCode());
                c.setTypeId(vo.getTypeId());
                c.setParentId(vo.getValueId());
                c.setParentLabel(vo.getParentLabel());
                c.setProfile(vo.getProfile());
                c.setParentValue(vo.getValue());
                this.synchronizeValue(c);
            }
        }
    }

    @Transactional
    public void synchronizeOne(LovTypeParamVo vo) throws InterruptedException {
	    lock.lock();
	    LovTypeBiz.runningCount++;
	    if(LovTypeBiz.runningCount > 0){
            Condition condition = lock.newCondition();
            LovTypeBiz.queue.add(condition);
            condition.await();
        }

	    LovTypeParam p =  new LovTypeParam();
	    p.setCode(vo.getCode());
        LovType type = mapper.selectOne(p);
        Integer typeId = null;

        if(null == type){
            vo.setEnable("Y");
            this.insert(vo);

            typeId = vo.getTypeId();
        }else {
            typeId = type.getTypeId();
        }

        if(null != vo.getValue()){
            vo.getValue().setTypeCode(vo.getCode());
            vo.getValue().setTypeId(typeId);
            lovValueMapper.insert(vo.getValue());
        }

        if( LovTypeBiz.queue.size() > 0)
            LovTypeBiz.queue.poll().signal();
        lock.unlock();
    }

    private String key(String application, String language) {
        return "lov:" + application + ":" + language + ":" + envTag;
    }

    /**
     * 根据app+lang+code刷新缓存
     * @param app
     * @param code
     * @param language
     */
    private void refreshCache(String app, String code, String language, String oldCode){
        HashOperations op = redisTemplate.opsForHash();

        if(null == code || (null != oldCode && !code.equals(oldCode))){
            op.delete(this.key(app, language), oldCode);
        }

        if(null != code){
            LovValueParamVo lovValueParam = new LovValueParamVo();
            Profile profile = new Profile();
            lovValueParam.setProfile(profile);
            lovValueParam.setTypeCode(code);
            lovValueParam.setApplication(app);
            profile.set__language(language);
            List<LovValue> values = lovValueBiz.selectByApp(lovValueParam);

            redisTemplate.opsForHash().put(
                    this.key(app, language),
                    code, values);
        }
    }

    public void refreshCache(LovTypeParamVo vo){
        List<LovType> types = this.list4Refresh(vo);
        List<String> langs = localeBiz.getLanguages(vo.getApplication());

        for(LovType type : types){
            for (String lang : langs)
                this.refreshCache(type.getApplication(), type.getCode(), lang, null);
        }
    }

    private void refreshCache(String app, String language){
        LovTypeParam p = new LovTypeParam();
        p.setApplication(app);
        p.setEnable("Y");
        List<LovType> types = this.selectList(p);

        for(LovType type : types){
            this.refreshCache(app, type.getCode(), language, null);
        }
    }

    public List<LovType> list4Refresh(LovTypeParamVo vo){
        return mapper.list4Refresh(vo);
    }
}

