package edu.pucmm.eict.dashboard;

import edu.pucmm.eict.common.Controller;
import edu.pucmm.eict.users.Role;
import edu.pucmm.eict.users.User;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class DashboardInterceptor extends Controller {

    public DashboardInterceptor(Javalin app) {
        super(app);
    }

    private void performRedirect(Context ctx) {
        // If we found a session, that means we redirect user to
        // his dashboard.
        User user = ctx.sessionAttribute("user");
        if(user != null) {
            if(user.getRoles().contains(Role.ADMIN)) {
                ctx.redirect("/app/admin-panel");
            } else {
                ctx.redirect("/app/dashboard");
            }
        }
    }

    @Override
    public void applyRoutes() {
        app.after("/", this::performRedirect);
    }
}