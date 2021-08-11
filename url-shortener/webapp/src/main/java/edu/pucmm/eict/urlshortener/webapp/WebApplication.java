package edu.pucmm.eict.urlshortener.webapp;

import edu.pucmm.eict.urlshortener.persistence.PersistenceService;
import edu.pucmm.eict.urlshortener.reports.*;
import edu.pucmm.eict.urlshortener.urls.*;
import edu.pucmm.eict.urlshortener.users.AuthService;
import edu.pucmm.eict.urlshortener.users.RoleDao;
import edu.pucmm.eict.urlshortener.users.UserDao;
import edu.pucmm.eict.urlshortener.users.UserService;
import edu.pucmm.eict.urlshortener.webapp.bootstrap.DataBootstrap;
import edu.pucmm.eict.urlshortener.webapp.bootstrap.DataBootstrapImpl;
import edu.pucmm.eict.urlshortener.webapp.bootstrap.EmbeddedDb;
import edu.pucmm.eict.urlshortener.webapp.bootstrap.H2Database;
import edu.pucmm.eict.urlshortener.webapp.config.CustomThymeleafRenderer;
import edu.pucmm.eict.urlshortener.webapp.config.JasyptEncryptor;
import edu.pucmm.eict.urlshortener.webapp.controllers.*;
import edu.pucmm.eict.urlshortener.webapp.converters.ShortUrlDtoConverter;
import edu.pucmm.eict.urlshortener.webapp.config.SecurityConfig;
import edu.pucmm.eict.urlshortener.webapp.services.SessionFlash;
import edu.pucmm.eict.urlshortener.webapp.services.SessionUrlService;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.seruco.encoding.base62.Base62;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class WebApplication {
    private static final Logger log = LoggerFactory.getLogger(WebApplication.class);
    private static final String DOMAIN = "179.53.16.254:7000";
    private static final String PERSISTENCE_UNIT = "embedded";
    private static final String ENCRYPT_PASSWORD = "wd583szUNc8bNZSA";
    private static final int DB_PORT = 9092;
    private static final int APP_PORT = 7000;

    public static void main(String[] args) {
        /*
         * First, we must start the database, and then initialize
         * the persistence services(entity manager factory) for the DAOs to use.
         * */
        EmbeddedDb embeddedDb = new H2Database(DB_PORT);
        embeddedDb.start();

        PersistenceService persistenceService = new PersistenceService(PERSISTENCE_UNIT);
        persistenceService.start();


        EntityManagerFactory entityManagerFactory = persistenceService.getEntityManagerFactory();

        // Initializing utils from users modules
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(ENCRYPT_PASSWORD);

        // Initializing DAOs and services from user modules...
        RoleDao roleDao = new RoleDao(entityManagerFactory);
        UserDao userDao = new UserDao(entityManagerFactory);
        JasyptEncryptor jasyptEncryptor = new JasyptEncryptor(passwordEncryptor, encryptor);
        AuthService authService = new AuthService(userDao, jasyptEncryptor);
        UserService userService = new UserService(userDao, jasyptEncryptor);

        // Initializing utils from urls modules
        QrGenerator qrGenerator = new PngQrGenerator();
        Base62 base62Encoder = Base62.createInstance();
        org.apache.commons.validator.routines.UrlValidator apacheUrlValidator = new org.apache.commons.validator.routines.UrlValidator();
        UrlValidator urlValidator = new UrlValidator(apacheUrlValidator);

        // Initializing DAOs and services from urls modules...
        RedirectUrlBuilder redirectUrlBuilder = new BasicRedirectUrlBuilder(DOMAIN);
        Client client = ClientBuilder.newClient();
        UserAgentAnalyzer userAgentAnalyzer = UserAgentAnalyzer.newBuilder().withCache(1234)
                .withField("DeviceClass")
                .withAllFields()
                .build();

        ShortUrlDao shortUrlDao = new ShortUrlDao(entityManagerFactory);
        ClickDao clickDao = new ClickDao(entityManagerFactory);
        IpInfoService ipInfoService = new IpInfoService(client);
        ShortUrlService shortUrlService = new ShortUrlService(shortUrlDao, base62Encoder, urlValidator);
        RedirectService redirectService = new RedirectService(clickDao, shortUrlDao, ipInfoService, userAgentAnalyzer);

        // Initializing DAOs and services from reports modules...
        DataConverter dataConverter = new DataConverter();
        UrlStatisticsDao urlStatisticsDao = new UrlStatisticsDao(entityManagerFactory);
        AdminReportDao adminReportDao = new AdminReportDao(entityManagerFactory);
        UrlStatisticsService urlStatisticsService = new UrlStatisticsService(urlStatisticsDao, dataConverter);
        AdminReportService adminReportService = new AdminReportService(adminReportDao);

        /*
         * Next time is to check and initialize the initial data
         * of the application.
         * */
        DataBootstrap dataBootstrap = new DataBootstrapImpl(roleDao, userService);
        dataBootstrap.inserts();

        /*
         * Last step is to create the web server
         * alongside with its dependencies.
         * */
        Javalin app = Javalin.create(config -> {
            config.registerPlugin(new RouteOverviewPlugin("/routes"));
            config.showJavalinBanner = false;
            config.addStaticFiles("/public", "/public", Location.CLASSPATH);
        });


        /*
        * Don't forget to created the mapper
        * to map complex entities.
         * */
        ModelMapper modelMapper = new ModelMapper();
        ShortUrlDtoConverter shortUrlDtoConverter = new ShortUrlDtoConverter(qrGenerator, redirectUrlBuilder);
        modelMapper.addConverter(shortUrlDtoConverter);

        SessionUrlService sessionUrlService = new SessionUrlService(redirectUrlBuilder, qrGenerator);
        SessionFlash sessionFlash = new SessionFlash();
        CustomThymeleafRenderer customThymeleafRenderer = new CustomThymeleafRenderer();

        // Javalin will implement my own security implementation...
        SecurityConfig securityConfig = new SecurityConfig(authService);
        app.config.accessManager(securityConfig);
        JavalinRenderer.register(customThymeleafRenderer, ".html");

        // Controllers...
        new AdviceController(app).applyRoutes();
        new RedirectAppFilter(app).applyRoutes();
        new RedirectUrlController(app, redirectService).applyRoutes();
        new AssignUrlsToUserFilter(app, sessionUrlService, shortUrlService).applyRoutes();
        new AdminZoneController(app, adminReportService, shortUrlService, userService, sessionFlash, modelMapper).applyRoutes();
        new AuthController(app, userService, authService, sessionFlash).applyRoutes();
        new ShortUrlController(app, shortUrlService, sessionUrlService, sessionFlash).applyRoutes();
        new UrlStatisticsController(app, sessionUrlService, urlStatisticsService, shortUrlDao).applyRoutes();

        // Start web server application
        app.start(APP_PORT);
    }
}
