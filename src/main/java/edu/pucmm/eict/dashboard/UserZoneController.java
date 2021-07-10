package edu.pucmm.eict.dashboard;

import edu.pucmm.eict.common.Controller;
import edu.pucmm.eict.users.Role;
import edu.pucmm.eict.users.User;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Set;

public class UserZoneController extends Controller {

    public UserZoneController(Javalin app) {
        super(app);
    }

    private void UserZoneView(Context ctx) {
        User user = ctx.sessionAttribute("user");
        var data = new HashMap<String, Object>();
        data.put("user", user);
        ctx.status(200).render("templates/dashboard/user/dashboard.vm", data);
    }

    private void AdminPanelView(Context ctx) {
        User user = ctx.sessionAttribute("user");
        var data = new HashMap<String, Object>();
        data.put("user", user);
        ctx.status(200).render("templates/dashboard/admin/dashboard.vm", data);
    }

    @Override
    public void applyRoutes() {
        app.get("/app/dashboard", this::UserZoneView, Set.of(Role.APP_USER));
        app.get("/app/admin-panel", this::AdminPanelView, Set.of(Role.ADMIN));
    }
}