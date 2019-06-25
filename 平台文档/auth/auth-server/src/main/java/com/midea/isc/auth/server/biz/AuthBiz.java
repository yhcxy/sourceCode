package com.midea.isc.auth.server.biz;

import com.esotericsoftware.minlog.Log;
import com.midea.isc.auth.common.jwt.TokenManager;
import com.midea.isc.auth.common.vo.AuthenticationRequest;
import com.midea.isc.auth.server.model.Locale;
import com.midea.isc.auth.server.model.User;
import com.midea.isc.auth.server.param.LocaleParam;
import com.midea.isc.auth.server.param.LoginLogParam;
import com.midea.isc.auth.server.param.UserParam;
import com.midea.isc.auth.server.sys.LanguageHelper;
import com.midea.isc.common.constant.CommonConstants;
import com.midea.isc.common.model.Profile;
import com.midea.isc.common.model.UserInfo;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.util.CommUtil;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthBiz {

	@Value("${sso.logout.url}")
	private String ssoLogoutUrl;
	
	@Value("${session.expired}")
	private long sessionExpired;

	@Autowired
	TokenManager tokenManager;
	
	@Autowired
	LanguageHelper languageHelper;

	@Autowired
	protected UserBiz userBiz;

	@Autowired
	protected LocaleBiz localeBiz;

	@Autowired
	protected LoginLogBiz loginLogBiz;

	@Autowired
	public AuthBiz() {
	}

	public Profile login(AuthenticationRequest authenticationRequest, boolean enableSSO) throws Exception {
		Profile profile = null;
		UserParam param = new UserParam();
		String username = authenticationRequest.getUsername();
		String app = authenticationRequest.getApp();
		param.setUserName(username);
		if (!enableSSO) {
			param.setPassword(authenticationRequest.getPassword());
		}
		User user = userBiz.selectOne(param);
		if (user != null) {
			UserInfo info = new UserInfo();
			BeanUtils.copyProperties(user, info);
			info.setUserId(user.getUserId().toString());
			profile = new Profile(info);
			String token = enableSSO ? authenticationRequest.getToken() : tokenManager.generateToken();
			profile.set__token(token);
			profile.set__app(app);
			// 注意当前会话的语言已经放在应用层控制，修改应用层profile一定要在初始化context之后再修改
			if (!CommUtil.isEmpty(authenticationRequest.getLanguage())) {
				String lang = authenticationRequest.getLanguage();
				lang = lang.indexOf("-") > 0 ? lang.substring(0, lang.indexOf("-")) : lang;
				lang = lang.indexOf("_") > 0 ? lang.substring(0, lang.indexOf("_")) : lang;
				// 暂不校验是否支持该语言
				// Language language = langDAO.selectByISOCode(lang);
				// if (language == null) {
				// throw new Message("common.login.invalidLanguage");
				// }
				profile.set__language(lang);
			}
			this.installedLanguages(profile);
			profile.set__sessionExpired(sessionExpired);
			profile.set__timestamp(System.currentTimeMillis());
			tokenManager.put(app, token, profile, sessionExpired);

			LoginLogParam loginLog = new LoginLogParam();
			loginLog.setUserId(user.getUserId());
			loginLog.setUserName(user.getUserName());
			loginLog.setFullname(user.getFullName());
			loginLog.setLanguage(profile.get__language());
			loginLog.setToken(token);
			loginLog.setApplication(app);
			loginLog.setIpAddress(authenticationRequest.getIpAddress());
			loginLog.setLoginTime(DateTime.now().toDate());
			loginLogBiz.insert(loginLog);
		} else {
			Log.error("login failed:" + app + "," + username);
			throw new IscException("ISC-950", "user not existed or password wrong!");
		}

		return profile;
	}

	public String logout(String app, String token) {
		tokenManager.remove(app, token);
		return ssoLogoutUrl;
	}

	public Profile updateProfile(String app, String token, Profile param) throws IscException {
		Profile profile = tokenManager.get(app, token);
		if (profile != null) {
			boolean updated = false;

			if (!CommUtil.isEmpty(param.get__timezone())) {
				profile.set__timezone(param.get__timezone());
				updated = true;
			}

			if (!CommUtil.isEmpty(param.get__language())) {
				profile.set__language(param.get__language());
				updated = true;
			}

			if (!CommUtil.isEmpty(param.get__currency())) {
				profile.set__currency(param.get__currency());
				updated = true;
			}

			if (!CommUtil.isEmpty(param.get__sobId())) {
				profile.set__sobId(param.get__sobId());
				updated = true;
			}

			if (!CommUtil.isEmpty(param.get__divisionId())) {
				profile.set__divisionId(param.get__divisionId());
				updated = true;
			}

			if (!CommUtil.isEmpty(param.get__organizationId())) {
				profile.set__organizationId(param.get__organizationId());
				updated = true;
			}

			if (param.get__infos() != null) {
				profile.set__infos(param.get__infos());
				updated = true;
			}

			if (updated) {
				tokenManager.update(profile, app);
			}
		} else {
			throw new IscException("ISC-919", "Get Profile failed by app+token:" + app + "+" + token);
		}

		return profile;
	}

	public Profile updateUserProfile(String app, String token, Profile param) throws IscException {
		//刷新缓存
		Profile profile = this.updateProfile(app,token,param);
		//更新用户信息
		User updateUser = new User();
		updateUser.setUserId(Integer.parseInt(profile.get__userId()));
		updateUser.setTimezone(profile.get__timezone());
		updateUser.setLanguage(profile.get__language());
		userBiz.updateFields(updateUser);
		return profile;
	}

	/**
	 * 加载应用语言
	 * @param profile
	 */
	private void installedLanguages(Profile profile) {
		String applicationName = profile.get__app();
		List<String> languages = languageHelper.getAppLanguage(applicationName);
		if(languages == null){
			languages = localeBiz.languages(applicationName);
			if (languages.size() == 0) {
				languages.add(CommonConstants.SYSTEM_LANGUAGE);
			}
			languageHelper.setAppLanguage(applicationName, languages);
		}
		profile.setLanguages(languages);
		// 校验用户语言是否为应用支持，不支持则设为应用默认语言
		if(!profile.getLanguages().contains(profile.get__language())){
			LocaleParam param = new LocaleParam();
			param.setApplication(applicationName);
			param.setDefaultFlag("Y");
			List<Locale> localeList = localeBiz.selectList(param);
			if(localeList.size()>0){
				profile.set__language(localeList.get(0).getLanguage());
			}else{
				profile.set__language(CommonConstants.SYSTEM_LANGUAGE);
			}
		}
	}
}
