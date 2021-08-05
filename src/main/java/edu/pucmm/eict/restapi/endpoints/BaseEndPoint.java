package edu.pucmm.eict.restapi.endpoints;

import io.javalin.Javalin;

public abstract class BaseEndPoint {
    protected final Javalin app;

    public BaseEndPoint(Javalin app) {
        this.app = app;
    }

    public abstract void applyRoutes();
}
