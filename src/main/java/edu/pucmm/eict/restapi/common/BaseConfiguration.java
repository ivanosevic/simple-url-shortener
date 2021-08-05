package edu.pucmm.eict.restapi.common;

import io.javalin.Javalin;

public abstract class BaseConfiguration {

    protected final Javalin app;

    public BaseConfiguration(Javalin app) {
        this.app = app;
    }

    public abstract void applyConfiguration();
}
