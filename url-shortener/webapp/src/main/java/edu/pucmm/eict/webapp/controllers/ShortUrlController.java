package edu.pucmm.eict.webapp.controllers;

import edu.pucmm.eict.urls.ShortUrl;
import edu.pucmm.eict.urls.ShortUrlService;
import edu.pucmm.eict.users.User;
import edu.pucmm.eict.webapp.configuration.SessionFlash;
import edu.pucmm.eict.webapp.sessionurls.SessionUrl;
import edu.pucmm.eict.webapp.sessionurls.SessionUrlService;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShortUrlController {
    private final ShortUrlService shortUrlService;
    private final SessionUrlService sessionUrlService;
    private final SessionFlash sessionFlash;

    public ShortUrlController(ShortUrlService shortUrlService, SessionUrlService sessionUrlService, SessionFlash sessionFlash) {
        this.shortUrlService = shortUrlService;
        this.sessionUrlService = sessionUrlService;
        this.sessionFlash = sessionFlash;
    }

    private Map<String, Object> mainViewData(Context ctx) {
        Map<String, Object> data = new HashMap<>();
        List<SessionUrl> sessionUrls = sessionUrlService.consult(ctx);
        Boolean successOnShortingUrl = sessionFlash.get("successOnShortingUrl", ctx);
        String errorShortingUrl = sessionFlash.get("errorShortingUrl", ctx);
        data.put("sessionUrls", sessionUrls);
        data.put("errorShortingUrl", errorShortingUrl);
        data.put("successOnShortingUrl", successOnShortingUrl);
        return data;
    }

    public void mainView(Context ctx) {
        List<SessionUrl> sessionUrls = sessionUrlService.getList(ctx);
        var data = mainViewData(ctx);
        ctx.render("index.html", data);
    }

    public void shortUrl(Context ctx) {
        String url = ctx.formParam("url");
        ShortUrl shortUrl = shortUrlService.doShort(null, url);
        sessionUrlService.addToSession(ctx, shortUrl);
        sessionFlash.add("successOnShortingUrl", true, ctx);
        ctx.redirect("/", HttpStatus.OK_200);
    }

    public void handleInvalidUrlException(Exception ex, Context ctx) {
        sessionFlash.add("errorShortingUrl", ex.getMessage(), ctx);
        User user = ctx.sessionAttribute("user");
        if(user != null) {
            if(user.isAdmin()) {
                ctx.redirect("/admin-panel/urls", HttpStatus.BAD_REQUEST_400);
            } else {
                ctx.redirect("/user-zone/urls", HttpStatus.BAD_REQUEST_400);
            }
            return;
        }
        ctx.redirect("/", HttpStatus.BAD_REQUEST_400);
    }
}
