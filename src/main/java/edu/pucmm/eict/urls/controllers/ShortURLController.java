package edu.pucmm.eict.urls.controllers;

import edu.pucmm.eict.common.ApplicationProperties;
import edu.pucmm.eict.common.Controller;
import edu.pucmm.eict.common.MyValidator;
import edu.pucmm.eict.reports.ReportService;
import edu.pucmm.eict.reports.URLGroupByCountry;
import edu.pucmm.eict.reports.URLGroupByPlataform;
import edu.pucmm.eict.urls.models.SessionURL;
import edu.pucmm.eict.urls.models.ShortForm;
import edu.pucmm.eict.urls.models.ShortURL;
import edu.pucmm.eict.urls.services.SessionURLService;
import edu.pucmm.eict.urls.services.ShortURLService;
import edu.pucmm.eict.users.User;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.json.JavalinJson;

import javax.persistence.EntityNotFoundException;
import java.util.*;

public class ShortURLController extends Controller {

    private final MyValidator validator;
    private final ShortURLService shortURLService;
    private final SessionURLService sessionURLService;
    private final ReportService reportService;

    public ShortURLController(Javalin app) {
        super(app);
        this.validator = MyValidator.getInstance();
        this.shortURLService = ShortURLService.getInstance();
        this.sessionURLService = SessionURLService.getInstance();
        this.reportService = ReportService.getInstance();
    }

    private void shortenUrlView(Context ctx) {
        List<SessionURL> urls = sessionURLService.findSessionURLs(ctx);
        var data = new HashMap<String, Object>();
        data.put("urls", urls);
        ctx.status(200).render("templates/index.vm", data);
    }

    private void shortUrl(Context ctx) {
        String url = ctx.formParam("url");
        String name = ctx.formParam("name");
        User user = ctx.sessionAttribute("user");
        ShortForm form = new ShortForm(url, name);
        Map<String, List<String>> errors = validator.validate(form);

        // Data to be sent to model...
        var data = new HashMap<String, Object>();
        if(!errors.isEmpty()) {
            data.put("formErrors", errors);
            ctx.status(400).render("templates/index.vm", data);
            return;
        }

        // Create the URL
        ShortURL shortURL = shortURLService.cut(form.getUrl(), form.getName(), user);

        // Add it to the session
        sessionURLService.addSessionURL(ctx, shortURL);

        // Return to the original route
        ctx.redirect("/");
    }

    private void generateQr(Context ctx) {
        String code = ctx.pathParam("code", String.class).get();
        String base64Image = shortURLService.getQrCodeBase64(code);
        var image = new HashMap<String, String>();
        image.put("imageBase64", base64Image);
        ctx.json(image);
    }

    private void showTempStatistics(Context ctx) {
        String tempCode = ctx.pathParam("tempCode", String.class).get();
        SessionURL sessionURL = sessionURLService.findByTemporaryId(ctx, tempCode).orElseThrow(EntityNotFoundException::new);

        // Getting report information...
        List<URLGroupByCountry> urlGroupByCountries = reportService.URLsGroupByCountry(sessionURL.getId());
        Long amountClicks = reportService.amountClicks(sessionURL.getId());
        Long amountUniqueClicks = reportService.amountUniqueClicks(sessionURL.getId());
        Long clicksLastDay = reportService.amountClicksDuringLastDay(sessionURL.getId());
        List<URLGroupByPlataform> urlGroupByPlatform = reportService.URLGroupByPlatform(sessionURL.getId());
        String topCountryClicks = reportService.topCountryByClicks(sessionURL.getId());
        String urlGroupByCountriesJson = JavalinJson.toJson(urlGroupByCountries);
        String urlGroupByPlatformJson = JavalinJson.toJson(urlGroupByPlatform);
        String mapsApiKey = ApplicationProperties.getInstance().getMapsApiKey();

        // Data to be sent to the view
        var data = new HashMap<String, Object>();
        data.put("url", sessionURL);
        data.put("urlGroupByCountries", urlGroupByCountriesJson);
        data.put("MyMapsApiKey", mapsApiKey);
        data.put("amountClicks", amountClicks);
        data.put("amountUniqueClicks", amountUniqueClicks);
        data.put("clicksLastDay", clicksLastDay);
        data.put("topCountryClicks", topCountryClicks);
        data.put("urlGroupByPlatform", urlGroupByPlatformJson);
        ctx.status(200).render("templates/reports/statistic-in-session.vm", data);
    }

    @Override
    public void applyRoutes() {
        app.get("/", this::shortenUrlView);
        app.post("/short", this::shortUrl);
        app.get("/qr/:code", this::generateQr);
        app.get("/statistics/temp/:tempCode", this::showTempStatistics);
    }
}