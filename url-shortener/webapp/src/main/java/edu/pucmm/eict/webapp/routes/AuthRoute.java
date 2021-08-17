package edu.pucmm.eict.webapp.routes;

import edu.pucmm.eict.users.BadCredentialsException;
import edu.pucmm.eict.webapp.controllers.AuthController;
import io.javalin.Javalin;

public class AuthRoute extends BaseRoute {

    private final AuthController authController;

    public AuthRoute(Javalin app, AuthController authController) {
        super(app);
        this.authController = authController;
    }

    @Override
    public void applyRoutes() {
        app.get("/login", authController::loginView);
        app.post("/login/process", authController::login);
        app.get("/signup", authController::signupView);
        app.post("/signup/process", authController::signup);
        app.get("/logout", authController::logout);
        app.exception(BadCredentialsException.class, authController::handleBadCredentialsException);
    }
}
