package edu.pucmm.eict.urlshortener.webapp.controllers;

import edu.pucmm.eict.urlshortener.urls.InvalidUrlException;
import edu.pucmm.eict.urlshortener.urls.ShortUrl;
import edu.pucmm.eict.urlshortener.urls.ShortUrlService;
import edu.pucmm.eict.urlshortener.webapp.SessionFlash;
import edu.pucmm.eict.urlshortener.webapp.SessionUrl;
import edu.pucmm.eict.urlshortener.webapp.SessionUrlService;
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
        Map<String, List<String>> errors = sessionFlash.get("errors", ctx);
        Boolean hasErrors = sessionFlash.get("hasErrors", ctx);
        Boolean success = sessionFlash.get("success", ctx);
        String errorMessage = sessionFlash.get("errorMessage", ctx);
        data.put("sessionUrls", sessionUrls);
        data.put("errorMessage", errorMessage);
        data.put("errors", errors);
        data.put("hasErrors", hasErrors);
        data.put("success", success);
        return data;
    }

    private void showMainView(Context ctx) {
        List<SessionUrl> sessionUrls = sessionUrlService.getList(ctx);
        var data = mainViewData(ctx);
        ctx.render("index.html", data);
    }

    private void processUrl(Context ctx) {
        var vUrl = ctx.formParam("url", String.class)
                .check(s-> !s.isBlank(), "Url can't be blank")
                .check(s -> s.length() <= 2048, "Url can't be longer than 2048 characters.");
        var errors = Validator.collectErrors(vUrl);
        if(!errors.isEmpty()) {
            sessionFlash.add("hasErrors", true, ctx);
            sessionFlash.add("errors", errors, ctx);
            ctx.redirect("/", HttpStatus.SEE_OTHER_303);
            return;
        }
        String url = vUrl.getValue();
        ShortUrl shortUrl = shortUrlService.doShort(null, url);
        sessionUrlService.addToSession(ctx, shortUrl);
        sessionFlash.add("success", true, ctx);
        ctx.redirect("/", HttpStatus.SEE_OTHER_303);
    }

    private void handleInvalidUrlException(Exception ex, Context ctx) {
        log.info("{}", ex.getMessage());
        sessionFlash.add("errorMessage", "The given URL is invalid. Please, make sure that the URL is correct.", ctx);
        ctx.redirect("/", HttpStatus.SEE_OTHER_303);
    }

    @Override
    public void applyRoutes() {
        app.get("/", this::showMainView);
        app.post("/short", this::processUrl);
        app.exception(InvalidUrlException.class, this::handleInvalidUrlException);
    }
}
