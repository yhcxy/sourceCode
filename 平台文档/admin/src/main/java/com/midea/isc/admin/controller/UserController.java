package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.*;
import com.midea.isc.admin.model.User;
import com.midea.isc.admin.param.LanguageParam;
import com.midea.isc.admin.param.TerritoryParam;
import com.midea.isc.admin.param.TimezoneParam;
import com.midea.isc.admin.param.UserParam;
import com.midea.isc.admin.vo.UserParamVo;
import com.midea.isc.admin.vo.UserVo;
import com.midea.isc.auth.client.annotation.ServiceLog;
import com.midea.isc.common.param.BasicParam;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.web.annotation.IgnoreResultHandler;
import com.midea.isc.common.web.contoller.BasicController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
@RefreshScope
public class UserController extends BasicController<UserBiz, User, UserParam> {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	AuthBiz authBiz;

	@Autowired
	protected RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private LanguageBiz languageBiz;

	@Autowired
	private TimezoneBiz timezoneBiz;

	@Autowired
	private TerritoryBiz territoryBiz;

	@ServiceLog
	@RequestMapping(value = "/testMore", method = RequestMethod.POST)
	public List<User> testMore(@RequestBody UserParam param) throws IllegalAccessException {
		List<User> result = new ArrayList<>();
		result.addAll(authBiz.testMore(param));
		// result.add(baseBiz.selectOne(param));
		// result.add(baseBiz.testMore2(param));
		redisTemplate.opsForHash().put("test1", "test2", result);
		String testRedis = redisTemplate.opsForHash().get("test1", "test2").toString();
		log.trace("trace");
		log.debug("debug");
		log.info("info");
		log.warn("warn");
		log.error("error");
		log.info(testRedis);

		return result;
	}

	@RequestMapping(value = "/testNull", method = RequestMethod.POST)
	public void testNull(@RequestBody UserParam param) throws IllegalAccessException {
	}

	@RequestMapping(value = "/testInt", method = RequestMethod.POST)
	public int testInt(@RequestBody UserParam param) throws IllegalAccessException {
		return 123456;
	}

	@IgnoreResultHandler
	@RequestMapping(value = "/testIgnore", method = RequestMethod.POST)
	public List<User> testIgnore(@RequestBody UserParam param) throws IllegalAccessException {
		List<User> result = new ArrayList<>();
		result.addAll(authBiz.testMore(param));
		// result.add(baseBiz.selectOne(param));
		// result.add(baseBiz.testMore2(param));
		redisTemplate.opsForHash().put("test1", "test2", result);
		String testRedis = redisTemplate.opsForHash().get("test1", "test2").toString();
		log.info(testRedis);

		return result;
	}

	@RequestMapping(value = "/userlist", method = RequestMethod.POST)
	public List<UserVo> userlist(@RequestBody UserParamVo userVo) {
		return this.baseBiz.selectUserList(userVo);
	}

	@PostMapping(value = "/insertUser")
	public Integer insertUser(@RequestBody UserVo userVo) {
		return this.baseBiz.insertUser(userVo);
	}

	@PostMapping("/updateUser")
	public Integer updateUser(@RequestBody UserVo userVo) {
		return this.baseBiz.updateUser(userVo);
	}

	@PostMapping("/deleteUser")
	public Integer deleteUser(@RequestBody UserParamVo param) {
		return this.baseBiz.deleteUser(param);
	}

	public static void main(String[] args) throws IscException {
//		User test = new User();
//		Profile profile = new Profile("1", "admin");
//		profile.set__country("CN");
//		test.setProfile(profile);
//		test.set__application("admin");
//		test.setUserId(12345);
//
//		String json = JacksonUtils.obj2json(test);
//		System.out.println(json);
//
//		JSONObject obj = new JSONObject();
//
//		obj.put("userId", 1234567);
//		JSONObject profile1 = new JSONObject();
//		profile1.put("__userId", 1);
//		profile1.put("__userName", "admin");
//		profile1.put("__country", "CN");
//		obj.put("profile", profile1);
//		obj.put("__application", "admin");
//		String objtostring = obj.toJSONString();
//		System.out.println(objtostring);
//
//		User userTest = JacksonUtils.json2pojo(objtostring, User.class);
//		System.out.println(userTest);

		UserParam param = new UserParam();
		Class<?> clazz = param.getClass();
		System.out.println(clazz.getName());
		Class<?> clazz1 = clazz.getSuperclass();
		System.out.println(clazz1.getName());

		BasicParam b = new BasicParam();
		System.out.println(b.getClass().getSuperclass().getName());

		Class<?> p = b.getClass().getSuperclass().getSuperclass();
		System.out.println(p);

	}

	@PostMapping("/findUser")
	public List<User> findUser(@RequestBody UserParam param) {
		return baseBiz.findUser(param);
	}

	@PostMapping("/getUserInfoLovs")
	public Map<String, Object> getUserInfoLovs(@RequestBody UserParam param) {
		LanguageParam lanParam = new LanguageParam();
		TimezoneParam timezoneParam = new TimezoneParam();
		TerritoryParam territoryParam = new TerritoryParam();
		Map<String, Object> result = new HashMap<>();
		lanParam.setProfile(param.getProfile());
		timezoneParam.setProfile(param.getProfile());
		territoryParam.setProfile(param.getProfile());
		result.put("languageList", languageBiz.find(lanParam));
		result.put("timezoneList", timezoneBiz.find(timezoneParam));
		result.put("territoryList", territoryBiz.find(territoryParam));
		return result;
	}
}
