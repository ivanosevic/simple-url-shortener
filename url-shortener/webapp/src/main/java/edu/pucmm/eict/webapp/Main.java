package edu.pucmm.eict.webapp;

import edu.pucmm.eict.persistence.PersistenceService;
import edu.pucmm.eict.urls.*;
import edu.pucmm.eict.users.*;
import edu.pucmm.eict.webapp.bootstrap.*;
import edu.pucmm.eict.webapp.configuration.*;
import edu.pucmm.eict.webapp.controllers.AuthController;
import edu.pucmm.eict.webapp.controllers.ShortUrlController;
import edu.pucmm.eict.webapp.routes.AuthRoute;
import edu.pucmm.eict.webapp.routes.ShortUrlRoute;
import edu.pucmm.eict.webapp.sessionurls.SessionUrlService;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.seruco.encoding.base62.Base62;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class Main {
    private static final int JAVALIN_PORT = 7000;
    private static final int EMBEDDED_DB_PORT = 9092;
    private static final String PERSISTENCE_UNIT = "embedded";
    private static final String ENCRYPT_PASSWORD = "wd583szUNc8bNZSA";
    private static final String DOMAIN = "short.wolfisc.com";
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        /**
         * Start by booting up the embedded database and
         * enable persistence services.
         */
        EmbeddedDb embeddedDb = new H2Database(EMBEDDED_DB_PORT);
        embeddedDb.start();

        PersistenceService persistenceService = new PersistenceService(PERSISTENCE_UNIT);
        persistenceService.start();

        EntityManagerFactory emf = persistenceService.getEntityManagerFactory();

        /**
         * Preparing the dependencies
         * from the users module
         */
        PasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        StandardPBEStringEncryptor textEncryptor = new StandardPBEStringEncryptor();
        textEncryptor.setPassword(ENCRYPT_PASSWORD);
        MyEncryptor myEncryptor = new JasyptEncryptor(passwordEncryptor, textEncryptor);

        RoleDao roleDao = new RoleDao(emf);
        UserDao userDao = new UserDao(emf);
        UserService userService = new UserService(userDao, myEncryptor);
        AuthService authService = new AuthService(userDao, myEncryptor);

        DataBootstrap dataBootstrap = new DataBootstrapImpl(roleDao, userService);
        dataBootstrap.inserts();

        /**
         * Preparing the dependencies
         * from the urls module
         */
        Client client = ClientBuilder.newClient();
        Base62 base62Encoder = Base62.createInstance();
        org.apache.commons.validator.routines.UrlValidator apacheUrlValidator = new org.apache.commons.validator.routines.UrlValidator();
        UserAgentAnalyzer userAgentAnalyzer = UserAgentAnalyzer.newBuilder().withCache(1234)
                .withField("DeviceClass")
                .withAllFields()
                .build();
        IpInfoService ipInfoService = new IpInfoServiceImpl(client);
        QrGenerator qrGenerator = new PngQrGenerator();
        UrlEncoder urlEncoder = new Base62UrlEncoder(base62Encoder);
        UrlValidator urlValidator = new UrlValidator(apacheUrlValidator);
        ShortUrlBuilder shortUrlBuilder = new ShortUrlBuilderImpl(DOMAIN);

        ClickDao clickDao = new ClickDao(emf);
        ShortUrlDao shortUrlDao = new ShortUrlDao(emf);
        ShortUrlService shortUrlService = new ShortUrlService(shortUrlDao, urlEncoder, urlValidator);
        RedirectService redirectService = new RedirectService(clickDao, shortUrlDao, ipInfoService, userAgentAnalyzer);


        /**
         * Preparing javalin server
         * and dependencies
         */
        Javalin app = Javalin.create(config -> {
            config.registerPlugin(new RouteOverviewPlugin("/routes"));
            config.showJavalinBanner = false;
            config.enableCorsForAllOrigins();
            config.addStaticFiles("/public", "/public", Location.CLASSPATH);
        });

        CustomThymeleafRenderer thymeleafRenderer = new CustomThymeleafRenderer();
        JavalinRenderer.register(thymeleafRenderer, ".html");

        SecurityConfig securityConfig = new SecurityConfig(authService);
        app.config.accessManager(securityConfig);

        SessionFlash sessionFlash = new SessionFlash();
        SessionUrlService sessionUrlService = new SessionUrlService(shortUrlBuilder, qrGenerator);

        /**
         * Creating and attaching controllers
         * to routes
         */
        AuthController authController = new AuthController(userService, authService, sessionFlash);
        ShortUrlController shortUrlController = new ShortUrlController(shortUrlService, sessionUrlService, sessionFlash);

        new AuthRoute(app, authController).applyRoutes();
        new ShortUrlRoute(app, shortUrlController).applyRoutes();

        app.start(JAVALIN_PORT);

        /**
         * Before shutting down,
         * let's stop all the services & servers.
         */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Stopping application.");
            app.stop();
        }));
    }
}
