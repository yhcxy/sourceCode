package com.midea.isc.auth.server.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.ServiceMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.midea.isc.auth.server.model.GateClient;
import com.midea.isc.auth.server.param.GateClientParam;
import com.midea.isc.auth.server.util.ClientTokenUtil;
import com.midea.isc.auth.server.vo.ClientInfo;
import com.midea.isc.auth.common.event.AuthRemoteEvent;
import com.midea.isc.auth.common.vo.ServiceAuthority;
import com.midea.isc.auth.server.mapper.GateClientMapper;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;

import java.util.ArrayList;
import java.util.List;

@Service
public class GateClientBiz extends BaseBiz<GateClientMapper, GateClient, GateClientParam> {

    @Autowired
    private ClientTokenUtil clientTokenUtil;

    @Autowired
    private ServiceMatcher serviceMatcher;

    private ApplicationContext context;

    @Autowired
    public GateClientBiz(ApplicationContext context) {
        this.context = context;
    }

    public String apply(String clientId, String secret) throws Exception {
        GateClientParam param = new GateClientParam();
        param.setCode(clientId);
        param.setSecret(secret);
        GateClient client = mapper.selectOne(param);
        if (client == null) {
            throw new IscException("ISC-918", "Clinet not registered!");
        }
        else {
            return clientTokenUtil
                    .generateToken(new ClientInfo(client.getCode(), client.getName(), client.getClientId().toString()));
        }
    }

    private GateClient getClient(String clientId, String secret) throws IscException {
        GateClient client = new GateClient();
        GateClientParam param = new GateClientParam();
        param.setCode(clientId);
        client = mapper.selectOne(param);
        if (client == null || !client.getSecret().equals(secret)) {
            throw new IscException("ISC-905", "Client not found or Client secret is error!");
        }
        return client;
    }

    public void validate(String clientId, String secret) throws Exception {
        GateClientParam param = new GateClientParam();
        param.setCode(clientId);
        GateClient client = mapper.selectOne(param);
        if (client == null || !client.getSecret().equals(secret)) {
            throw new IscException("ISC-905", "Client not found or Client secret is error!");
        }
    }

    public boolean lockService(GateClientParam param) {
        this.setWhoForUpdateInfo(param);
        int lockResult = mapper.lockService(param);
        if (lockResult > 0) {
            pushAuthority(param);
            return true;
        }
        else {
            return false;
        }
    }

    public void pushAuthority(GateClientParam param) {
        GateClient client = this.selectOne(param);
        String myUniqueId = serviceMatcher.getServiceId();// context.getId();
        AuthRemoteEvent event;
        if (client.getLocked().equals("Y")) {
            List<String> clients = new ArrayList<>();
            event = new AuthRemoteEvent(this, myUniqueId, client.getCode(), clients, true);
        }
        else {
            if (client.getAuthFlag().equals("N")) {
                List<String> clients = new ArrayList<>();
                event = new AuthRemoteEvent(this, myUniqueId, client.getCode(), clients, false);
            }
            else {
                List<String> clients = mapper.selectAllowedClient(client.getClientId() + "");
                if (client.getAppShareFlag().equals("Y")) {
                    GateClientParam newParam = new GateClientParam();
                    newParam.setApplicationCond("eq");
                    newParam.setApplication(client.getApplication());
                    newParam.setClientIdCond("ne");
                    newParam.setClientId(client.getClientId());
                    List<GateClient> appclients = this.find(newParam);
                    if (clients == null) {
                        clients = new ArrayList<>();
                    }
                    for (GateClient appClient : appclients) {
                        clients.add(appClient.getCode());
                    }
                }
                event = new AuthRemoteEvent(this, myUniqueId, client.getCode(), clients, true);
            }
        }
        context.publishEvent(event);
    }

    public ServiceAuthority getServiceAuthority(String clientId, String secret) throws IscException {
        ServiceAuthority result = new ServiceAuthority();
        GateClient client = this.getClient(clientId, secret);
        if (client.getLocked().equals("N")) {
            if (client.getAuthFlag().equals("N")) {
                result.setNeedAuth(false);
                result.setAllowedClient(new ArrayList<>());
            }
            else {
                List<String> clients = mapper.selectAllowedClient(client.getClientId() + "");
                if (client.getAppShareFlag().equals("Y")) {
                    GateClientParam newParam = new GateClientParam();
                    newParam.setApplicationCond("eq");
                    newParam.setApplication(client.getApplication());
                    newParam.setClientIdCond("ne");
                    newParam.setClientId(client.getClientId());
                    List<GateClient> appclients = this.find(newParam);
                    if (clients == null) {
                        clients = new ArrayList<>();
                    }
                    for (GateClient appClient : appclients) {
                        clients.add(appClient.getCode());
                    }
                }
                result.setNeedAuth(true);
                result.setAllowedClient(clients);
            }
        }
        else {
            result.setNeedAuth(true);
            result.setAllowedClient(new ArrayList<>());
        }

        return result;
    }
}
