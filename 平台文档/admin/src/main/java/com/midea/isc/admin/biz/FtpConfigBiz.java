package com.midea.isc.admin.biz;

import com.midea.isc.admin.mapper.FtpConfigMapper;
import com.midea.isc.admin.model.FtpConfig;
import com.midea.isc.admin.param.ApplicationParam;
import com.midea.isc.admin.param.FtpConfigParam;
import com.midea.isc.admin.vo.FtpConfigParamVo;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FtpConfigBiz extends BaseBiz<FtpConfigMapper,FtpConfig,FtpConfigParam> {

    @Value("${ftp.encodeKey}")
    private String key;

    @Autowired
    private ApplicationBiz applicationBiz;
    /**
     * 判断逻辑的插入
     * @param param
     */
    public void insertOne(FtpConfigParam param) throws Exception{
        applicationBiz.hasPermission(param.getProfile(), param.getApplication());

        FtpConfigParam tmp = new FtpConfigParam();
        tmp.setApplication(param.getApplication());
        tmp.setCode(param.getCode());

        int count = mapper.selectCount(tmp);

        if(count > 0){
            throw new IscException("FTP-001");
        }

        param.setPassword(AESUtil.aesEncrypt(param.getPassword(), this.key));
        super.setWho(param);
        mapper.insert(param);
    }

    /**
     * 判断逻辑的更新
     * @param param
     */
    public void updateOne(FtpConfigParam param) throws Exception{
        applicationBiz.hasPermission(param.getProfile(), param.getApplication());

        FtpConfigParam tmp = new FtpConfigParam();
        tmp.setApplication(param.getApplication());
        tmp.setCode(param.getCode());

        FtpConfig config = mapper.selectOne(tmp);

        if(null != config && config.getFtpConfigId() != param.getFtpConfigId()){
            throw new IscException("FTP-002");
        }

        param.setPassword(config.getPassword());
        super.setWhoForUpdateInfo(param);
        mapper.update(param);
    }

    public void resetPassword(FtpConfigParamVo param)throws Exception{
        applicationBiz.hasPermission(param.getProfile(), param.getApplication());

        FtpConfigParam tmp = new FtpConfigParam();
        tmp.setFtpConfigId(param.getFtpConfigId());
        FtpConfig config = mapper.selectOne(tmp);

        if(null == config){
            throw new IscException("FTP-002");
        }

        param.setPassword(AESUtil.aesEncrypt(param.getPassword(), this.key));

        if(!config.getPassword().equals(AESUtil.aesEncrypt(param.getOldPassword(), this.key))){
            throw new IscException("FTP-003");
        }

        super.setWhoForUpdateInfo(param);
        mapper.resetPwd(param);
    }

    /**
     * 启用/禁用
     * @param param
     * @throws Exception
     */
    public void enable(FtpConfigParam param)throws Exception{
        applicationBiz.hasPermission(param.getProfile(), param.getApplication());
        super.setWhoForUpdateInfo(param);
        mapper.enable(param);
    }

    public List<FtpConfig> list(FtpConfigParamVo param)throws Exception{
        ApplicationParam p = new ApplicationParam();
        p.setProfile(param.getProfile());
       List<String> apps = applicationBiz.getAccessibleAppString(p);
        param.setApps(apps);
        return mapper.list(param);
    }

    public int count(FtpConfigParamVo param)throws Exception{
        ApplicationParam p = new ApplicationParam();
        p.setProfile(param.getProfile());
        List<String> apps = applicationBiz.getAccessibleAppString(p);
        param.setApps(apps);
        return mapper.count(param);
    }

    /**
     * 刷新ftp服务的配置
     * @param param
     * @throws Exception
     */
    public void refresh(FtpConfigParam param)throws Exception{
        applicationBiz.hasPermission(param.getProfile(), param.getApplication());
    }
}

