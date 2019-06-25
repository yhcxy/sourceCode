package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.FtpTaskMapper;
import com.midea.isc.admin.model.FtpConfig;
import com.midea.isc.admin.model.FtpTask;
import com.midea.isc.admin.param.ApplicationParam;
import com.midea.isc.admin.param.FtpTaskParam;
import com.midea.isc.admin.vo.FtpTaskParamVo;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FtpTaskBiz extends BaseBiz<FtpTaskMapper,FtpTask,FtpTaskParam> {
    @Autowired
    private ApplicationBiz applicationBiz;
    /**
     * 判断逻辑的插入
     * @param param
     */
    public void insertOne(FtpTaskParam param) throws Exception{
        applicationBiz.hasPermission(param.getProfile(), param.getApplication());

        FtpTaskParam tmp = new FtpTaskParam();
        tmp.setApplication(param.getApplication());
        tmp.setCode(param.getCode());

        int count = mapper.selectCount(tmp);

        if(count > 0){
            throw new IscException("FTP-001");
        }

        super.setWho(param);
        mapper.insert(param);
    }

    /**
     * 判断逻辑的更新
     * @param param
     */
    public void updateOne(FtpTaskParam param) throws Exception{
        applicationBiz.hasPermission(param.getProfile(), param.getApplication());
        FtpTaskParam tmp = new FtpTaskParam();
        tmp.setApplication(param.getApplication());
        tmp.setCode(param.getCode());

        FtpTask task = mapper.selectOne(tmp);

        if(null != task && task.getFtpTaskId() != param.getFtpTaskId()){
            throw new IscException("FTP-002");
        }

        super.setWhoForUpdateInfo(param);
        mapper.update(param);
    }

    /**
     * 启用/禁用
     * @param param
     * @throws Exception
     */
    public void enable(FtpTaskParam param)throws Exception{
        applicationBiz.hasPermission(param.getProfile(), param.getApplication());
        super.setWhoForUpdateInfo(param);
        mapper.enable(param);
    }

    public List<FtpTask> list(FtpTaskParamVo param)throws Exception{
        ApplicationParam p = new ApplicationParam();
        p.setProfile(param.getProfile());
        List<String> apps = applicationBiz.getAccessibleAppString(p);
        param.setApps(apps);
        return mapper.list(param);
    }

    public int count(FtpTaskParamVo param)throws Exception{
        ApplicationParam p = new ApplicationParam();
        p.setProfile(param.getProfile());
        List<String> apps = applicationBiz.getAccessibleAppString(p);
        param.setApps(apps);
        return mapper.count(param);
    }
}

