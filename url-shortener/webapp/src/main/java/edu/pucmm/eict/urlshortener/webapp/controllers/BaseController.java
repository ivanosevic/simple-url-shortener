package edu.pucmm.eict.urlshortener.webapp.controllers;

import io.javalin.Javalin;

public abstract class BaseController {
    protected final Javalin app;

    public BaseController(Javalin app) {
        this.app = app;
    }

    public abstract void applyRoutes();
}
