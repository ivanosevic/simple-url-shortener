package edu.pucmm.eict.urls.controllers;

import edu.pucmm.eict.common.Controller;
import edu.pucmm.eict.common.MyValidator;
import edu.pucmm.eict.reports.ReportService;
import edu.pucmm.eict.urls.models.SessionURL;
import edu.pucmm.eict.urls.models.ShortForm;
import edu.pucmm.eict.urls.models.ShortURL;
import edu.pucmm.eict.urls.services.SessionURLService;
import edu.pucmm.eict.urls.services.ShortURLService;
import edu.pucmm.eict.users.User;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShortURLController extends Controller {

    private final MyValidator validator;
    private final ShortURLService shortURLService;
    private final SessionURLService sessionURLService;

    public ShortURLController(Javalin app) {
        super(app);
        this.validator = MyValidator.getInstance();
        this.shortURLService = ShortURLService.getInstance();
        this.sessionURLService = SessionURLService.getInstance();
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

    @Override
    public void applyRoutes() {
        app.get("/", this::shortenUrlView);
        app.post("/short", this::shortUrl);
        app.get("/qr/:code", this::generateQr);
    }
}