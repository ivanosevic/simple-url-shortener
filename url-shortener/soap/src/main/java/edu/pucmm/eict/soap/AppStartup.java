package edu.pucmm.eict.soap;

import edu.pucmm.eict.soap.controllers.ShortUrlController;
import io.javalin.Javalin;

import javax.inject.Inject;
import javax.inject.Named;

public class AppStartup implements Startup {

    private final Javalin app;
    private final Integer port;
    private final ShortUrlController shortUrlController;

    @Inject
    public AppStartup(Javalin app, @Named("webservicePort") Integer port, ShortUrlController shortUrlController) {
        this.app = app;
        this.port = port;
        this.shortUrlController = shortUrlController;
    }

    @Override
    public void start(String[] args) {
        shortUrlController.applyRoutes();
        app.start(port);
    }
}
