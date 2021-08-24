package edu.pucmm.eict.webapp.controllers;

import edu.pucmm.eict.urls.ShortUrl;
import edu.pucmm.eict.urls.ShortUrlService;
import edu.pucmm.eict.users.User;
import edu.pucmm.eict.webapp.sessionurls.SessionUrl;
import edu.pucmm.eict.webapp.sessionurls.SessionUrlService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AssignUrlsToUserFilter extends BaseRoute {

    private final SessionUrlService sessionUrlService;
    private final ShortUrlService shortUrlService;

    @Inject
    public AssignUrlsToUserFilter(Javalin app, SessionUrlService sessionUrlService, ShortUrlService shortUrlService) {
        super(app);
        this.sessionUrlService = sessionUrlService;
        this.shortUrlService = shortUrlService;
    }

    private void assignGeneratedUrls(Context ctx) {
        User user = ctx.sessionAttribute("user");
        if(user != null) {
            List<SessionUrl> sessionUrls = sessionUrlService.getList(ctx);
            ArrayList<ShortUrl> urls = new ArrayList<>();
            if(sessionUrls != null) {
                for(var sessionUrl : sessionUrls) {
                    // Check if that URL is still active...
                    String urlCode = sessionUrl.getUrlCode();
                    var urlDb = shortUrlService.findByCode(urlCode);
                    urlDb.ifPresent(urls::add);
                }
                // add everything to a batch
                shortUrlService.assignUrlsToUser(user, urls);

                // Clean the session urls from context
                sessionUrlService.clean(ctx);
            }
        }
    }

    @Override
    public void applyRoutes() {
        app.after("/login/process", this::assignGeneratedUrls);
    }
}
