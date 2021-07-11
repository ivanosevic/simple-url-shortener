package edu.pucmm.eict.reports;

import edu.pucmm.eict.common.ApplicationProperties;
import edu.pucmm.eict.common.Controller;
import edu.pucmm.eict.urls.models.SessionURL;
import edu.pucmm.eict.urls.services.SessionURLService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;

public class ReportController extends Controller {

    private final SessionURLService sessionURLService;
    private final ReportService reportService;

    public ReportController(Javalin app) {
        super(app);
        this.sessionURLService = SessionURLService.getInstance();
        this.reportService = ReportService.getInstance();
    }

    private void showTempStatistics(Context ctx) {
        String tempCode = ctx.pathParam("tempCode", String.class).get();
        SessionURL sessionURL = sessionURLService.findByTemporaryId(ctx, tempCode).orElseThrow(EntityNotFoundException::new);
        BasicURLReport report = reportService.basicURLReport(sessionURL.getId());
        String mapsApiKey = ApplicationProperties.getInstance().getMapsApiKey();

        // Data to be sent to the view
        var data = new HashMap<String, Object>();
        data.put("url", sessionURL);
        data.put("report", report);
        data.put("MyMapsApiKey", mapsApiKey);
        ctx.status(200).render("templates/reports/statistic-in-session.vm", data);
    }

    @Override
    public void applyRoutes() {
        app.get("/statistics/temp/:tempCode", this::showTempStatistics);
    }
}