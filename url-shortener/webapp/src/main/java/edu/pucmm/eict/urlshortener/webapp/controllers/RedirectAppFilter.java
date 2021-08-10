package edu.pucmm.eict.urlshortener.webapp.controllers;

import edu.pucmm.eict.urlshortener.users.RoleList;
import edu.pucmm.eict.urlshortener.users.User;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class RedirectAppFilter extends BaseController {

    public RedirectAppFilter(Javalin app) {
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
