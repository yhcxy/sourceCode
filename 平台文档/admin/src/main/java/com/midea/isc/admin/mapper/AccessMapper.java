package com.midea.isc.admin.mapper;

import com.midea.isc.admin.model.Access;
import com.midea.isc.admin.param.AccessParam;
import com.midea.isc.admin.vo.AccessParamVo;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface AccessMapper extends BaseMapper<Access, AccessParam>{

    List<AccessParam> selectListAccess(AccessParamVo param);

    Integer deleteAccessByAccessId(List<AccessParam> accessesList);

    void deleteByUserId(AccessParamVo param);

    void insertByApp(AccessParamVo param);

    Integer updatePrimaryPosition(Access access);
}

