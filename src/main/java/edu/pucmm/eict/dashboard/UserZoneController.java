package edu.pucmm.eict.dashboard;

import edu.pucmm.eict.common.ApplicationProperties;
import edu.pucmm.eict.common.Controller;
import edu.pucmm.eict.common.Page;
import edu.pucmm.eict.reports.AdminReport;
import edu.pucmm.eict.reports.ReportService;
import edu.pucmm.eict.urls.dao.ShortURLDao;
import edu.pucmm.eict.urls.models.ShortURL;
import edu.pucmm.eict.urls.services.ShortURLService;
import edu.pucmm.eict.users.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Set;

public class UserZoneController extends Controller {

    private static final Logger log = LoggerFactory.getLogger(UserZoneController.class);
    private final ReportService reportService;
    private final UserDao userDao;
    private final UserService userService;
    private final ShortURLDao shortURLDao;
    private final ShortURLService shortURLService;
    private final Integer DEFAULT_USER_PAGE_SIZE = 5;
    private final Integer DEFAULT_URL_PAGE_SIZE = 15;

    public UserZoneController(Javalin app) {
        super(app);
        this.reportService = ReportService.getInstance();
        this.userDao = UserDao.getInstance();
        this.userService = UserService.getInstance();
        this.shortURLDao = ShortURLDao.getInstance();
        this.shortURLService = ShortURLService.getInstance();
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
        Integer page = ctx.queryParam("page", Integer.class, "1").get();
        String deleteError = ctx.sessionAttribute("privilege_error");
        String privilegeError = ctx.sessionAttribute("delete_user_error");
        var users = userDao.findPaged(page, DEFAULT_USER_PAGE_SIZE, user.getId());
        var data = new HashMap<String, Object>();
        data.put("user", user);
        data.put("userPage", users);
        data.put("deleteError", deleteError);
        data.put("privilegeError", privilegeError);
        ctx.status(200).render("templates/dashboard/admin/users.vm", data);
        ctx.req.getSession().removeAttribute("privilege_error");
        ctx.req.getSession().removeAttribute("delete_user_error");
    }

    private void grantOrRemovePrivileges(Context ctx) {
        Long id = ctx.pathParam("id", Long.class).get();
        userService.grantOrRemovePrivileges(id);
        ctx.redirect("/app/admin-panel/users");
    }

    private void deleteUser(Context ctx) {
        Long id = ctx.pathParam("id", Long.class).get();
        userService.delete(id);
        ctx.redirect("/app/admin-panel/users");
    }

    private void AllLinksView(Context ctx) {
        User user = ctx.sessionAttribute("user");
        Integer page = ctx.queryParam("page", Integer.class, "1").get();
        Page<ShortURL> shortenURLs = shortURLDao.findPaged(page, DEFAULT_URL_PAGE_SIZE);
        var data = new HashMap<String, Object>();
        data.put("user", user);
        data.put("urlPage", shortenURLs);
        data.put("baseUrl", ApplicationProperties.getInstance().getDomain());
        ctx.status(200).render("templates/dashboard/admin/urls.vm", data);
        ctx.req.getSession().removeAttribute("delete_user_error");
    }

    private void deleteURL(Context ctx) {
        String code = ctx.pathParam("code", String.class).get();
        shortURLService.delete(code);
        ctx.redirect("/app/admin-panel/urls");
        ctx.sessionAttribute("deleted_url_successfully", "URL was deleted successfully.");
    }

    private void handleUserDeleteException(Exception exception, Context ctx) {
        ctx.sessionAttribute("delete_user_error", exception.getMessage());
        ctx.redirect("/app/admin-panel/users");
    }

    private void handlePrivilegeException(Exception exception, Context ctx) {
        ctx.sessionAttribute("privilege_error", exception.getMessage());
        ctx.redirect("/app/admin-panel/users");
    }

    @Override
    public void applyRoutes() {
        app.get("/app/dashboard", this::UserZoneView, Set.of(Role.APP_USER));
        app.get("/app/admin-panel", this::AdminPanelView, Set.of(Role.ADMIN));
        app.get("/app/admin-panel/users", this::allUsersView, Set.of(Role.ADMIN));
        app.get("/app/admin-panel/urls", this::AllLinksView, Set.of(Role.ADMIN));
        app.get("/app/admin-panel/users/:id/grant-privileges", this::grantOrRemovePrivileges, Set.of(Role.ADMIN));
        app.get("/app/admin-panel/users/:id/remove-privileges", this::grantOrRemovePrivileges, Set.of(Role.ADMIN));
        app.get("/app/admin-panel/users/:id/delete", this::deleteUser, Set.of(Role.ADMIN));
        app.get("/app/admin-panel/urls/:code/delete", this::deleteURL);
        app.exception(UserDeleteException.class, this::handleUserDeleteException);
        app.exception(UserGrantPrivilegeException.class, this::handlePrivilegeException);
    }
}