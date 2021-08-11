package edu.pucmm.eict.urlshortener.restapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.pucmm.eict.urlshortener.persistence.PersistenceService;
import edu.pucmm.eict.urlshortener.reports.*;
import edu.pucmm.eict.urlshortener.restapi.config.JwtUtil;
import edu.pucmm.eict.urlshortener.restapi.config.SecurityConfig;
import edu.pucmm.eict.urlshortener.restapi.converters.LoginDtoConverter;
import edu.pucmm.eict.urlshortener.restapi.converters.ShortUrlDtoConverter;
import edu.pucmm.eict.urlshortener.restapi.endpoints.ErrorAdviceEndpoint;
import edu.pucmm.eict.urlshortener.restapi.endpoints.LoginEndpoint;
import edu.pucmm.eict.urlshortener.restapi.endpoints.ShortUrlEndpoint;
import edu.pucmm.eict.urlshortener.urls.*;
import edu.pucmm.eict.urlshortener.users.*;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.plugin.json.JavalinJson;
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

public class RestApiApplication {
    private static final Logger log = LoggerFactory.getLogger(RestApiApplication.class);
    private static final String DOMAIN = "179.53.16.254:7000";
    private static final String PERSISTENCE_UNIT = "embedded";
    private static final String ENCRYPT_PASSWORD = "wd583szUNc8bNZSA";
    private static final int APP_PORT = 7001;
    private static final String API_SECRET = "dDgHtpN5Hpfehzku";
    private static final Integer API_TOKEN_EXPIRATION_MS = 86400000;

    public static void main(String[] args) {

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
         * Last step is to create the web server
         * alongside with its dependencies.
         * */
        Javalin app = Javalin.create(config -> {
            config.registerPlugin(new RouteOverviewPlugin("/routes"));
            config.showJavalinBanner = false;
        });

        // Configuring JSON mapper from GSON
        Gson gson = new GsonBuilder().create();
        JavalinJson.setFromJsonMapper(gson::fromJson);
        JavalinJson.setToJsonMapper(gson::toJson);

        ModelMapper modelMapper = new ModelMapper();
        LoginDtoConverter loginDtoConverter = new LoginDtoConverter();
        ShortUrlDtoConverter shortUrlDtoConverter = new ShortUrlDtoConverter(redirectUrlBuilder);
        modelMapper.addConverter(loginDtoConverter);
        modelMapper.addConverter(shortUrlDtoConverter);
        JwtUtil jwtUtil = new JwtUtil(API_SECRET, API_TOKEN_EXPIRATION_MS);
        SecurityConfig securityConfig = new SecurityConfig(jwtUtil, userDao);
        app.config.accessManager(securityConfig);

        /*
         * Don't forget to created the mapper
         * to map complex entities.
         * */
        new ErrorAdviceEndpoint(app).applyEndpoints();
        new LoginEndpoint(app, authService, jwtUtil, modelMapper).applyEndpoints();
        new ShortUrlEndpoint(app, userDao, shortUrlService, urlStatisticsService, modelMapper, jwtUtil).applyEndpoints();

        // Start web server application
        app.start(APP_PORT);
    }
}
