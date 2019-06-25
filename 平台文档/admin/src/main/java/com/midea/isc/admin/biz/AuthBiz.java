package com.midea.isc.admin.biz;

import com.midea.isc.admin.model.User;
import com.midea.isc.admin.param.UserParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthBiz {
	@Autowired
	protected UserBiz userBiz;

	public List<User> testMore(UserParam param) {
		User user1 = userBiz.testMore1(param);
		User user2 = userBiz.testMore2(param);
		List<User> list = new ArrayList<>();
		list.add(user1);
		list.add(user2);

		return list;
	}
}
