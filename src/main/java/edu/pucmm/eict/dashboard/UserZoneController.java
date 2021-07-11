package edu.pucmm.eict.dashboard;

import edu.pucmm.eict.common.Controller;
import edu.pucmm.eict.reports.AdminReport;
import edu.pucmm.eict.reports.ReportService;
import edu.pucmm.eict.users.Role;
import edu.pucmm.eict.users.User;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Set;

public class UserZoneController extends Controller {

    private final ReportService reportService;

    public UserZoneController(Javalin app) {
        super(app);
        this.reportService = ReportService.getInstance();
    }

    private void UserZoneView(Context ctx) {
        User user = ctx.sessionAttribute("user");
        var data = new HashMap<String, Object>();
        data.put("user", user);
        ctx.status(200).render("templates/dashboard/user/dashboard.vm", data);
    }

    private void AdminPanelView(Context ctx) {
        AdminReport adminReport = reportService.adminReport();
        User user = ctx.sessionAttribute("user");
        var data = new HashMap<String, Object>();
        data.put("adminReport", adminReport);
        data.put("user", user);
        ctx.status(200).render("templates/dashboard/admin/dashboard.vm", data);
    }

    private void allUsersView(Context ctx) {
        User user = ctx.sessionAttribute("user");
        var data = new HashMap<String, Object>();
        data.put("user", user);
        ctx.status(200).render("templates/dashboard/admin/users.vm", data);
    }

    private void AllLinksView(Context ctx) {
        User user = ctx.sessionAttribute("user");
        var data = new HashMap<String, Object>();
        data.put("user", user);
        ctx.status(200).render("templates/dashboard/admin/urls.vm", data);
    }

    @Override
    public void applyRoutes() {
        app.get("/app/dashboard", this::UserZoneView, Set.of(Role.APP_USER));
        app.get("/app/admin-panel", this::AdminPanelView, Set.of(Role.ADMIN));
        app.get("/app/admin-panel/users", this::allUsersView, Set.of(Role.ADMIN));
        app.get("/app/admin-panel/urls", this::AllLinksView, Set.of(Role.ADMIN));
    }
}