package com.midea.isc.admin.biz;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import com.midea.isc.admin.model.Application;
import com.midea.isc.admin.model.ClientService;
import com.midea.isc.admin.model.GateClient;
import com.midea.isc.admin.param.ApplicationParam;
import com.midea.isc.admin.param.GateClientParam;
import com.midea.isc.admin.vo.AuthClientParamVo;
import com.midea.isc.admin.vo.GateClientParamVo;
import com.midea.isc.admin.mapper.GateClientMapper;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.util.UUIDUtils;

@Service
public class GateClientBiz extends BaseBiz<GateClientMapper, GateClient, GateClientParam> {
    @Autowired
    private DiscoveryClient discovery;

    @Autowired
    ApplicationBiz appBiz;

    @Autowired
    ClientServiceBiz serviceBiz;

    /**
     * @Description 服务发现
     * @author WANGXK7
     * @date 2019年5月29日 上午10:56:09
     * @param param
     * @return
     * @lastModifier
     */
    public int discoveryService(GateClientParam param) {
        int count = 0;
        List<String> serviceList = discovery.getServices();
        for (String service : serviceList) {
            GateClientParam clientParam = new GateClientParam();
            clientParam.setName(service);
            clientParam.setCode(service);
            GateClient dbClient = mapper.selectOne(clientParam);
            if (dbClient == null) {
                clientParam.setProfile(param.getProfile());
                clientParam.setSecret(UUIDUtils.generateShortUuid());
                clientParam.setLocked("N");
                clientParam.setAuthFlag("Y");
                clientParam.setAppShareFlag("Y");
                String[] nameStr = service.split("-");
                if (nameStr.length > 1) {
                    clientParam.setApplication(nameStr[0]);
                    clientParam.setModule(nameStr[1]);
                }
                else {
                    clientParam.setApplication(service);
                }
                count += this.insert(clientParam);
            }
        }

        return count;
    }

    public List<GateClient> getAppClient(GateClientParamVo param) {
        if (setClientParam(param)) {
            return mapper.getAppClient(param);
        }
        else {
            return new ArrayList<GateClient>();
        }

    }

    public int totalAppClient(GateClientParamVo param) {
        if (setClientParam(param)) {
            return mapper.totalAppClient(param);
        }
        else {
            return 0;
        }
    }

    private boolean setClientParam(GateClientParamVo param) {
        ApplicationParam appParam = new ApplicationParam();
        appParam.setProfile(param.getProfile());
        List<Application> appList = appBiz.getAccessibleApp(appParam);
        if (appList != null && !appList.isEmpty()) {
            List<String> apps = new ArrayList<>();
            for (Application app : appList) {
                apps.add(app.getApplication());
            }
            param.setAppList(apps);

            return true;
        }

        return false;
    }

    public AuthClientParamVo initAuth(GateClientParam param) {
        AuthClientParamVo result = new AuthClientParamVo();
        result.setAuthClient(mapper.getAuthClient(param));
        result.setAllowedClient(mapper.getAllowClient(param));

        return result;
    }

    public int updateAuth(AuthClientParamVo param) {
        ClientService service = new ClientService();
        service.setServiceId(param.getClientId() + "");
        serviceBiz.delete(service);
        int count = 0;
        List<GateClient> clientList = param.getAllowedClient();
        if (clientList != null && !clientList.isEmpty()) {
            List<ClientService> serviceList = new ArrayList<>();
            for (GateClient client : clientList) {
                ClientService clientService = new ClientService();
                clientService.setProfile(param.getProfile());
                clientService.setClientId(client.getClientId() + "");
                clientService.setServiceId(param.getClientId() + "");
                serviceList.add(clientService);
            }
            count = serviceBiz.insertBulk(serviceList);
        }

        return count;
    }
}
