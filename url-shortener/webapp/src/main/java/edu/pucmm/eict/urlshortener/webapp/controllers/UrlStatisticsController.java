package edu.pucmm.eict.urlshortener.webapp.controllers;

import edu.pucmm.eict.urlshortener.reports.UrlReport;
import edu.pucmm.eict.urlshortener.reports.UrlStatisticsService;
import edu.pucmm.eict.urlshortener.urls.ShortUrl;
import edu.pucmm.eict.urlshortener.urls.ShortUrlDao;
import edu.pucmm.eict.urlshortener.webapp.SessionUrl;
import edu.pucmm.eict.urlshortener.webapp.SessionUrlService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class UrlStatisticsController extends BaseController {

    private final SessionUrlService sessionUrlService;
    private final UrlStatisticsService urlStatisticsService;
    private final ShortUrlDao shortUrlDao;

    public UrlStatisticsController(Javalin app, SessionUrlService sessionUrlService, UrlStatisticsService urlStatisticsService, ShortUrlDao shortUrlDao) {
        super(app);
        this.sessionUrlService = sessionUrlService;
        this.urlStatisticsService = urlStatisticsService;
        this.shortUrlDao = shortUrlDao;
    }

    private void showTemporaryPage(Context ctx) {
        String title = "Temporary Statistics";
        String temporaryCode = ctx.pathParam("temporaryCode", String.class).get();
        SessionUrl sessionUrl = sessionUrlService.findByTemporaryCode(ctx, temporaryCode).orElseThrow(EntityNotFoundException::new);
        String code = sessionUrl.getUrlCode();
        ShortUrl shortUrl = shortUrlDao.findByCode(code).orElseThrow(EntityNotFoundException::new);
        Long shortUrlId = shortUrl.getId();
        UrlReport report = urlStatisticsService.fetchReport(shortUrlId);
        Map<String, Object> data = new HashMap<>();
        data.put("report", report);
        data.put("title", title);
        data.put("sessionUrl", sessionUrl);
        ctx.render("link-statistics.html", data);
    }

    @Override
    public void applyRoutes() {
        app.get("/statistics/temp/:temporaryCode", this::showTemporaryPage);
    }
}
