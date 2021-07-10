package edu.pucmm.eict.urls;

import edu.pucmm.eict.common.Controller;
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
        var shortenedUrl = referrerService.newReferrer(code, ctx.req.getRemoteAddr(), ctx.userAgent());
        ctx.redirect(shortenedUrl.getToUrl());
    }

    @Override
    public void applyRoutes() {
        app.get("/r/:code", this::performRedirect);
    }
}