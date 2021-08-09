package edu.pucmm.eict.urlshortener.webapp.controllers;

import io.javalin.Javalin;

public class AdviceController extends BaseController{

    public AdviceController(Javalin app) {
        super(app);
    }

    @Override
    public void applyRoutes() {
        app.error(401, ctx -> ctx.render("401.html"));
        app.error(404, ctx -> ctx.render("404.html"));
        app.error(500, ctx -> ctx.render("500.html"));
    }
}
