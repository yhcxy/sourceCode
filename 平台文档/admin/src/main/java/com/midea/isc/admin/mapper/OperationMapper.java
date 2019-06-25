package com.midea.isc.admin.mapper;

import com.midea.isc.admin.model.Operation;
import com.midea.isc.admin.param.OperationParam;
import com.midea.isc.admin.vo.OperationParamVo;
import com.midea.isc.admin.vo.OperationVo;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface OperationMapper extends BaseMapper<Operation, OperationParam>{
    int insertPath(OperationParam param);

    /**
     * 删除app对应的Operation
     * @param app
     * @return
     */
    int clearOperation(String app);

    int countOperation(OperationParamVo param);

    List<OperationVo> selectAllOperations(OperationParam param);

    void deleteBatch(List<Operation> operations);
}

