package edu.pucmm.eict.common;

import io.javalin.Javalin;

public class StaticFileController extends Controller{

    public StaticFileController(Javalin app) {
        super(app);
    }

    @Override
    public void applyRoutes() {
        app.get("/terms-of-service", ctx -> ctx.render("templates/terms-of-service.vm"));
    }
}
