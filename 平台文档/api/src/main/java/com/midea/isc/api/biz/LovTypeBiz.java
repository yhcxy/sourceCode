package com.midea.isc.api.biz;

import com.midea.isc.api.mapper.LovTypeMapper;
import com.midea.isc.api.mapper.LovValueMapper;
import com.midea.isc.api.model.LovType;
import com.midea.isc.api.param.LovTypeParam;
import com.midea.isc.api.vo.LovTypeParamVo;
import com.midea.isc.api.vo.LovValueParamVo;
import com.midea.isc.common.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class LovTypeBiz extends BaseBiz<LovTypeMapper,LovType,LovTypeParam> {
    @Autowired
    private LovValueBiz lovValueBiz;

    @Autowired
    private LovValueMapper lovValueMapper;

    private static ReentrantLock lock;
    private static Queue<Condition> queue = new LinkedList<>();
    private static int runningCount = 0;

    @Transactional
    public void synchronize(LovTypeParamVo vo){
        vo.setEnable("Y");
        this.insert(vo);

        if(null != vo.getChildren() && !vo.getChildren().isEmpty()){
            for(LovTypeParamVo c : vo.getChildren()){
                c.setEnable("Y");
                c.setParentId(vo.getTypeId());
                c.setParentCode(vo.getCode());
                c.setProfile(vo.getProfile());
                c.setApplication(vo.getApplication());
                this.synchronize(c);
            }
        }

        if(null != vo.getValues() && !vo.getValues().isEmpty()){
            for(LovValueParamVo c : vo.getValues()){
                c.setEnable("Y");
                c.setTypeId(vo.getTypeId());
                c.setTypeCode(vo.getCode());
                this.synchronizeValue(c);
            }
        }
    }

    @Transactional
    public void synchronizeValue(LovValueParamVo vo){
        lovValueBiz.insert4Synchronize(vo);
        if(null != vo.getChildren() && !vo.getChildren().isEmpty()){
            for(LovValueParamVo c : vo.getChildren()){
                c.setTypeCode(vo.getTypeCode());
                c.setTypeId(vo.getTypeId());
                c.setParentId(vo.getValueId());
                c.setParentLabel(vo.getParentLabel());
                c.setProfile(vo.getProfile());
                c.setParentValue(vo.getValue());
                this.synchronizeValue(c);
            }
        }
    }

    @Transactional
    public void synchronizeOne(LovTypeParamVo vo) throws InterruptedException {
        lock.lock();
        LovTypeBiz.runningCount++;
        if(LovTypeBiz.runningCount > 0){
            Condition condition = lock.newCondition();
            LovTypeBiz.queue.add(condition);
            condition.await();
        }

        LovTypeParam p =  new LovTypeParam();
        p.setCode(vo.getCode());
        LovType type = mapper.selectOne(p);
        Integer typeId = null;

        if(null == type){
            vo.setEnable("Y");
            this.insert(vo);

            typeId = vo.getTypeId();
        }else {
            typeId = type.getTypeId();
        }

        if(null != vo.getValue()){
            vo.getValue().setTypeCode(vo.getCode());
            vo.getValue().setTypeId(typeId);
            lovValueMapper.insert(vo.getValue());
        }

        if( LovTypeBiz.queue.size() > 0)
            LovTypeBiz.queue.poll().signal();
        lock.unlock();
    }

}

