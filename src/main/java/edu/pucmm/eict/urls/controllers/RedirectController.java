package edu.pucmm.eict.urls.controllers;

import edu.pucmm.eict.common.Controller;
import edu.pucmm.eict.urls.services.ReferrerService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class RedirectController extends Controller {

    private final ReferrerService referrerService;

    public RedirectController(Javalin app) {
        super(app);
        referrerService = ReferrerService.getInstance();
    }

    private void performRedirect(Context ctx) {
        String code = ctx.pathParam("code", String.class).get();
        var shortURL = referrerService.newReferrer(code, ctx.req.getRemoteAddr(), ctx.userAgent());
        ctx.redirect(shortURL.getToURL());
    }

    @Override
    public void applyRoutes() {
        app.get("/r/:code", this::performRedirect);
    }
}