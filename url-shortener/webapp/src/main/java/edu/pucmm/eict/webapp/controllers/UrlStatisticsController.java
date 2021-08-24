package edu.pucmm.eict.webapp.controllers;

import edu.pucmm.eict.reports.UrlReport;
import edu.pucmm.eict.reports.UrlReportService;
import edu.pucmm.eict.urls.ShortUrl;
import edu.pucmm.eict.urls.ShortUrlDao;
import edu.pucmm.eict.users.RoleList;
import edu.pucmm.eict.users.User;
import edu.pucmm.eict.webapp.sessionurls.SessionUrl;
import edu.pucmm.eict.webapp.sessionurls.SessionUrlService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UrlStatisticsController extends BaseRoute {

    private final SessionUrlService sessionUrlService;
    private final UrlReportService urlReportService;
    private final ShortUrlDao shortUrlDao;

    @Inject
    public UrlStatisticsController(Javalin app, SessionUrlService sessionUrlService, UrlReportService urlReportService, ShortUrlDao shortUrlDao) {
        super(app);
        this.sessionUrlService = sessionUrlService;
        this.urlReportService = urlReportService;
        this.shortUrlDao = shortUrlDao;
    }

    private void showTemporaryPage(Context ctx) {
        String title = "Temporary Statistics";
        String temporaryCode = ctx.pathParam("temporaryCode", String.class).get();
        SessionUrl sessionUrl = sessionUrlService.findByTemporaryCode(ctx, temporaryCode).orElseThrow(EntityNotFoundException::new);
        String code = sessionUrl.getUrlCode();
        ShortUrl shortUrl = shortUrlDao.findByCode(code).orElseThrow(EntityNotFoundException::new);
        Long shortUrlId = shortUrl.getId();
        UrlReport report = urlReportService.fetchReport(shortUrlId);
        Map<String, Object> data = new HashMap<>();
        data.put("report", report);
        data.put("title", title);
        data.put("sessionUrl", sessionUrl);
        ctx.render("link-statistics.html", data);
    }

    private void showUrlStatisticsToAdmin(Context ctx) {
        String title = "Statistics";
        String code = ctx.pathParam("code", String.class).get();
        ShortUrl shortUrl = shortUrlDao.findByCode(code).orElseThrow(EntityNotFoundException::new);
        Long shortUrlId = shortUrl.getId();
        UrlReport report = urlReportService.fetchReport(shortUrlId);
        SessionUrl sessionUrl = sessionUrlService.transform(shortUrl);
        Map<String, Object> data = new HashMap<>();
        data.put("report", report);
        data.put("title", title);
        data.put("sessionUrl", sessionUrl);
        ctx.render("link-statistics.html", data);
    }

    private void showUrlStatisticsToUser(Context ctx) {
        String title = "Statistics";
        User user = ctx.sessionAttribute("user");
        String code = ctx.pathParam("code", String.class).get();
        ShortUrl shortUrl = shortUrlDao.findByCodeAndUserId(code, user.getId()).orElseThrow(EntityNotFoundException::new);
        UrlReport report = urlReportService.fetchReport(shortUrl.getId());
        SessionUrl sessionUrl = sessionUrlService.transform(shortUrl);
        Map<String, Object> data = new HashMap<>();
        data.put("report", report);
        data.put("title", title);
        data.put("sessionUrl", sessionUrl);
        ctx.render("link-statistics.html", data);
    }

    @Override
    public void applyRoutes() {
        app.get("/statistics/temp/:temporaryCode", this::showTemporaryPage);
        app.get("/admin-panel/urls/:code/statistics", this::showUrlStatisticsToAdmin, Set.of(RoleList.ADMIN));
        app.get("/user-zone/urls/:code/statistics", this::showUrlStatisticsToUser, Set.of(RoleList.APP_USER));
    }
}
