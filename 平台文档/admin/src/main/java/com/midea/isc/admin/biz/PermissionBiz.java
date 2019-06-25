package com.midea.isc.admin.biz;

import com.midea.isc.admin.model.User;
import com.midea.isc.admin.param.UserParam;
import com.midea.isc.common.model.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionBiz {
	@Autowired
	protected UserBiz userBiz;

	public UserInfo validate(String username, String password) {
		UserInfo info = new UserInfo();
		UserParam param = new UserParam();
		param.setUserName(username);
		param.setPassword(password);
		User user = null;
		if (user != null) {
			BeanUtils.copyProperties(user, info);
			info.setUserId(user.getUserId().toString());
		}

		return info;
	}

	public UserInfo ssoUser(String username) {
		UserInfo info = new UserInfo();
		UserParam param = new UserParam();
		param.setUserName(username);
		User user = null;
		if (user != null) {
			BeanUtils.copyProperties(user, info);
			info.setUserId(user.getUserId().toString());
		}

		return info;
	}
}
