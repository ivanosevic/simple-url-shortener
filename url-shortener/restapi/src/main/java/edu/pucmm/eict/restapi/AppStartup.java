package edu.pucmm.eict.restapi;

import edu.pucmm.eict.restapi.endpoints.AuthEndpoint;
import edu.pucmm.eict.restapi.endpoints.ShortUrlEndpoint;
import io.javalin.Javalin;

import javax.inject.Inject;
import javax.inject.Named;

public class AppStartup implements Startup {

    private final Javalin app;
    private final Integer port;
    private final AuthEndpoint authEndpoint;
    private final ShortUrlEndpoint shortUrlEndpoint;

    @Inject
    public AppStartup(Javalin app, @Named("apiPort") Integer port, AuthEndpoint authEndpoint, ShortUrlEndpoint shortUrlEndpoint) {
        this.app = app;
        this.port = port;
        this.authEndpoint = authEndpoint;
        this.shortUrlEndpoint = shortUrlEndpoint;
    }

    @Override
    public void start(String[] args) {
        authEndpoint.applyEndpoints();
        shortUrlEndpoint.applyEndpoints();
        app.start(port);
    }
}
