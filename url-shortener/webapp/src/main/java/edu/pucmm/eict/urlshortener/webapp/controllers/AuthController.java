package edu.pucmm.eict.urlshortener.webapp.controllers;

import edu.pucmm.eict.urlshortener.users.AuthService;
import edu.pucmm.eict.urlshortener.users.BadCredentialsException;
import edu.pucmm.eict.urlshortener.users.User;
import edu.pucmm.eict.urlshortener.webapp.SessionFlash;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class AuthController extends BaseController {

    private final AuthService authService;
    private final SessionFlash sessionFlash;

    public AuthController(Javalin app, AuthService authService, SessionFlash sessionFlash) {
        super(app);
        this.authService = authService;
        this.sessionFlash = sessionFlash;
    }

    private Map<String, Object> getLoginViewData(Context ctx) {
        Map<String, Object> data = new HashMap<>();
        Boolean successLogout = sessionFlash.get("successLogout", ctx);
        Boolean successSignup = sessionFlash.get("successSignup", ctx);
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
        String rememberMe = ctx.formParam("remember-me", String.class).get();
        User user = authService.login(username, password);
        if(rememberMe != null) {
            String secret = authService.assignSecret(user);
            int time = 60 * 60 * 24 * 7;
            ctx.cookie("remember-me", secret, time);
        }

        ctx.sessionAttribute("user", user);

        // for security purposes, always change the session id when logging in
        ctx.req.changeSessionId();
    }

    private void showSignupView(Context ctx) {
        ctx.render("signup.html");
    }

    private void processSignup(Context ctx) {

    }

    private void handleBadCredentialsException(Exception ex, Context ctx) {
        sessionFlash.add("errorOnLogin", true, ctx);
        ctx.redirect("/login", HttpStatus.SEE_OTHER_303);
    }

    @Override
    public void applyRoutes() {
        app.get("/login", this::showLoginView);
        app.post("/login/process", this::processLogin);
        app.get("/signup", this::showSignupView);
        app.post("/signup/process", this::processSignup);
        app.exception(BadCredentialsException.class, this::handleBadCredentialsException);
    }
}
