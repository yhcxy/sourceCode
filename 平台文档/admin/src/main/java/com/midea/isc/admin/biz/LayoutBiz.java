package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.LayoutMapper;
import com.midea.isc.admin.model.Layout;
import com.midea.isc.admin.param.LayoutParam;
import com.midea.isc.admin.vo.LayoutParamVo;
import com.midea.isc.admin.vo.LayoutVo;
import com.midea.isc.common.annotation.DS;
import com.midea.isc.common.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LayoutBiz extends BaseBiz<LayoutMapper,Layout,LayoutParam> {

    @Autowired
    private LayoutDetailBiz layoutDetailBiz;

    @DS("slave")
    public LayoutVo getVo(LayoutParam param){
        return mapper.getVo(param);
    }

    @Transactional(rollbackFor = Exception.class)
	public void save(LayoutParamVo vo){
        super.setWho(vo);
        layoutDetailBiz.delete(vo);
        mapper.deleteVo(vo);

        mapper.insert(vo);
        layoutDetailBiz.insert(vo);
    }
}

