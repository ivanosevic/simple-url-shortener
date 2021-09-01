package edu.pucmm.eict.webapp;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.pucmm.eict.persistence.PersistenceModule;
import edu.pucmm.eict.reports.ReportModule;
import edu.pucmm.eict.urls.UrlModule;
import edu.pucmm.eict.users.UserModule;
import edu.pucmm.eict.webapp.bootstrap.DataBootstrap;
import edu.pucmm.eict.webapp.bootstrap.EmbeddedDb;
import edu.pucmm.eict.webapp.controllers.*;
import io.javalin.Javalin;

public class Main {
    private static final int JAVALIN_PORT = 7000;

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new PersistenceModule(),
                new UserModule(),
                new UrlModule(),
                new ReportModule(),
                new WebappModule()
        );

        injector.getInstance(EmbeddedDb.class).start();

        injector.getInstance(DataBootstrap.class).inserts();

        injector.getInstance(Javalin.class).start(JAVALIN_PORT);

        injector.getInstance(AdminZoneController.class).applyRoutes();
        injector.getInstance(AdviceController.class).applyRoutes();
        injector.getInstance(AssignUrlsToUserFilter.class).applyRoutes();
        injector.getInstance(AuthController.class).applyRoutes();
        injector.getInstance(RedirectToAppFilter.class).applyRoutes();
        injector.getInstance(RedirectUrlController.class).applyRoutes();
        injector.getInstance(ShortUrlController.class).applyRoutes();
        injector.getInstance(UrlStatisticsController.class).applyRoutes();
    }
}
