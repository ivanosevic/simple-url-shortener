package edu.pucmm.eict.urlshortener.webapp.controllers;

import edu.pucmm.eict.urlshortener.urls.RedirectService;
import edu.pucmm.eict.urlshortener.urls.ShortUrl;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class RedirectUrlController extends BaseController {

    private final RedirectService redirectService;

    public RedirectUrlController(Javalin app, RedirectService redirectService) {
        super(app);
        this.redirectService = redirectService;
    }

    private String getIp(Context ctx) {
        String ip = ctx.header("x-forwarded-for");
        if(ip == null) {
            ip = ctx.ip();
        }
        return ip;
    }

    private void performUrlRedirect(Context ctx) {
        String code = ctx.pathParam("code", String.class).get();
        String ip = getIp(ctx);
        String uaHeader = ctx.userAgent();
        ShortUrl shortUrl = redirectService.getClickInfo(code, ip, uaHeader);
        String longUrl = shortUrl.getUrl();
        ctx.redirect(longUrl);
    }

    @Override
    public void applyRoutes() {
        app.get("/r/:code", this::performUrlRedirect);
    }
}
