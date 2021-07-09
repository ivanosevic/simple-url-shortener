package edu.pucmm.eict.common;

import io.javalin.Javalin;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

public class ErrorController extends Controller {

    public ErrorController(Javalin app) {
        super(app);
    }

    @Override
    public void applyRoutes() {
        app.exception(EntityNotFoundException.class, (exception, ctx) -> {
            ctx.status(404);
        });

        app.exception(EntityExistsException.class, (exception, ctx) -> {
            ctx.status(500);
        });

        app.error(404, ctx -> {
            ctx.render("templates/404.vm");
        });

        app.error(500, ctx -> {
            ctx.render("templates/500.vm");
        });

        app.error(401, ctx -> {
            ctx.render("templates/401.vm");
        });
    }
}