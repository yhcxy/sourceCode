package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.LocaleMapper;
import com.midea.isc.admin.mapper.LovValueMapper;
import com.midea.isc.admin.model.LovValue;
import com.midea.isc.admin.param.LocaleParam;
import com.midea.isc.admin.param.LovValueParam;
import com.midea.isc.admin.vo.LovValueParamVo;
import com.midea.isc.common.annotation.DS;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LovValueBiz extends BaseBiz<LovValueMapper,LovValue,LovValueParam> {

	@Value("${spring.cloud.config.profile}")
	private String envTag;

	@Autowired
	protected RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private LocaleMapper localeMapper;

	@DS("master")
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public int insert(LovValue model) {
		this.setWho(model);
		int count = mapper.insert(model);
		if(count>0){
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
		if(countTL==0){
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
        if(countTL==0){
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
        if(countTL==0){
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
        if(countTL==0){
            mapper.copySourceLang(model);
            countTL = mapper.updateFieldsTL(model);
        }
        
        return count;
    }

	public void enable(LovValueParamVo vo){
		super.setWhoForUpdateInfo(vo);
		mapper.enable(vo);
	}

	@DS("slave")
	public List<LovValue> listParent(LovValueParamVo vo){
		return mapper.listParent(vo);
	}

	public void updateValue(LovValueParamVo vo){
		super.setWhoForUpdateInfo(vo);
		mapper.updateValue(vo);

		redisTemplate.opsForHash().delete(
				this.key(vo.getApplication(), vo.getProfile().get__language()),
				vo.getTypeCode());
	}

	@Transactional
	public void insertValue(LovValueParamVo vo){
		LocaleParam p = new LocaleParam();
		p.setApplication(vo.getApplication());

		localeMapper.list(p).forEach(o -> {
			redisTemplate.opsForHash().delete(
					this.key(vo.getApplication(), o.getLanguage()),
					vo.getTypeCode());
		});

		super.setWho(vo);
		mapper.insert(vo);
		mapper.insertTL(vo);
	}

	@DS("master")
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void insert4Synchronize(LovValueParamVo param) {
		this.setWho(param);
		mapper.insert(param);

		if(null != param.getTls() && param.getTls().size() > 0){
			param.getTls().forEach(o -> {
				o.setProfile(param.getProfile());
				o.setParentLabel(param.getParentLabel());
			});

			mapper.insertTL4Synchronize(param.getTls());
		}
	}

	public List<LovValue> selectByApp(LovValueParamVo param){
		return mapper.selectByApp(param);
	}

	private String key(String application, String language) {
		return "lov:" + application + ":" + language + ":" + envTag;
	}
}

