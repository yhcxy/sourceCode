package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.UserMapper;
import com.midea.isc.admin.model.Access;
import com.midea.isc.admin.model.User;
import com.midea.isc.admin.param.AccessParam;
import com.midea.isc.admin.param.UserParam;
import com.midea.isc.admin.vo.AccessParamVo;
import com.midea.isc.admin.vo.UserParamVo;
import com.midea.isc.admin.vo.UserVo;
import com.midea.isc.common.annotation.DS;
import com.midea.isc.common.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserBiz extends BaseBiz<UserMapper, User, UserParam> {
    @Autowired
    private AccessBiz accessBiz;

    @DS("master")
    public User testMore1(UserParam param) {
        return mapper.selectOne(param);
    }

    @DS("migration")
    public User testMore2(UserParam param) {
        return mapper.testMore(param);
    }

    @Transactional
    public Integer insertUser(UserVo userVo) {
        int count = 0;
        this.setWho(userVo);
        if (StringUtils.isEmpty(userVo.getApplication())) {
            userVo.setApplication(userVo.getProfile().get__app());
        }
        UserParam searchUserParam = new UserParam();
        searchUserParam.setUserName(userVo.getUserName());
        User searchUser = mapper.selectOne(searchUserParam);
        //避免重复插入User报错
        AccessParam ap = new AccessParam();
        if (searchUser == null) {
            this.setWho(userVo);
            count = mapper.insert(userVo);
            ap.setUserId(userVo.getUserId());
        } else {
            ap.setUserId(searchUser.getUserId());
        }
        ap.setAccessType("A");
        ap.setEnableFlag("Y");
        ap.setApplication(userVo.getApplication());
        ap.setProfile(userVo.getProfile());
        count += accessBiz.insert(ap);
        return count;
    }

    public List<UserVo> selectUserList(UserParamVo param) {
        if (StringUtils.isEmpty(param.getApplication())) {
            param.setApplication(param.getProfile().get__app());
        }
        return mapper.selectUserList(param);
    }

    public Integer updateUser(UserVo userVo) {
        if (userVo.getUserId() == null) {
            userVo.setUserId(Integer.parseInt(userVo.getProfile().get__userId()));
        }
        this.setWhoForUpdateInfo(userVo);
        int count = mapper.update(userVo);
        if (count > 0) {
            Access ap = new Access();
            this.setWhoForUpdateInfo(userVo);
            ap.setAccessId(userVo.getAccessId());
            ap.setPrimaryPositionId(userVo.getUserPositionId());
            count += accessBiz.updatePrimaryPosition(ap);
        }
        return count;
    }

    public Integer deleteUser(UserParamVo userVo) {
        // 各应用不允许删除用户，删除操作改为不可访问当前应用
        AccessParamVo param = new AccessParamVo();
        if (userVo.getApplication() == null) {
            param.setApplication(userVo.getProfile().get__app());
        }
        else {
            param.setApplication(userVo.getApplication());
        }
        param.setUserIds(userVo.getIds());
        List<AccessParam> accessesList = accessBiz.selectListAccess(param);
        return accessBiz.deleteAccessByAccessId(accessesList);
    }

    @DS("slave")
    public List<UserVo> listAccess(AccessParamVo param) {
        List<UserVo> ret = mapper.listAccess(param);
        return ret;
    }

    @DS("slave")
    public int countAccess(AccessParamVo param) {
        return mapper.countAccess(param);
    }

    public List<User> findUser(UserParam param) {
        return mapper.findUser(param);
    }
}
