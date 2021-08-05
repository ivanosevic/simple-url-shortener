package edu.pucmm.eict.restapi.security;

import edu.pucmm.eict.restapi.apiresponses.ApiError;
import edu.pucmm.eict.restapi.common.BaseConfiguration;
import edu.pucmm.eict.users.User;
import edu.pucmm.eict.users.UserDao;
import io.javalin.Javalin;
import org.eclipse.jetty.http.HttpStatus;

public class ApiSecurity extends BaseConfiguration {

    private final UserDao userDao;
    private final JwtUtils jwtUtils;

    public ApiSecurity(Javalin app) {
        super(app);
        this.userDao = UserDao.getInstance();
        this.jwtUtils = JwtUtils.getInstance();
    }

    @Override
    public void applyConfiguration() {
        app.config.accessManager((handler, ctx, permittedRoles) -> {
            // There are no roles, handle the request normally...
            if (permittedRoles.isEmpty()) {
                handler.handle(ctx);
                return;
            }

            User user = null;

            // We don't ask for the user in session, we ask for the token in the header
            String jwt = jwtUtils.parseJwt(ctx);
            if(jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUsernameFromToken(jwt);
                var dbUser = userDao.findByUsername(username);
                if(dbUser.isPresent()) {
                    user = dbUser.get();
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
        });
    }
}
