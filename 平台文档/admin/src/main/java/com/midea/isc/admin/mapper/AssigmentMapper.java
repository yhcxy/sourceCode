package com.midea.isc.admin.mapper;

import com.midea.isc.admin.model.Assigment;
import com.midea.isc.admin.param.AssigmentParam;
import com.midea.isc.admin.vo.AssigmentParamVO;
import com.midea.isc.admin.vo.AssigmentVO;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface AssigmentMapper extends BaseMapper<Assigment, AssigmentParam>{
    /**
     * 删除app对应的Assigment
     * @param app
     * @return
     */
    int clearAssigment(String app);

    int insertByName(AssigmentParam param);

    List<AssigmentVO> selectAssigmentList(AssigmentParamVO param);

    Integer deleteBatchs(AssigmentParamVO assigmentVO);
}

