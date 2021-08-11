package edu.pucmm.eict.urlshortener.webapp.controllers;

import edu.pucmm.eict.urlshortener.urls.InvalidUrlException;
import edu.pucmm.eict.urlshortener.urls.ShortUrl;
import edu.pucmm.eict.urlshortener.urls.ShortUrlService;
import edu.pucmm.eict.urlshortener.users.User;
import edu.pucmm.eict.urlshortener.webapp.services.SessionFlash;
import edu.pucmm.eict.urlshortener.webapp.domain.SessionUrl;
import edu.pucmm.eict.urlshortener.webapp.services.SessionUrlService;
import io.javalin.Javalin;
import io.javalin.core.validation.Validator;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShortUrlController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ShortUrlController.class);
    private final ShortUrlService shortUrlService;
    private final SessionUrlService sessionUrlService;
    private final SessionFlash sessionFlash;

    public ShortUrlController(Javalin app, ShortUrlService shortUrlService, SessionUrlService sessionUrlService, SessionFlash sessionFlash) {
        super(app);
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

    private void showMainView(Context ctx) {
        List<SessionUrl> sessionUrls = sessionUrlService.getList(ctx);
        var data = mainViewData(ctx);
        ctx.render("index.html", data);
    }

    private void processUrl(Context ctx) {
        String url = ctx.formParam("url");
        ShortUrl shortUrl = shortUrlService.doShort(null, url);
        sessionUrlService.addToSession(ctx, shortUrl);
        sessionFlash.add("successOnShortingUrl", true, ctx);
        ctx.redirect("/", HttpStatus.SEE_OTHER_303);
    }

    private void handleInvalidUrlException(Exception ex, Context ctx) {
        log.info("{}", ex.getMessage());
        sessionFlash.add("errorShortingUrl", ex.getMessage(), ctx);
        User user = ctx.sessionAttribute("user");
        if(user != null) {
            if(user.isAdmin()) {
                ctx.redirect("/admin-panel/urls", HttpStatus.SEE_OTHER_303);
            } else {
                ctx.redirect("/user-zone/urls", HttpStatus.SEE_OTHER_303);
            }
            return;
        }
        ctx.redirect("/", HttpStatus.SEE_OTHER_303);
    }

    @Override
    public void applyRoutes() {
        app.get("/", this::showMainView);
        app.post("/short", this::processUrl);
        app.exception(InvalidUrlException.class, this::handleInvalidUrlException);
    }
}
