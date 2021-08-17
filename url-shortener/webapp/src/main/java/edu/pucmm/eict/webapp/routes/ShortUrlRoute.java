package edu.pucmm.eict.webapp.routes;

import edu.pucmm.eict.urls.InvalidUrlException;
import edu.pucmm.eict.webapp.controllers.ShortUrlController;
import io.javalin.Javalin;

public class ShortUrlRoute extends BaseRoute {

    private final ShortUrlController shortUrlController;

    public ShortUrlRoute(Javalin app, ShortUrlController shortUrlController) {
        super(app);
        this.shortUrlController = shortUrlController;
    }

    @Override
    public void applyRoutes() {
        app.get("/", shortUrlController::mainView);
        app.post("/short", shortUrlController::shortUrl);
        app.exception(InvalidUrlException.class, shortUrlController::handleInvalidUrlException);
    }
}
