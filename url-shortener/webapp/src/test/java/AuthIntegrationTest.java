import edu.pucmm.eict.persistence.PersistenceService;
import edu.pucmm.eict.urls.*;
import edu.pucmm.eict.users.*;
import edu.pucmm.eict.webapp.bootstrap.DataBootstrap;
import edu.pucmm.eict.webapp.bootstrap.DataBootstrapImpl;
import edu.pucmm.eict.webapp.configuration.CustomThymeleafRenderer;
import edu.pucmm.eict.webapp.configuration.SecurityConfig;
import edu.pucmm.eict.webapp.configuration.SessionFlash;
import edu.pucmm.eict.webapp.controllers.AuthController;
import edu.pucmm.eict.webapp.controllers.ShortUrlController;
import edu.pucmm.eict.webapp.sessionurls.SessionUrlService;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.seruco.encoding.base62.Base62;
import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.eclipse.jetty.http.HttpStatus;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthIntegrationTest {

    private Javalin javalin;

    @BeforeAll
    public void before() {
        /**
         * Start by booting up the embedded database and
         * enable persistence services.
         */
        PersistenceService persistenceService = new PersistenceService("tests");
        persistenceService.start();
        EntityManagerFactory emf = persistenceService.getEntityManagerFactory();

        /**
         * Preparing the dependencies
         * from the users module
         */
        PasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        StandardPBEStringEncryptor textEncryptor = new StandardPBEStringEncryptor();
        textEncryptor.setPassword("wd583szUNc8bNZSA");
        MyEncryptor myEncryptor = new JasyptEncryptor(passwordEncryptor, textEncryptor);

        RoleDao roleDao = new RoleDao(emf);
        UserDao userDao = new UserDao(emf);
        UserService userService = new UserService(userDao, myEncryptor);
        AuthService authService = new AuthService(userDao, myEncryptor);

        DataBootstrap dataBootstrap = new DataBootstrapImpl(roleDao, userService);
        dataBootstrap.inserts();

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
        MyUrlValidator myUrlValidator = new MyUrlValidator(apacheUrlValidator);
        ShortUrlBuilder shortUrlBuilder = new ShortUrlBuilderImpl("short.wolfisc.com");

        ClickDao clickDao = new ClickDao(emf);
        ShortUrlDao shortUrlDao = new ShortUrlDao(emf);
        ShortUrlService shortUrlService = new ShortUrlService(shortUrlDao, urlEncoder, myUrlValidator);
        RedirectService redirectService = new RedirectService(clickDao, shortUrlDao, ipInfoService, userAgentAnalyzer);

        /**
         * Preparing javalin server
         * and dependencies
         */
        javalin = Javalin.create(config -> {
            config.registerPlugin(new RouteOverviewPlugin("/routes"));
            config.showJavalinBanner = false;
            config.enableCorsForAllOrigins();
            config.addStaticFiles("/public", "/public", Location.CLASSPATH);
        });

        CustomThymeleafRenderer thymeleafRenderer = new CustomThymeleafRenderer();
        JavalinRenderer.register(thymeleafRenderer, ".html");

        SecurityConfig securityConfig = new SecurityConfig(authService);
        javalin.config.accessManager(securityConfig);

        SessionFlash sessionFlash = new SessionFlash();
        SessionUrlService sessionUrlService = new SessionUrlService(shortUrlBuilder, qrGenerator);
        new AuthController(javalin, userService, authService, sessionFlash).applyRoutes();
        new ShortUrlController(javalin, shortUrlService, sessionUrlService, sessionFlash).applyRoutes();

        javalin.start(7000);
    }

    @Test
    public void POST_login_admin_user() {
        HttpResponse response = Unirest.post("http://localhost:7000/login/process")
                .field("username", "Admin")
                .field("password", "admin")
                .asEmpty();
        assertEquals(HttpStatus.OK_200, response.getStatus());
    }

    @Test
    public void POST_login_user_with_remember_me() {
        HttpResponse response = Unirest.post("http://localhost:7000/login/process")
                .field("username", "Admin")
                .field("password", "admin")
                .field("remember-me", "remember-me")
                .asEmpty();
        Cookie rememberMeCookie = response.getCookies().getNamed("remember-me");
        int maxAge = rememberMeCookie.getMaxAge();
        assertEquals(HttpStatus.OK_200, response.getStatus());
        assertEquals(rememberMeCookie.getExpiration().toLocalDateTime().plusDays(7),
                rememberMeCookie.getExpiration().toLocalDateTime().plusSeconds(maxAge));
    }

    @Test
    public void POST_login_bad_credentials_user() {
        HttpResponse response = Unirest.post("http://localhost:7000/login/process")
                .field("username", "fakeuser")
                .field("password", "fakepassword")
                .asEmpty();
        assertEquals(HttpStatus.SEE_OTHER_303, response.getStatus());
    }

    @Test
    public void GET_login_view() {
        HttpResponse response = Unirest.get("http://localhost:7000/login").asEmpty();
        assertEquals(HttpStatus.OK_200, response.getStatus());
    }

    @Test
    public void GET_signup_view() {
        HttpResponse response = Unirest.get("http://localhost:7000/signup").asEmpty();
        assertEquals(HttpStatus.OK_200, response.getStatus());
    }

    @Test
    public void POST_null_values_signup_form() {
        String empty = "";
        HttpResponse response = Unirest.post("http://localhost:7000/signup/process")
                .field("username", empty)
                .field("password", empty)
                .field("name", empty)
                .field("lastname", empty)
                .field("email", empty)
                .asString();
        assertEquals(HttpStatus.SEE_OTHER_303, response.getStatus());
    }

    @Test
    public void POST_with_already_taken_user_and_email() {
        HttpResponse response = Unirest.post("http://localhost:7000/signup/process")
                .field("username", "Admin")
                .field("password", "123456")
                .field("name", "MaliciousUser")
                .field("lastname", "Big Evil")
                .field("email", "20170874@ce.pucmm.edu.do")
                .asString();
        assertEquals(HttpStatus.SEE_OTHER_303, response.getStatus());
    }

    @Test
    public void create_user_then_login_logout() {
        HttpResponse performSignup = Unirest.post("http://localhost:7000/signup/process")
                .field("username", "TestUser1")
                .field("password", "123456")
                .field("name", "TestUser1")
                .field("lastname", "TestUser1")
                .field("email", "testuser1@wolfisc.com")
                .asString();

        HttpResponse performLogin = Unirest.post("http://localhost:7000/login/process")
                .field("username", "TestUser1")
                .field("password", "123456")
                .asEmpty();

        Cookie JSESSIONID = performLogin.getCookies().getNamed("JSESSIONID");

        HttpResponse logout = Unirest.get("http://localhost:7000/logout")
                .cookie(JSESSIONID)
                .asEmpty();

        assertEquals(HttpStatus.OK_200, performSignup.getStatus());
        assertEquals(HttpStatus.OK_200, performLogin.getStatus());
        assertEquals(HttpStatus.OK_200, logout.getStatus());
    }

    @AfterAll
    public void afterAll() {
        javalin.stop();
    }
}
