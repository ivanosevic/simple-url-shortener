package edu.pucmm.eict.restapi.endpoints;

import io.javalin.Javalin;

public abstract class BaseEndpoint {
    protected final Javalin app;

    public BaseEndpoint(Javalin app) {
        this.app = app;
    }

    public abstract void applyEndpoints();
}
