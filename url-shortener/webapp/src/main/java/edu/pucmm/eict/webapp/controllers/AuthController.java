package edu.pucmm.eict.webapp.controllers;

import edu.pucmm.eict.users.AuthService;
import edu.pucmm.eict.users.BadCredentialsException;
import edu.pucmm.eict.users.User;
import edu.pucmm.eict.users.UserService;
import edu.pucmm.eict.webapp.configuration.SessionFlash;
import io.javalin.Javalin;
import io.javalin.core.validation.Validator;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;

import javax.inject.Inject;
import java.util.*;

public class AuthController extends BaseRoute {

    private final UserService userService;
    private final AuthService authService;
    private final SessionFlash sessionFlash;

    @Inject
    public AuthController(Javalin app, UserService userService, AuthService authService, SessionFlash sessionFlash) {
        super(app);
        this.userService = userService;
        this.authService = authService;
        this.sessionFlash = sessionFlash;
    }

    private Map<String, Object> loginViewData(Context ctx) {
        Map<String, Object> data = new HashMap<>();
        Boolean successLogout = sessionFlash.get("successLogout", ctx);
        Boolean successSignup = sessionFlash.get("successOnSignup", ctx);
        Boolean errorOnLogin = sessionFlash.get("errorOnLogin", ctx);
        data.put("errorOnLogin", errorOnLogin);
        data.put("successSignup", successSignup);
        data.put("successLogout", successLogout);
        return data;
    }

    public void loginView(Context ctx) {
        Map<String, Object> viewData = this.loginViewData(ctx);
        ctx.render("login.html", viewData);
    }

    public void login(Context ctx) {
        String username = ctx.formParam("username", String.class).get();
        String password = ctx.formParam("password", String.class).get();
        String rememberMe = ctx.formParam("remember-me");
        User user = authService.login(username, password);
        if (rememberMe != null) {
            String secret = authService.assignSecret(user);
            int time = 60 * 60 * 24 * 7;
            ctx.cookie("remember-me", secret, time);
        }
        ctx.sessionAttribute("user", user);

        // for security purposes, always change the session id when logging in
        ctx.req.changeSessionId();
    }


    private Map<String, Object> signupViewData(Context ctx) {
        Map<String, Object> data = new HashMap<>();
        Boolean errorOnSignup = sessionFlash.get("errorOnSignup", ctx);
        Map<String, List<String>> signupFormErrors = sessionFlash.get("signupFormErrors", ctx);
        // Map could be null and ruin the template
        if(signupFormErrors == null) {
            signupFormErrors = new HashMap<>();
        }
        data.put("errorOnSignup", errorOnSignup);
        data.put("signupFormErrors", signupFormErrors);
        return data;
    }

    public void signupView(Context ctx) {
        Map<String, Object> data = signupViewData(ctx);
        ctx.render("signup.html", data);
    }

    @SuppressWarnings("rawtypes")
    private Map<String, List<String>> validateUserForm(Context ctx) {
        var email = ctx.formParam("email", String.class)
                .check(s -> userService.findByEmail(s).isEmpty(), "There's an account associated with this email.")
                .check(s -> !s.isBlank(), "Email can't be empty.")
                .check(s -> s.length() < 320, "Email can't be larger than 320 characters.");

        var username = ctx.formParam("username", String.class)
                .check(s -> userService.findByUsername(s).isEmpty(), "There's an account associated with this username.")
                .check(s -> !s.isBlank(), "username can't be blank.")
                .check(s -> s.length() < 50, "username can't be larger than 50 characters.");

        var password = ctx.formParam("password", String.class)
                .check(s -> !s.isBlank(), "password can't be blank.")
                .check(s -> s.length() < 26, "password can't be larger than 26 characters.");

        var name = ctx.formParam("name", String.class)
                .check(s -> !s.isBlank(), "name can't be blank.")
                .check(s -> s.length() < 100, "name can't be larger than 100 characters.");

        var lastname = ctx.formParam("lastname", String.class)
                .check(s -> !s.isBlank(), "lastname can't be blank.")
                .check(s -> s.length() < 100, "lastname can't be larger than 100 characters.");

        List<Validator> validators = List.of(email, username, password, name, lastname);
        Map<String, List<String>> errors =  Validator.collectErrors(email, username, password, name, lastname);
        validators.forEach(validator -> {
            if(validator.getValue() == null) {
                String key = validator.getKey();
                errors.computeIfAbsent(key, k -> new ArrayList<>()).add(key + " can't be empty.");
            }
        });
        return errors;
    }

    private User parseUserFromForm(Context ctx) {
        String username = ctx.formParam("username");
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String name = ctx.formParam("name");
        String lastname = ctx.formParam("lastname");
        return new User(username, email, password, name, lastname);
    }

    public void signup(Context ctx) {
        Map<String, List<String>> errors = validateUserForm(ctx);
        if (!errors.isEmpty()) {
            sessionFlash.add("errorOnSignup", true, ctx);
            sessionFlash.add("signupFormErrors", errors, ctx);
            ctx.redirect("/signup", HttpStatus.SEE_OTHER_303);
            return;
        }
        User user = parseUserFromForm(ctx);
        userService.create(user);
        sessionFlash.add("successOnSignup", true, ctx);
        ctx.redirect("/login", HttpStatus.SEE_OTHER_303);
    }

    public void logout(Context ctx) {
        User user = ctx.sessionAttribute("user");
        if(user != null) {
            authService.invalidateSecret(user);
            ctx.req.getSession().invalidate();
            ctx.removeCookie("remember-me");
            ctx.redirect("/");
        }
    }

    public void handleBadCredentialsException(Exception ex, Context ctx) {
        sessionFlash.add("errorOnLogin", true, ctx);
        ctx.redirect("/login", HttpStatus.SEE_OTHER_303);
    }

    @Override
    public void applyRoutes() {
        app.get("/login", this::loginView);
        app.post("/login/process", this::login);
        app.get("/signup", this::signupView);
        app.post("/signup/process", this::signup);
        app.get("/logout", this::logout);
        app.exception(BadCredentialsException.class, this::handleBadCredentialsException);
    }
}
