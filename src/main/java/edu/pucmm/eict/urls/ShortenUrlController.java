package edu.pucmm.eict.urls;

import edu.pucmm.eict.common.Controller;
import edu.pucmm.eict.common.MyValidator;
import edu.pucmm.eict.users.User;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.*;

public class ShortenUrlController extends Controller {

    private final MyValidator validator;
    private final ShortenedUrlDtoConverter converter;
    private final ShortenUrlService shortenUrlService;

    public ShortenUrlController(Javalin app) {
        super(app);
        this.converter = ShortenedUrlDtoConverter.getInstance();
        this.shortenUrlService = ShortenUrlService.getInstance();
        this.validator = MyValidator.getInstance();
    }

    private void shortenUrlView(Context ctx) {
        var data = new HashMap<String, Object>();
        List<ShortenedUrlDto> urls = ctx.sessionAttribute("urls");
        if(urls != null) {
            Collections.reverse(urls);
        }
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

        // Create the url
        ShortenedUrl shortenedUrl = shortenUrlService.doShort(form.getUrl(), form.getName(), user);
        ShortenedUrlDto dto = converter.convert(shortenedUrl);

        // Now we save it to the session
        List<ShortenedUrlDto> urls = ctx.sessionAttribute("urls");
        if(urls == null) {
            urls = new ArrayList<>();
            ctx.sessionAttribute("urls", urls);
        }
        urls.add(dto);

        // Return to the original route
        ctx.redirect("/");
    }

    private void generateQr(Context ctx) {
        String code = ctx.pathParam("code", String.class).get();
        String base64Image = shortenUrlService.getQrCodeBase64(code);
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