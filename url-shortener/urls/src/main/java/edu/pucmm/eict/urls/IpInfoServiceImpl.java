package edu.pucmm.eict.urls;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

public class IpInfoServiceImpl implements IpInfoService {
    private final Client client;
    private final String URI = "http://ip-api.com/json";

    @Inject
    public IpInfoServiceImpl(Client client) {
        this.client = client;
    }

    public String getUriForIp(String ip) {
        return URI + "/" + ip;
    }

    public IpInfo getIpInfo(String ip) {
        String url = getUriForIp(ip);
        Response response = client.target(url).request().get();
        return response.readEntity(IpInfo.class);
    }
}
