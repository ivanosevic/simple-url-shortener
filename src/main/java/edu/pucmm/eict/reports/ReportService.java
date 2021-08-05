package edu.pucmm.eict.reports;

import edu.pucmm.eict.urls.dao.ShortURLDao;
import io.javalin.plugin.json.JavalinJson;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class ReportService {

    private static ReportService instance;
    private final ReportDao reportDao;
    private final ShortURLDao shortURLDao;

    private ReportService() {
        this.reportDao = ReportDao.getInstance();
        this.shortURLDao = ShortURLDao.getInstance();
    }

    public static ReportService getInstance() {
        if (instance == null) {
            instance = new ReportService();
        }
        return instance;
    }

    public BasicURLReport basicURLReport(Long shortURLId) {
        shortURLDao.findById(shortURLId).orElseThrow(EntityNotFoundException::new);
        long clicks = reportDao.amountClicks(shortURLId);
        long uniqueClicks = reportDao.amountUniqueClicks(shortURLId);
        long clicksDuringDay = reportDao.amountClicksDuringLastDay(shortURLId);
        String topCountryByClicks = reportDao.topCountryByClicks(shortURLId);
        String clicksByBrowser = JavalinJson.toJson(reportDao.URLGroupByBrowser(shortURLId));
        String clicksByPlatform = JavalinJson.toJson(reportDao.URLGroupByPlatform(shortURLId));
        String clicksByCountry = JavalinJson.toJson(reportDao.URLsGroupByCountry(shortURLId));
        String clicksByOS = JavalinJson.toJson(reportDao.URLGroupByOS(shortURLId));
        return new BasicURLReport(clicks, uniqueClicks, clicksDuringDay, topCountryByClicks, clicksByBrowser, clicksByPlatform, clicksByCountry, clicksByOS);
    }

    public UrlReport getUrlReport(Long shortURLId) {
        shortURLDao.findById(shortURLId).orElseThrow(EntityNotFoundException::new);
        long clicks = reportDao.amountClicks(shortURLId);
        long uniqueClicks = reportDao.amountUniqueClicks(shortURLId);
        long clicksDuringDay = reportDao.amountClicksDuringLastDay(shortURLId);
        String topCountryByClicks = reportDao.topCountryByClicks(shortURLId);
        List<StringGroupByNum> clicksByBrowser = reportDao.URLGroupByBrowser(shortURLId);
        List<StringGroupByNum> clicksByPlatform = reportDao.URLGroupByPlatform(shortURLId);
        List<StringGroupByNum> clicksByCountry = reportDao.URLsGroupByCountry(shortURLId);
        List<StringGroupByNum> clicksByOS = reportDao.URLGroupByOS(shortURLId);
        return new UrlReport(clicks, uniqueClicks, clicksDuringDay, topCountryByClicks, clicksByBrowser, clicksByPlatform, clicksByCountry, clicksByOS);
    }

    public AdminReport adminReport() {
        long users = reportDao.totalUsers();
        long clicks = reportDao.totalClicks();
        long links = reportDao.totalLinks();
        long linksLastDay = reportDao.totalLinksLastDay();
        return new AdminReport(clicks, users, links, linksLastDay);
    }
}