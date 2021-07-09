package edu.pucmm.eict.common;

import io.javalin.Javalin;

public abstract class JavalinConfig {

    protected Javalin app;

    public JavalinConfig(Javalin app) {
        this.app = app;
    }

    public abstract void applyConfig();
}
