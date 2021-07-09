package edu.pucmm.eict;

import edu.pucmm.eict.auth.AuthController;
import edu.pucmm.eict.bootstrap.DataBootstrap;
import edu.pucmm.eict.bootstrap.DatabaseBootstrap;
import edu.pucmm.eict.common.ApplicationProperties;
import edu.pucmm.eict.common.ErrorController;
import edu.pucmm.eict.common.StaticFileController;
import edu.pucmm.eict.security.SecurityConfig;
import edu.pucmm.eict.urls.ShortenUrlController;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinVelocity;

public class Main {
    public static void main(String[] args) {
        ApplicationProperties appProperties = ApplicationProperties.getInstance();
        DatabaseBootstrap dbBootstrap = DatabaseBootstrap.getInstance();

        // Try to make database embeddable
        boolean isEmbedded = appProperties.isEmbedded();
        int dbPort = appProperties.getH2Port();
        if(isEmbedded) {
            dbBootstrap.init(dbPort);
        }

        // Bootstrap initial data
        DataBootstrap dataBootstrap = DataBootstrap.getInstance();
        dataBootstrap.inserts();

        int javalinPort = appProperties.getPort();
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
            config.enableCorsForAllOrigins();
        }).start(javalinPort);

        JavalinRenderer.register(JavalinVelocity.INSTANCE);

        new SecurityConfig(app).applyConfig();
        new ErrorController(app).applyRoutes();
        new StaticFileController(app).applyRoutes();
        new AuthController(app).applyRoutes();
        new ShortenUrlController(app).applyRoutes();
    }
}