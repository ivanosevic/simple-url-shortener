package edu.pucmm.eict.urlshortener.webapp.controllers;

import io.javalin.Javalin;

public abstract class BaseController {
    protected final Javalin app;

    protected BaseController(Javalin app) {
        this.app = app;
    }

    public abstract void applyRoutes();
}
