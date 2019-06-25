package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.OperationMapper;
import com.midea.isc.admin.model.Operation;
import com.midea.isc.admin.param.OperationParam;
import com.midea.isc.admin.param.OprPathParam;
import com.midea.isc.admin.vo.MenuParamVo;
import com.midea.isc.admin.vo.OperationParamVo;
import com.midea.isc.admin.vo.OperationVo;
import com.midea.isc.common.annotation.DS;
import com.midea.isc.common.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OperationBiz extends BaseBiz<OperationMapper,Operation,OperationParam> {

    @Autowired
    private OprPathBiz oprPathBiz;

    @Transactional
    public int insertOperation(MenuParamVo param){
        int count = 0;
        List<OperationParamVo> operations = param.getOperationParams();
        for (OperationParamVo operation : operations) {
            operation.setMenuId(param.getMenuId());// menu 必须先插入
            operation.setApplication(param.getApplication());
            operation.setProfile(param.getProfile());
            this.setWho(operation);
            mapper.insert(operation);

            if (operation.getPaths() != null) {
                for (OprPathParam path : operation.getPaths()) {
                    path.setMenuId(param.getMenuId());
                    path.setCode(operation.getCode());
                    path.setApplication(operation.getApplication());
                    oprPathBiz.insert(path);
                }
            }

            count++;
        }

        return count;
    }

    /**
     * 删除app对应的Operation
     * @param app
     * @return
     */
    public int clearOperation(String app){
        return mapper.clearOperation(app);
    }


    @DS("migration")
    public int countOperationMigration(OperationParamVo param){
        return mapper.countOperation(param);
    }

    @DS("migration")
    public int deleteMigration(OperationParam param){
        return mapper.delete(param);
    }

    @DS("slave")
    public List<OperationVo> selectAllOperations(OperationParam param){
        return mapper.selectAllOperations(param);
    }
    
    public void deleteBatch(List<Operation> operations){
        mapper.deleteBatch(operations);
    }

}

