package edu.pucmm.eict.security;

import edu.pucmm.eict.auth.AuthService;
import edu.pucmm.eict.common.JavalinConfig;
import edu.pucmm.eict.users.User;
import io.javalin.Javalin;

public class SecurityConfig extends JavalinConfig {

    private final AuthService authService;

    public SecurityConfig(Javalin app) {
        super(app);
        authService = AuthService.getInstance();
    }

    @Override
    public void applyConfig() {
        app.config.accessManager((handler, ctx, permittedRoles) -> {
            // Ask for the user in session
            User user = ctx.sessionAttribute("user");

            // Ask for the cookie remember-me
            String rememberMe = ctx.cookie("remember-me");

            // If there's a cookie but there's no session for the user,
            // we must check if the content of the cookie is valid.
            if(rememberMe != null && user == null) {
                var userFromCookie = authService.findUserBySecret(rememberMe);
                userFromCookie.ifPresent(value -> ctx.sessionAttribute("user", value));
            }

            user = ctx.sessionAttribute("user");

            if(permittedRoles.isEmpty()) {
                handler.handle(ctx);
                return;
            }

            if(user == null) {
                ctx.redirect("/app/login");
                return;
            }

            boolean hasPermission = false;
            for(var rol : user.getRoles()) {
                if(permittedRoles.contains(rol)) {
                    hasPermission = true;
                    break;
                }
            }

            if(hasPermission) {
                handler.handle(ctx);
            } else {
                ctx.status(401);
            }
        });
    }
}