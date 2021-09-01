package edu.pucmm.eict.webapp.controllers;

import edu.pucmm.eict.persistence.Page;
import edu.pucmm.eict.reports.AdminReport;
import edu.pucmm.eict.reports.AdminReportService;
import edu.pucmm.eict.urls.ShortUrl;
import edu.pucmm.eict.urls.ShortUrlService;
import edu.pucmm.eict.users.*;
import edu.pucmm.eict.webapp.configuration.SessionFlash;
import edu.pucmm.eict.webapp.dtos.ShortUrlDto;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;
import org.modelmapper.ModelMapper;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminZoneController extends BaseRoute {

    private final AdminReportService adminReportService;
    private final ShortUrlService shortUrlService;
    private final UserService userService;
    private final SessionFlash sessionFlash;
    private final ModelMapper modelMapper;

    @Inject
    public AdminZoneController(Javalin app,
                               AdminReportService adminReportService,
                               ShortUrlService shortUrlService,
                               UserService userService,
                               SessionFlash sessionFlash,
                               ModelMapper modelMapper) {
        super(app);
        this.adminReportService = adminReportService;
        this.shortUrlService = shortUrlService;
        this.userService = userService;
        this.sessionFlash = sessionFlash;
        this.modelMapper = modelMapper;
    }

    private Map<String, Object> getAdminViewData(Context ctx) {
        Map<String, Object> data = new HashMap<>();
        User user = ctx.sessionAttribute("user");
        AdminReport adminReport = adminReportService.fetchReport();
        data.put("adminReport", adminReport);
        data.put("user", user);
        return data;
    }

    private Map<String, Object> getAllUrlsViewData(Context ctx) {
        final int page = ctx.queryParam("page", Integer.class, "1").get();
        final int size = 5;
        Map<String, Object> data = new HashMap<>();
        User user = ctx.sessionAttribute("user");
        // We must fetch from the database, and do the transformation to the DTO
        Page<ShortUrl> shortUrlPage = shortUrlService.findPaged(page, size);
        List<ShortUrlDto> shortUrlDtoList = shortUrlPage.getResults().stream()
                .map(shortUrl -> modelMapper.map(shortUrl, ShortUrlDto.class)).collect(Collectors.toList());
        Page<ShortUrlDto> shortUrlDtoPage = new Page<>(shortUrlPage.getTotalPages(), shortUrlPage.getCurrentPage(),
                shortUrlPage.isFirst(), shortUrlPage.isLast(), shortUrlDtoList);
        // Session Messages
        Boolean successOnDeletingUrl = sessionFlash.get("successOnDeletingUrl", ctx);
        data.put("successOnDeletingUrl", successOnDeletingUrl);
        data.put("urlPage", shortUrlDtoPage);
        data.put("user", user);
        return data;
    }

    private Map<String, Object> getMyUrlsViewData(Context ctx) {
        final int page = ctx.queryParam("page", Integer.class, "1").get();
        final int size = 5;
        Map<String, Object> data = new HashMap<>();
        User user = ctx.sessionAttribute("user");
        Long userId = user.getId();
        // We must fetch from the database, and do the transformation to the DTO
        Page<ShortUrl> shortUrlPage = shortUrlService.findPagedByUserId(userId, page, size);
        List<ShortUrlDto> shortUrlDtoList = shortUrlPage.getResults().stream()
                .map(shortUrl -> modelMapper.map(shortUrl, ShortUrlDto.class)).collect(Collectors.toList());
        Page<ShortUrlDto> shortUrlDtoPage = new Page<>(shortUrlPage.getTotalPages(), shortUrlPage.getCurrentPage(),
                shortUrlPage.isFirst(), shortUrlPage.isLast(), shortUrlDtoList);
        Boolean successOnShortingUrl = sessionFlash.get("successOnShortingUrl", ctx);
        String errorShortingUrl = sessionFlash.get("errorShortingUrl", ctx);
        data.put("errorShortingUrl", errorShortingUrl);
        data.put("successOnShortingUrl", successOnShortingUrl);
        data.put("urlPage", shortUrlDtoPage);
        data.put("user", user);
        return data;
    }

    private Map<String, Object> getAllUsersViewData(Context ctx) {
        final int page = ctx.queryParam("page", Integer.class, "1").get();
        final int size = 5;
        Map<String, Object> data = new HashMap<>();
        User user = ctx.sessionAttribute("user");
        // We must fetch from the database, and do the transformation to the DTO
        Page<User> userPage = userService.findPaged(page, size);
        String successOnUpdatingPrivileges = sessionFlash.get("successOnUpdatingPrivileges", ctx);
        String errorOnDeletingUser = sessionFlash.get("errorOnDeletingUser", ctx);
        String errorOnUpdatingPrivileges = sessionFlash.get("errorOnUpdatingPrivileges", ctx);
        data.put("userPage", userPage);
        data.put("user", user);
        data.put("successOnUpdatingPrivileges", successOnUpdatingPrivileges);
        data.put("errorOnDeletingUser", errorOnDeletingUser);
        data.put("errorOnUpdatingPrivileges", errorOnUpdatingPrivileges);
        return data;
    }

    private void adminView(Context ctx) {
        Map<String, Object> data = getAdminViewData(ctx);
        ctx.render("admin-dashboard.html", data);
    }

    private void deleteUrl(Context ctx) {
        String code = ctx.pathParam("code", String.class).get();
        shortUrlService.delete(code);
        sessionFlash.add("successOnDeletingUrl", true, ctx);
        ctx.redirect("/admin-panel/urls/all", HttpStatus.SEE_OTHER_303);
    }

    private void allUrlsView(Context ctx) {
        Map<String, Object> data = getAllUrlsViewData(ctx);
        ctx.render("all-urls.html", data);
    }

    private void myUrlsView(Context ctx) {
        Map<String, Object> data = getMyUrlsViewData(ctx);
        ctx.render("admin-urls.html", data);
    }

    private void allUsersView(Context ctx) {
        Map<String, Object> data = getAllUsersViewData(ctx);
        ctx.render("all-users.html", data);
    }

    private void deleteUser(Context ctx) {
        String username = ctx.pathParam("username", String.class).get();
        userService.delete(username);
        String messageOnDelete = "User " + username + " was deleted successfully";
        sessionFlash.add("successOnDeletingUser", messageOnDelete, ctx);
        ctx.redirect("/admin-panel/users/all", HttpStatus.SEE_OTHER_303);
    }

    private void grantUserAdminPrivileges(Context ctx) {
        String username = ctx.pathParam("username", String.class).get();
        userService.grantAdminPrivileges(username);
        String messageOnSuccess = "User " + username + " now has Admin privileges.";
        sessionFlash.add("successOnUpdatingPrivileges", messageOnSuccess, ctx);
        ctx.redirect("/admin-panel/users/all", HttpStatus.SEE_OTHER_303);
    }

    private void removeUserAdminPrivileges(Context ctx) {
        String username = ctx.pathParam("username", String.class).get();
        userService.removeAdminPrivileges(username);
        String messageOnSuccess = "User " + username + " admin privileges were removed successfully.";
        sessionFlash.add("successOnUpdatingPrivileges", messageOnSuccess, ctx);
        ctx.redirect("/admin-panel/users/all", HttpStatus.SEE_OTHER_303);
    }

    private void handleUserNotDeletableException(Exception ex, Context ctx) {
        sessionFlash.add("errorOnDeletingUser", ex.getMessage(), ctx);
        ctx.redirect("/admin-panel/users/all", HttpStatus.SEE_OTHER_303);
    }

    private void handleGrantPrivilegeException(Exception ex, Context ctx) {
        sessionFlash.add("errorOnUpdatingPrivileges", ex.getMessage(), ctx);
        ctx.redirect("/admin-panel/users/all", HttpStatus.SEE_OTHER_303);
    }

    private void shortUrlAdminPanel(Context ctx) {
        String url = ctx.formParam("url");
        User user = ctx.sessionAttribute("user");
        shortUrlService.doShort(user, url);
        sessionFlash.add("successOnShortingUrl", true, ctx);
        ctx.redirect("/admin-panel/urls", HttpStatus.SEE_OTHER_303);
    }

    private void shortUrlUserZone(Context ctx) {
        String url = ctx.formParam("url");
        User user = ctx.sessionAttribute("user");
        shortUrlService.doShort(user, url);
        sessionFlash.add("successOnShortingUrl", true, ctx);
        ctx.redirect("/user-zone/urls", HttpStatus.SEE_OTHER_303);
    }

    private void userZoneUrls(Context ctx) {
        Map<String, Object> data = getMyUrlsViewData(ctx);
        ctx.render("user-urls.html", data);
    }


    @Override
    public void applyRoutes() {
        app.get("/admin-panel", this::adminView, Set.of(RoleList.ADMIN));
        app.get("/admin-panel/urls", this::myUrlsView, Set.of(RoleList.ADMIN));
        app.get("/admin-panel/urls/all", this::allUrlsView, Set.of(RoleList.ADMIN));
        app.get("/admin-panel/urls/:code/delete", this::deleteUrl, Set.of(RoleList.ADMIN));
        app.get("/admin-panel/users/all", this::allUsersView, Set.of(RoleList.ADMIN));
        app.get("/admin-panel/users/:username/delete", this::deleteUser, Set.of(RoleList.ADMIN));
        app.get("/admin-panel/users/:username/grant-privileges", this::grantUserAdminPrivileges, Set.of(RoleList.ADMIN));
        app.get("/admin-panel/users/:username/remove-privileges", this::removeUserAdminPrivileges, Set.of(RoleList.ADMIN));
        app.post("/admin-panel/short-url", this::shortUrlAdminPanel, Set.of(RoleList.ADMIN));
        app.post("/user-zone/short-url", this::shortUrlUserZone, Set.of(RoleList.APP_USER));
        app.get("/user-zone", ctx -> ctx.redirect("/user-zone/urls"));
        app.get("/user-zone/urls", this::userZoneUrls, Set.of(RoleList.APP_USER));
        app.exception(UserNotDeletableException.class, this::handleUserNotDeletableException);
        app.exception(GrantPrivilegesException.class, this::handleGrantPrivilegeException);
    }
}
