package edu.pucmm.eict.webapp.configuration;

import io.javalin.http.Context;

public class SessionFlash {

    public <T> void add(String name, T value, Context ctx) {
        ctx.sessionAttribute(name, value);
    }

    public <T> T get(String name, Context ctx) {
        T value = ctx.sessionAttribute(name);
        ctx.req.getSession().removeAttribute(name);
        return value;
    }
}
