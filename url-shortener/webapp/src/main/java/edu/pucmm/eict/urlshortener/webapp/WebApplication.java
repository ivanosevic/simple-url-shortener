package edu.pucmm.eict.urlshortener.webapp;

import edu.pucmm.eict.urlshortener.persistence.PersistenceService;
import edu.pucmm.eict.urlshortener.reports.DataConverter;
import edu.pucmm.eict.urlshortener.reports.UrlStatisticsDao;
import edu.pucmm.eict.urlshortener.reports.UrlStatisticsService;
import edu.pucmm.eict.urlshortener.urls.*;
import edu.pucmm.eict.urlshortener.users.AuthService;
import edu.pucmm.eict.urlshortener.users.UserDao;
import edu.pucmm.eict.urlshortener.users.UserService;
import edu.pucmm.eict.urlshortener.webapp.bootstrap.EmbeddedDb;
import edu.pucmm.eict.urlshortener.webapp.bootstrap.H2Database;
import edu.pucmm.eict.urlshortener.webapp.controllers.*;
import edu.pucmm.eict.urlshortener.webapp.security.SecurityConfig;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.seruco.encoding.base62.Base62;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;

public class WebApplication {
    private static final Logger log = LoggerFactory.getLogger(WebApplication.class);
    private static final String DOMAIN = "wolfisc.com";
    private static final String PERSISTENCE_UNIT = "embedded";
    private static final String ENCRYPT_PASSWORD = "wd583szUNc8bNZSA";
    private static final int DB_PORT = 9092;
    private static final int APP_PORT = 7000;

    public static void main(String[] args) {
        EmbeddedDb embeddedDb = new H2Database(DB_PORT);
        embeddedDb.start();

        PersistenceService persistenceService = new PersistenceService(PERSISTENCE_UNIT);
        persistenceService.start();

        // Initializing jasypt encryptors
        EntityManagerFactory entityManagerFactory = persistenceService.getEntityManagerFactory();

        // Initializing DAOs and services from user modules...
        UserDao userDao = new UserDao(entityManagerFactory);
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(ENCRYPT_PASSWORD);
        AuthService authService = new AuthService(userDao, passwordEncryptor, textEncryptor);
        UserService userService = new UserService(userDao, passwordEncryptor);


        // Initializing DAOs and services from urls modules...
        QrGenerator qrGenerator = new PngQrGenerator();
        Base62 base62Encoder = Base62.createInstance();
        org.apache.commons.validator.routines.UrlValidator apacheUrlValidator = new org.apache.commons.validator.routines.UrlValidator();
        UrlValidator urlValidator = new UrlValidator(apacheUrlValidator);
        ShortUrlDao shortUrlDao = new ShortUrlDao(entityManagerFactory);
        ShortUrlService shortUrlService = new ShortUrlService(shortUrlDao, base62Encoder, urlValidator);
        RedirectUrlBuilder redirectUrlBuilder = new BasicRedirectUrlBuilder(DOMAIN);
        SessionUrlService sessionUrlService = new SessionUrlService(redirectUrlBuilder, qrGenerator);


        // Initializing DAOs and services from reports modules...
        DataConverter dataConverter = new DataConverter();
        UrlStatisticsDao urlStatisticsDao = new UrlStatisticsDao(entityManagerFactory);
        UrlStatisticsService urlStatisticsService = new UrlStatisticsService(urlStatisticsDao, dataConverter);

        // Now, let's initialize web server...
        Javalin app = Javalin.create(config -> {
            config.registerPlugin(new RouteOverviewPlugin("/routes"));
            config.showJavalinBanner = false;
            config.addStaticFiles("/public", "/public", Location.CLASSPATH);
        });

        SessionFlash sessionFlash = new SessionFlash();
        CustomThymeleafRenderer customThymeleafRenderer = new CustomThymeleafRenderer();
        JavalinRenderer.register(customThymeleafRenderer, ".html");

        // Javalin will implement my own security implementation...
        SecurityConfig securityConfig = new SecurityConfig(authService);
        app.config.accessManager(securityConfig);

        // Controllers...
        new AdviceController(app).applyRoutes();
        new AuthController(app, authService, sessionFlash).applyRoutes();
        new UserZoneController(app).applyRoutes();
        new ShortUrlController(app, shortUrlService, sessionUrlService, sessionFlash).applyRoutes();
        new UrlStatisticsController(app, sessionUrlService, urlStatisticsService, shortUrlDao).applyRoutes();
        // Start web server application
        app.start(APP_PORT);
    }
}
