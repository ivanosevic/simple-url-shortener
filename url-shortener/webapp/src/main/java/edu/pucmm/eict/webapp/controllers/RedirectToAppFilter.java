package edu.pucmm.eict.webapp.controllers;

import edu.pucmm.eict.users.RoleList;
import edu.pucmm.eict.users.User;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class RedirectToAppFilter extends BaseRoute {

    private static final Logger log = LoggerFactory.getLogger(RedirectToAppFilter.class);

    @Inject
    public RedirectToAppFilter(Javalin app) {
        super(app);
    }

    private void redirectToUserZone(Context ctx) {
        User user = ctx.sessionAttribute("user");
        if(user != null) {
            if(user.getRoles().contains(RoleList.ADMIN)) {
                ctx.redirect("/admin-panel");
            } else {
                ctx.redirect("/user-zone");
            }
        }
    }

    @Override
    public void applyRoutes() {
        app.after("/", this::redirectToUserZone);
        app.after("/login/process", this::redirectToUserZone);
    }
}
