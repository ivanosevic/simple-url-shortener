package edu.pucmm.eict.webapp.configuration;

import edu.pucmm.eict.users.AuthService;
import edu.pucmm.eict.users.User;
import io.javalin.core.security.AccessManager;
import io.javalin.core.security.Role;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.Set;

public class SecurityConfig implements AccessManager {

    private final AuthService authService;

    @Inject
    public SecurityConfig(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void manage(@NotNull Handler handler, @NotNull Context ctx, @NotNull Set<Role> permittedRoles) throws Exception {

        User user = ctx.sessionAttribute("user");
        String rememberMe = ctx.cookie("remember-me");
        // If there's a cookie but there's no session for the user,
        // we must check if the content of the cookie is valid.
        if(rememberMe != null && user == null) {
            var userFromCookie = authService.findUserBySecret(rememberMe);
            userFromCookie.ifPresent(value -> ctx.sessionAttribute("user", value));
        }

        user = ctx.sessionAttribute("user");

        // If the route is accessible to everyone, no need
        // to check session
        if(permittedRoles.isEmpty()) {
            handler.handle(ctx);
            return;
        }

        if(user == null) {
            ctx.redirect("/login");
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
            ctx.status(HttpStatus.FORBIDDEN_403);
        }
    }
}
