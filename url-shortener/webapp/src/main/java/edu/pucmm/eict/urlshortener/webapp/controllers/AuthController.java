package edu.pucmm.eict.urlshortener.webapp.controllers;

import edu.pucmm.eict.urlshortener.users.AuthService;
import edu.pucmm.eict.urlshortener.users.BadCredentialsException;
import edu.pucmm.eict.urlshortener.users.User;
import edu.pucmm.eict.urlshortener.users.UserService;
import edu.pucmm.eict.urlshortener.webapp.services.SessionFlash;
import io.javalin.Javalin;
import io.javalin.core.validation.Validator;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthController extends BaseController {

    private final UserService userService;
    private final AuthService authService;
    private final SessionFlash sessionFlash;

    public AuthController(Javalin app, UserService userService, AuthService authService, SessionFlash sessionFlash) {
        super(app);
        this.userService = userService;
        this.authService = authService;
        this.sessionFlash = sessionFlash;
    }

    private Map<String, Object> getLoginViewData(Context ctx) {
        Map<String, Object> data = new HashMap<>();
        Boolean successLogout = sessionFlash.get("successLogout", ctx);
        Boolean successSignup = sessionFlash.get("successOnSignup", ctx);
        Boolean errorOnLogin = sessionFlash.get("errorOnLogin", ctx);
        data.put("errorOnLogin", errorOnLogin);
        data.put("successSignup", successSignup);
        data.put("successLogout", successLogout);
        return data;
    }

    private void showLoginView(Context ctx) {
        Map<String, Object> data = getLoginViewData(ctx);
        ctx.render("login.html", data);
    }

    private void processLogin(Context ctx) {
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

    private Map<String, Object> getSignupViewData(Context ctx) {
        Map<String, Object> data = new HashMap<>();
        Boolean errorOnSignup = sessionFlash.get("errorOnSignup", ctx);
        Map<String, List<String>> signupFormErrors = sessionFlash.get("signupFormErrors", ctx);
        data.put("errorOnSignup", errorOnSignup);
        data.put("signupFormErrors", signupFormErrors);
        return data;
    }

    private void showSignupView(Context ctx) {
        Map<String, Object> data = getLoginViewData(ctx);
        ctx.render("signup.html", data);
    }

    private void processSignup(Context ctx) {
        var email = ctx.formParam("email", String.class)
                .check(s -> userService.findByUsername(s).isEmpty(), "There's an account associated with this email.")
                .check(s -> !s.isBlank(), "Email can't be blank.")
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

        Map<String, List<String>> errors = Validator.collectErrors(email, username, password, name, lastname);
        if (!errors.isEmpty()) {
            sessionFlash.add("errorOnSignup", true, ctx);
            sessionFlash.add("signupFormErrors", errors, ctx);
            ctx.redirect("/signup", HttpStatus.SEE_OTHER_303);
            return;
        }

        User user = new User(username.getValue(), email.getValue(),
                password.getValue(), name.getValue(), lastname.getValue());
        userService.create(user);
        sessionFlash.add("successOnSignup", true, ctx);
        ctx.redirect("/login", HttpStatus.SEE_OTHER_303);
    }

    private void handleBadCredentialsException(Exception ex, Context ctx) {
        sessionFlash.add("errorOnLogin", true, ctx);
        ctx.redirect("/login", HttpStatus.SEE_OTHER_303);
    }

    private void logout(Context ctx) {
        User user = ctx.sessionAttribute("user");
        if(user != null) {
            authService.invalidateSecret(user);
            ctx.req.getSession().invalidate();
            ctx.removeCookie("remember-me");
            ctx.redirect("/");
        }
    }

    @Override
    public void applyRoutes() {
        app.get("/login", this::showLoginView);
        app.post("/login/process", this::processLogin);
        app.get("/signup", this::showSignupView);
        app.post("/signup/process", this::processSignup);
        app.get("/logout", this::logout);
        app.exception(BadCredentialsException.class, this::handleBadCredentialsException);
    }
}
