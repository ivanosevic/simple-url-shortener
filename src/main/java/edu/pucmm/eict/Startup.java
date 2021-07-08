package edu.pucmm.eict;

import edu.pucmm.eict.bootstrap.DataBootstrap;
import edu.pucmm.eict.common.AppProperties;
import io.javalin.Javalin;

import javax.inject.Inject;

public class Startup {

    private final AppProperties appProperties;
    private final Javalin app;
    private final DataBootstrap dataBootstrap;

    @Inject
    public Startup(AppProperties appProperties, Javalin app, DataBootstrap dataBootstrap) {
        this.appProperties = appProperties;
        this.app = app;
        this.dataBootstrap = dataBootstrap;
    }

    public void boot(String[] args) {
        int port = appProperties.getPort();
        dataBootstrap.inserts();
        app.start(port);
    }
}