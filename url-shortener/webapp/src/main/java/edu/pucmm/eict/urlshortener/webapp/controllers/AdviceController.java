package edu.pucmm.eict.urlshortener.webapp.controllers;

import edu.pucmm.eict.urlshortener.persistence.PaginationErrorException;
import edu.pucmm.eict.urlshortener.users.UserAlreadyExistsException;
import io.javalin.Javalin;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

public class AdviceController extends BaseController{

    public AdviceController(Javalin app) {
        super(app);
    }

    @Override
    public void applyRoutes() {
        app.exception(PaginationErrorException.class, (e, ctx) -> ctx.status(404));
        app.exception(EntityNotFoundException.class, (e, ctx) -> ctx.status(404));
        app.exception(EntityExistsException.class, (e, ctx) -> ctx.status(500));
        app.exception(UserAlreadyExistsException.class, (e, ctx) -> ctx.status(500));
        app.error(401, ctx -> ctx.render("401.html"));
        app.error(404, ctx -> ctx.render("404.html"));
        app.error(500, ctx -> ctx.render("500.html"));
    }
}
