package com.midea.isc.attachment.sys;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.midea.isc.attachment.model.AttachmentConfig;
import com.midea.isc.attachment.model.MideaOSSCert;
import com.midea.isc.attachment.model.MideaOSSReturn;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.util.AESUtil;
import com.midea.isc.common.util.CommUtil;
import com.midea.isc.common.util.JacksonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MideaOSSCertification {

	@Autowired
	protected RedisTemplate<String, Object> redisTemplate;

	@Value("${spring.cloud.config.profile}")
	private String envTag;
	
	// 美的云盘临时token有效期默认为3600s，这里设置3500s，过期重新获取
	private static final long certificationExpired= 3500;

	private String key(String application) {
		return "oss:certificate:" +application+":"+ envTag;
	}

	public String certificate(AttachmentConfig config) throws IscException {
		Object certificateObject = redisTemplate.opsForValue().get(key(config.getApplication()));
		if (certificateObject != null) {
			return certificateObject.toString();
		} else {
			return getAppCert(config);
		}
	}

	public String getAppCert(AttachmentConfig config) {
		String result = "";
		if (config != null) {
			try {
				String url = "http://" + OSS.mideaOSSApplServiceAddr + "/v1/appmanager/certification/create";
				HashMap<String, Object> param = new HashMap<String, Object>();
				long ts = System.currentTimeMillis();
				param.put("appid", config.getAppId());
				param.put("ts", String.valueOf(ts));
				param.put("signature", AESUtil.md5(config.getAppId() + ts + config.getAppKey()));
				// param.put("expire", -1);
				param.put("acl", "wr");
				log.info(JacksonUtils.obj2json(param));
				String resp = CommUtil.callRestful(url, JacksonUtils.obj2json(param));
				if (resp != null) {
					MideaOSSReturn<MideaOSSCert> ossResult = JacksonUtils.json2Object(resp,
							new TypeReference<MideaOSSReturn<MideaOSSCert>>() {
							});
					if ("200".equals(ossResult.getCode())) {
						MideaOSSCert data = ossResult.getData();
						data.setTimestamp(ts);
						redisTemplate.opsForValue().set(key(config.getApplication()), data.getCertification(), certificationExpired, TimeUnit.SECONDS);
						result = data.getCertification();
					} else {
						throw new Exception(ossResult.getMsg());
					}
				}
			} catch (Exception e) {
				log.error(CommUtil.parseException(e));
			}
		}
		return result;
	}
}
