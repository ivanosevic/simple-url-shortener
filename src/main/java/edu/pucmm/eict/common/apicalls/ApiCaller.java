package edu.pucmm.eict.common.apicalls;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

public class ApiCaller {

    private static ApiCaller instance;
    private final Client client;
    private final String IP_API = "http://ip-api.com/json";

    private ApiCaller() {
        client = ClientBuilder.newClient();
    }

    public static ApiCaller getInstance() {
        if (instance == null) {
            instance = new ApiCaller();
        }
        return instance;
    }

    public IpApiResponse getGeoStats(String ip) {
        String url = IP_API + "/" + ip;
        Response response = client.target(url).request().get();
        return response.readEntity(IpApiResponse.class);
    }
}