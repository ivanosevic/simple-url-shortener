package edu.pucmm.eict.urlshortener.webapp.controllers;

import edu.pucmm.eict.urlshortener.persistence.Page;
import edu.pucmm.eict.urlshortener.reports.AdminReport;
import edu.pucmm.eict.urlshortener.reports.AdminReportService;
import edu.pucmm.eict.urlshortener.urls.ShortUrl;
import edu.pucmm.eict.urlshortener.urls.ShortUrlService;
import edu.pucmm.eict.urlshortener.users.RoleList;
import edu.pucmm.eict.urlshortener.users.User;
import edu.pucmm.eict.urlshortener.webapp.ShortUrlDto;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.modelmapper.ModelMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminZoneController extends BaseController {

    private final AdminReportService adminReportService;
    private final ShortUrlService shortUrlService;
    private final ModelMapper modelMapper;

    public AdminZoneController(Javalin app, AdminReportService adminReportService, ShortUrlService shortUrlService, ModelMapper modelMapper) {
        super(app);
        this.adminReportService = adminReportService;
        this.shortUrlService = shortUrlService;
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

    private void adminView(Context ctx) {
        Map<String, Object> data = getAdminViewData(ctx);
        ctx.render("admin/dashboard.html", data);
    }

    private Map<String, Object> getAllUrlsViewData(Context ctx) {
        final int page = ctx.queryParam("page", Integer.class, "1").get();
        final int size = 10;
        Map<String, Object> data = new HashMap<>();
        User user = ctx.sessionAttribute("user");
        // We must fetch from the database, and do the transformation to the DTO
        Page<ShortUrl> shortUrlPage = shortUrlService.findPaged(page, size);
        List<ShortUrlDto> shortUrlDtoList = shortUrlPage.getResults().stream()
                .map(shortUrl -> modelMapper.map(shortUrl, ShortUrlDto.class)).collect(Collectors.toList());
        Page<ShortUrlDto> shortUrlDtoPage = new Page<>(shortUrlPage.getTotalPages(), shortUrlPage.getCurrentPage(),
                shortUrlPage.isFirst(), shortUrlPage.isLast(), shortUrlDtoList);
        data.put("urlPage", shortUrlDtoPage);
        data.put("user", user);
        return data;
    }

    private void allUrlsView(Context ctx) {
        Map<String, Object> data = getAllUrlsViewData(ctx);
        ctx.render("admin/urls.html", data);
    }

    private void myUrlsView(Context ctx) {

    }

    private void allUsersView(Context ctx) {

    }

    @Override
    public void applyRoutes() {
        app.get("/admin-panel", this::adminView, Set.of(RoleList.ADMIN));
        app.get("/admin-panel/urls/all", this::allUrlsView, Set.of(RoleList.ADMIN));
        app.get("/admin-panel/urls", this::myUrlsView, Set.of(RoleList.ADMIN));
        app.get("/admin-panel/users/all", this::allUsersView, Set.of(RoleList.ADMIN));
    }
}
