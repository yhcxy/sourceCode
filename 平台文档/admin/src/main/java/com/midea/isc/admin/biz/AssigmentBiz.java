package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.AssigmentMapper;
import com.midea.isc.admin.model.Assigment;
import com.midea.isc.admin.param.AssigmentParam;
import com.midea.isc.admin.vo.AssigmentParamVO;
import com.midea.isc.admin.vo.AssigmentVO;
import com.midea.isc.common.annotation.DS;
import com.midea.isc.common.biz.BaseBiz;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssigmentBiz extends BaseBiz<AssigmentMapper,Assigment,AssigmentParam> {
    /**
     * 删除app对应的Assigment
     * @param app
     * @return
     */
    public int clearAssigment(String app){
        return mapper.clearAssigment(app);
    }

    @DS("migration")
    public List<Assigment> selectListMigration(AssigmentParam param){
        return mapper.selectList(param);
    }

    public int insertByName(AssigmentParamVO param) {
        this.setWho(param);
        int count = mapper.insertByName(param);
        return count;
    }

    public List<AssigmentVO> selectAssigmentList(AssigmentParamVO param) {
        return mapper.selectAssigmentList(param);
    }

    public Integer deleteBatchs(AssigmentParamVO assigmentVO) {
        return mapper.deleteBatchs(assigmentVO);
    }
}

