package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.ResourceMapper;
import com.midea.isc.admin.model.Resource;
import com.midea.isc.admin.param.ResourceParam;
import com.midea.isc.admin.param.ResourceRoleParam;
import com.midea.isc.admin.vo.MenuParamVo;
import com.midea.isc.admin.vo.ResourceParamVo;
import com.midea.isc.admin.vo.ResourceVo;
import com.midea.isc.common.annotation.DS;
import com.midea.isc.common.biz.BaseBiz;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceBiz extends BaseBiz<ResourceMapper,Resource,ResourceParam> {

    @DS("slave")
    public List<ResourceVo> listWithFlags(ResourceParamVo vo){
        return mapper.listWithFlags(vo);
    }
}

