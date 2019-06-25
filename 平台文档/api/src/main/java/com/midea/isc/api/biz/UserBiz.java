package com.midea.isc.api.biz;

import org.springframework.stereotype.Service;
import com.midea.isc.api.model.User;
import com.midea.isc.api.param.UserParam;
import com.midea.isc.api.mapper.UserMapper;
import com.midea.isc.common.biz.BaseBiz;

@Service
public class UserBiz extends BaseBiz<UserMapper,User,UserParam> {
	public Integer getld(String userName){
        UserParam param = new UserParam();
        param.setUserName(userName);
	    return mapper.selectOne(param).getUserId();
    }
}

