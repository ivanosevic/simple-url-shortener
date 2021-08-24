package edu.pucmm.eict.webapp.controllers;

import io.javalin.Javalin;

public abstract class BaseRoute {
    protected final Javalin app;

    public BaseRoute(Javalin app) {
        this.app = app;
    }

    public abstract void applyRoutes();
}
