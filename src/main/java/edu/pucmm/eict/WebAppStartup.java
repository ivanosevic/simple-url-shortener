package edu.pucmm.eict;

import edu.pucmm.eict.auth.AuthController;
import edu.pucmm.eict.common.ApplicationProperties;
import edu.pucmm.eict.common.ErrorController;
import edu.pucmm.eict.common.Startup;
import edu.pucmm.eict.common.StaticFileController;
import edu.pucmm.eict.dashboard.DashboardInterceptor;
import edu.pucmm.eict.dashboard.UserZoneController;
import edu.pucmm.eict.reports.ReportController;
import edu.pucmm.eict.security.SecurityConfig;
import edu.pucmm.eict.urls.controllers.RedirectController;
import edu.pucmm.eict.urls.controllers.ShortURLController;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinVelocity;

public class WebAppStartup implements Startup {

    private ApplicationProperties appProperties = ApplicationProperties.getInstance();

    @Override
    public void boot(String[] args) {
        int javalinPort = appProperties.getPort();
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
            config.showJavalinBanner = false;
        }).start(javalinPort);

        JavalinRenderer.register(JavalinVelocity.INSTANCE);

        new SecurityConfig(app).applyConfig();
        new ErrorController(app).applyRoutes();
        new StaticFileController(app).applyRoutes();
        new AuthController(app).applyRoutes();
        new ShortURLController(app).applyRoutes();
        new RedirectController(app).applyRoutes();
        new DashboardInterceptor(app).applyRoutes();
        new UserZoneController(app).applyRoutes();
        new ReportController(app).applyRoutes();
    }
}
