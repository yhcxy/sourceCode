package com.midea.isc.api.biz;

import com.midea.isc.api.mapper.ViewFilterMapper;
import com.midea.isc.api.mapper.ViewLayoutMapper;
import com.midea.isc.api.mapper.ViewMapper;
import com.midea.isc.api.model.Application;
import com.midea.isc.api.model.View;
import com.midea.isc.api.param.ViewParam;
import com.midea.isc.api.vo.ViewVo;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ViewBiz extends BaseBiz<ViewMapper,View,ViewParam> {

    @Autowired
    private ViewFilterMapper filterMapper;

    @Autowired
    private ViewLayoutMapper layoutMapper;

    @Autowired
    private ApplicationBiz applicationBiz;

    /**
     * 获取一个view带filter和layout
     * @param view
     * @return
     */
    public ViewVo getOne(View view){
        return mapper.getOne(view);
    }

    /**
     * 新增一个view
     * @param view
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertOne(ViewVo view) throws IscException {
        ViewParam param = new ViewParam();
        param.setPageId(view.getPageId());
        param.setGridId(view.getGridId());
        param.setViewName(view.getViewName());
        param.setApplication(view.getApplication());
        super.setWho(view);
        view.setLoginId(view.getCreatedBy());

        int count= mapper.selectCount(param);
        if(0 != count){
            throw new IscException("ISC-04442");
        }

        if("Y".equals(view.getDefaultFlag())){
            param.setViewName(null);
            param.setLoginId(view.getProfile().get__userName());
            param.setDefaultFlag("Y");

            count= mapper.selectCount(param);

            if(0 != count){
                throw new IscException("ISC-04442");
            }
        }

        super.setWho(view);
        mapper.insert(view);

        if(view.getFilters().size() > 0){
            view.getFilters().forEach(o -> {
                o.setViewId(view.getViewId());
                o.setCreatedBy(view.getCreatedBy());
                o.setLastUpdatedBy(view.getCreatedBy());
            });

            filterMapper.insertBulk(view.getFilters());
        }

        if(view.getLayouts().size() > 0){
            view.getLayouts().forEach(o -> {
                o.setViewId(view.getViewId());
                o.setCreatedBy(view.getCreatedBy());
                o.setLastUpdatedBy(view.getCreatedBy());
            });

            layoutMapper.insertBulk(view.getLayouts());
        }
    }

    /**
     * update一个view
     * @param view
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOne(ViewVo view) throws IscException {
        ViewParam param = new ViewParam();
        param.setViewName(view.getViewName());
        param.setLoginId(view.getProfile().get__userName());
        param.setPageId(view.getPageId());
        param.setGridId(view.getGridId());
        param.setViewId(view.getViewId());
        param.setApplication(view.getApplication());
        int count = mapper.selectCount(param);
        if(1 != count){
            throw new IscException("ISC-04442");
        }
        super.setWhoForUpdateInfo(view);
        mapper.update(view);

        this.setWho(view);
        filterMapper.deleteBulk(view.getFilters());
        layoutMapper.deleteBulk(view.getLayouts());

        view.getLayouts().forEach(o -> {
            o.setViewId(view.getViewId());
            o.setCreatedBy(view.getCreatedBy());
            o.setLastUpdatedBy(view.getCreatedBy());
        });

        view.getFilters().forEach(o -> {
            o.setViewId(view.getViewId());
            o.setCreatedBy(view.getCreatedBy());
            o.setLastUpdatedBy(view.getCreatedBy());
        });

        layoutMapper.insertBulk(view.getLayouts());
        filterMapper.insertBulk(view.getFilters());
    }

    /**
     * 设置who
     */
    private void setWho(ViewVo view){
        if(null != view.getFilters() && view.getFilters().size() > 0){
            view.getFilters().forEach(o -> {
                o.setCreatedBy(view.getCreatedBy());
                o.setLastUpdatedBy(view.getCreatedBy());
            });
        }

        if(view.getLayouts().size() > 0){
            view.getLayouts().forEach(o -> {
                o.setCreatedBy(view.getCreatedBy());
                o.setLastUpdatedBy(view.getCreatedBy());
            });
        }
    }
}

