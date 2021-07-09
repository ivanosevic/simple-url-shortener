package edu.pucmm.eict.common;

import io.javalin.Javalin;

public abstract class Controller {

    protected Javalin app;

    public Controller(Javalin app) {
        this.app = app;
    }

    public abstract void applyRoutes();
}