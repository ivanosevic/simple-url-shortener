package edu.pucmm.eict.urlshortener.restapi.config;

import edu.pucmm.eict.urlshortener.restapi.responses.ApiError;
import edu.pucmm.eict.urlshortener.users.User;
import edu.pucmm.eict.urlshortener.users.UserDao;
import io.javalin.core.security.AccessManager;
import io.javalin.core.security.Role;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class SecurityConfig implements AccessManager {

    private final JwtUtil jwtUtil;
    private final UserDao userDao;

    public SecurityConfig(JwtUtil jwtUtil, UserDao userDao) {
        this.jwtUtil = jwtUtil;
        this.userDao = userDao;
    }

    @Override
    public void manage(@NotNull Handler handler, @NotNull Context ctx, @NotNull Set<Role> permittedRoles) throws Exception {

        if(permittedRoles.isEmpty()) {
            handler.handle(ctx);
            return;
        }

        User user = null;
        String token = jwtUtil.parseJwt(ctx);
        if(token != null && jwtUtil.validateJwtToken(token)) {
            String username = jwtUtil.getUserFromToken(token);
            var userDb = userDao.findByUsername(username);
            if(userDb.isPresent()) {
                user = userDb.get();
            }
        }

        if (user == null) {
            ApiError apiError = new ApiError("Unauthorized", "You are not authorized to access this resource.");
            ctx.status(HttpStatus.UNAUTHORIZED_401).json(apiError);
            return;
        }

        boolean hasPermission = false;
        for (var rol : user.getRoles()) {
            if (permittedRoles.contains(rol)) {
                hasPermission = true;
                break;
            }
        }

        if (hasPermission) {
            handler.handle(ctx);
        } else {
            ApiError apiError = new ApiError("Unauthorized", "You are not authorized to access this resource.");
            ctx.status(HttpStatus.UNAUTHORIZED_401).json(apiError);
        }
    }
}
