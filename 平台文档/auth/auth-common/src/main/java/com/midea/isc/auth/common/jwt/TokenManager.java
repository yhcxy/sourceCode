package com.midea.isc.auth.common.jwt;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.midea.isc.common.model.Profile;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.util.JacksonUtils;

public class TokenManager {
	private static final Logger log = LoggerFactory.getLogger(TokenManager.class);

	public TokenManager() {
	}

	@Autowired
	protected RedisTemplate<String, Object> redisTemplate;

	private String key(String app, String token) {
		String prefix = app + "_token:";
		return prefix + token;
	}

	public boolean containsKey(String app, String token) {
		return redisTemplate.hasKey(key(app, token));
	}

	public void put(String app, String token, Profile profile, long sessionExpired) {
		String key = key(app, token);
		log.debug("new token=app:" + app + ",token:" + token + ",key:" + key);
		redisTemplate.opsForValue().set(key, profile, sessionExpired, TimeUnit.SECONDS);
	}

	public void expire(String app, String token, long sessionExpired) {
		redisTemplate.expire(key(app, token), sessionExpired, TimeUnit.SECONDS);
	}

	public Profile getAndSetExpire(String app, String token) throws IscException {
		String key = key(app, token);
		HashMap map = (HashMap) redisTemplate.opsForValue().get(key);
		Profile profile = map == null ? null : JacksonUtils.map2pojo(map, Profile.class);
		if (profile == null)
			throw new IscException("ISC-912", "Invalid Token:" + token);
		redisTemplate.expire(key, profile.get__sessionExpired(), TimeUnit.SECONDS);

		log.debug("app:" + app + ",token:" + token + ",key:" + key);

		return profile;
	}

	/**
	 * 从缓存获取Token对应的Profile
	 * 
	 * @param token
	 * @return Profile
	 * @throws InvalidException
	 *             profile不存在则抛出此异常
	 * @throws ExpiredException
	 *             token过期则抛出此异常
	 */
	public Profile get(String app, String token) throws IscException {
		String key = key(app, token);
		HashMap map = (HashMap) redisTemplate.opsForValue().get(key);
		Profile profile = map == null ? null : JacksonUtils.map2pojo(map, Profile.class);
		if (profile == null)
			throw new IscException("ISC-912", "Invalid App+Token:" + app + "+" + token);

		return profile;
	}

	public void remove(String app, String token) {
		redisTemplate.delete(key(app, token));
	}

	public void update(Profile profile, String app) {
		redisTemplate.opsForValue().set(key(app, profile.get__token()), profile);
	}

	public String generateToken() {
		return UUID.randomUUID().toString();
	}

	public void addProfile(String token, Profile profile, String app, long sessionExpired) {
		profile.set__timestamp(System.currentTimeMillis());
		put(app, token, profile, sessionExpired);
	}
}