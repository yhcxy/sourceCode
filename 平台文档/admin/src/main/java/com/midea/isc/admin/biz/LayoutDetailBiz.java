package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.LayoutDetailMapper;
import com.midea.isc.admin.model.LayoutDetail;
import com.midea.isc.admin.param.LayoutDetailParam;
import com.midea.isc.admin.vo.LayoutParamVo;
import com.midea.isc.admin.vo.LayoutVo;
import com.midea.isc.common.biz.BaseBiz;
import org.springframework.stereotype.Service;

@Service
public class LayoutDetailBiz extends BaseBiz<LayoutDetailMapper,LayoutDetail,LayoutDetailParam> {
    public void delete(LayoutParamVo vo){
        mapper.deleteBatch(vo);
    }

    public void insert(LayoutParamVo vo){

        vo.getDetails().forEach(o -> {
            o.setProfile(vo.getProfile());
            o.setLayoutId(vo.getLayoutId());
            super.setWho(o);
        });
        mapper.insertBatch(vo);
    }
}

