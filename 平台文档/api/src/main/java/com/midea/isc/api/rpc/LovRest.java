package com.midea.isc.api.rpc;

import com.midea.isc.api.biz.LovTypeBiz;
import com.midea.isc.api.biz.LovValueBiz;
import com.midea.isc.api.model.LovValue;
import com.midea.isc.api.vo.LovTypeParamVo;
import com.midea.isc.api.vo.LovValueParamVo;
import com.midea.isc.common.model.LovRedis;
import com.midea.isc.common.model.Profile;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.web.rpc.BasicRest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(value="数据字典API", tags={"lov","dictionary"})
@RestController
@RequestMapping("lov")
public class LovRest extends BasicRest {
	@Autowired
	protected RedisTemplate<String, Object> redisTemplate;

	@Value("${spring.cloud.config.profile}")
	private String envTag;

	@Autowired
	private LovValueBiz lovBiz;

	@Autowired
	private LovTypeBiz lovTypeBiz;

	private String key(String application, String language) {
		return "lov:" + application + ":" + language + ":" + envTag;
	}

	@ApiOperation("通过types获取LOV数据")
	@RequestMapping(value = "/getByTypes", method = RequestMethod.POST)
	public Map<String, List<LovRedis>> getByTypes(
				@ApiParam(value = "应用名称", required = true)String application,
			@ApiParam(value = "获取语言", required = true)String language,
			@ApiParam(value = "类型列表,逗号分隔", required = true)String types)
			throws IscException {
		Map<String, List<LovRedis>> result = new HashMap<>();
		List<String> typeList = Arrays.asList(types.split(","));
		List<String> queryList = new ArrayList<>();
		String nodeKey = this.key(application, language);
		// 先从缓存加载
		for (String type : typeList) {
			Object lovObj = redisTemplate.opsForHash().get(nodeKey, type);
			if (lovObj != null) {
				result.put(type, (List<LovRedis>) lovObj);
			} else {
				queryList.add(type);
			}
		}
		// 对于未缓存的LOV从数据获取并加入缓存
		if (!queryList.isEmpty()) {
			result.putAll(this.getLOVFromDB(application, language, queryList));
		}

		return result;
	}

	@ApiOperation("批量同步lov")
	@PostMapping(value = "/synchronize")
	public void synchronize(@RequestBody LovTypeParamVo vo) {
		lovTypeBiz.synchronize(vo);
	}

	@ApiOperation("单条同步lov")
	@PostMapping(value = "/synchronizeOne")
	public void synchronizeOne(@RequestBody LovTypeParamVo vo) throws InterruptedException {
		lovTypeBiz.synchronizeOne(vo);
	}

	private Map<String, List<LovRedis>> getLOVFromDB(String application, String language, List<String> queryList) {
		Map<String, List<LovRedis>> result = new HashMap<>();
		LovValueParamVo param = new LovValueParamVo();
		Profile profile = new Profile();
		profile.set__language(language);
		param.setProfile(profile);
		param.setTypeCode(String.join(",", queryList));
		param.setTypeCodeCond("me");
		param.setEnable("Y");
		param.setApplication(application);
		List<LovValue> queryResult = lovBiz.selectByApp(param);
		String nodeKey = this.key(application, language);
		for (String type : queryList) {
			List<LovRedis> lovs = new ArrayList<>();
			for (LovValue item : queryResult) {
				if (item.getTypeCode().equals(type)) {
					LovRedis lov = new LovRedis();
					BeanUtils.copyProperties(item, lov);
					lovs.add(lov);
				}
			}
			redisTemplate.opsForHash().put(nodeKey, type, lovs);
			result.put(type, lovs);
		}

		return result;
	}


}
