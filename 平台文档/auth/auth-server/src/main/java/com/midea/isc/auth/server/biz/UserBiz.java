package com.midea.isc.auth.server.biz;

import org.springframework.stereotype.Service;
import com.midea.isc.auth.server.model.User;
import com.midea.isc.auth.server.param.UserParam;
import com.midea.isc.auth.server.mapper.UserMapper;
import com.midea.isc.common.biz.BaseBiz;

@Service
public class UserBiz extends BaseBiz<UserMapper,User,UserParam> {
	
}

