package edu.pucmm.eict.urlshortener.urls;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

public class IpInfoService {
    private final Client client;
    private final String URI = "http://ip-api.com/json";

    public IpInfoService(Client client) {
        this.client = client;
    }

    public String getUriForIp(String ip) {
        return URI + "/" + ip;
    }

    public IpInfo getInfoFromIp(String ip) {
        String url = getUriForIp(ip);
        Response response = client.target(url).request().get();
        return response.readEntity(IpInfo.class);
    }
}