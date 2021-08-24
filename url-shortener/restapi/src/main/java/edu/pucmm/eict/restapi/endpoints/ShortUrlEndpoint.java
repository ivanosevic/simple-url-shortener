package edu.pucmm.eict.restapi.endpoints;

import edu.pucmm.eict.persistence.Page;
import edu.pucmm.eict.reports.UrlReport;
import edu.pucmm.eict.reports.UrlReportService;
import edu.pucmm.eict.restapi.config.JwtUtils;
import edu.pucmm.eict.restapi.dtos.ShortUrlDto;
import edu.pucmm.eict.restapi.dtos.UrlReportDto;
import edu.pucmm.eict.restapi.forms.ShortUrlForm;
import edu.pucmm.eict.restapi.reponses.ApiError;
import edu.pucmm.eict.urls.InvalidUrlException;
import edu.pucmm.eict.urls.ShortUrl;
import edu.pucmm.eict.urls.ShortUrlService;
import edu.pucmm.eict.users.RoleList;
import edu.pucmm.eict.users.User;
import edu.pucmm.eict.users.UserDao;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.*;
import org.eclipse.jetty.http.HttpStatus;
import org.modelmapper.ModelMapper;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShortUrlEndpoint extends BaseEndpoint {

    private final UserDao userDao;
    private final ShortUrlService shortUrlService;
    private final UrlReportService urlStatisticsService;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;

    @Inject
    public ShortUrlEndpoint(Javalin app, UserDao userDao,
                            ShortUrlService shortUrlService,
                            UrlReportService urlStatisticsService,
                            ModelMapper modelMapper,
                            JwtUtils jwtUtils) {
        super(app);
        this.userDao = userDao;
        this.shortUrlService = shortUrlService;
        this.urlStatisticsService = urlStatisticsService;
        this.modelMapper = modelMapper;
        this.jwtUtils = jwtUtils;
    }

    @OpenApi(
            path = "/users/:username/urls",
            method = HttpMethod.GET,
            tags = ("Users"),
            summary = "Gets the URLs of a specific user.",
            security = {
                    @OpenApiSecurity(name = "ADMIN")
            },
            pathParams = {
              @OpenApiParam(name = "username", required = true, description = "Target user.")
            },
            queryParams = {
                    @OpenApiParam(name = "page", required = true, description = "Page of the URLs you want to access.")
            },
            responses = {
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = Page.class)),
                    @OpenApiResponse(status = "404", content = @OpenApiContent(from = ApiError.class)),
                    @OpenApiResponse(status = "400", content = @OpenApiContent(from = ApiError.class)),
                    @OpenApiResponse(status = "401", content = @OpenApiContent(from = ApiError.class)),
            }
    )
    private void getUrlsByUser(Context ctx) {
        String username = ctx.pathParam("username", String.class).get();
        final int page = ctx.queryParam("page", Integer.class, "1").get();
        final int size = 5;
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username " + username + " was not found."));
        Long userId = user.getId();
        Page<ShortUrl> shortUrlPage = shortUrlService.findPagedByUserId(userId, page, size);

        // We must map the entities in the endpoint...
        List<ShortUrlDto> shortUrlDtoList = new ArrayList<>();
        shortUrlPage.getResults().forEach(shortUrl -> {
            Long shortUrlId = shortUrl.getId();
            UrlReport urlReport = urlStatisticsService.fetchReport(shortUrlId);
            UrlReportDto urlReportDto = modelMapper.map(urlReport, UrlReportDto.class);
            ShortUrlDto shortUrlDto = modelMapper.map(shortUrl, ShortUrlDto.class);
            shortUrlDto.setStatistics(urlReportDto);
            shortUrlDtoList.add(shortUrlDto);
        });

        // We create our own page and return it
        Page<ShortUrlDto> shortUrlDtoPage = new Page<>(shortUrlPage.getTotalPages(), shortUrlPage.getCurrentPage(), shortUrlPage.isFirst(),
                shortUrlPage.isLast(), shortUrlDtoList);
        ctx.status(HttpStatus.OK_200).json(shortUrlDtoPage);
    }

    @OpenApi(
            path = "/me/urls",
            method = HttpMethod.GET,
            tags = ("Users"),
            summary = "Gets the URLs of the logged user.",
            security = {
                    @OpenApiSecurity(name = "APP_USER"),
                    @OpenApiSecurity(name = "ADMIN")
            },
            queryParams = {
                    @OpenApiParam(name = "page", required = true, description = "Page of the URLs you want to access.")
            },
            responses = {
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = Page.class)),
                    @OpenApiResponse(status = "404", content = @OpenApiContent(from = ApiError.class)),
                    @OpenApiResponse(status = "400", content = @OpenApiContent(from = ApiError.class)),
                    @OpenApiResponse(status = "401", content = @OpenApiContent(from = ApiError.class)),
            }
    )
    private void getMyUrls(Context ctx) {
        final int size = 5;
        final int page = ctx.queryParam("page", Integer.class, "1").get();
        String token = jwtUtils.parseJwt(ctx);
        String username = jwtUtils.getUserFromToken(token);
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username " + username + " was not found."));
        Long userId = user.getId();

        Page<ShortUrl> shortUrlPage = shortUrlService.findPagedByUserId(userId, page, size);
        // We must map the entities in the endpoint...
        List<ShortUrlDto> shortUrlDtoList = new ArrayList<>();
        shortUrlPage.getResults().forEach(shortUrl -> {
            Long shortUrlId = shortUrl.getId();
            UrlReport urlReport = urlStatisticsService.fetchReport(shortUrlId);
            UrlReportDto urlReportDto = modelMapper.map(urlReport, UrlReportDto.class);
            ShortUrlDto shortUrlDto = modelMapper.map(shortUrl, ShortUrlDto.class);
            shortUrlDto.setStatistics(urlReportDto);
            shortUrlDtoList.add(shortUrlDto);
        });

        // We create our own page and return it
        Page<ShortUrlDto> shortUrlDtoPage = new Page<>(shortUrlPage.getTotalPages(), shortUrlPage.getCurrentPage(), shortUrlPage.isFirst(),
                shortUrlPage.isLast(), shortUrlDtoList);
        ctx.status(HttpStatus.OK_200).json(shortUrlDtoPage);
    }

    @OpenApi(
            path = "/short-url",
            method = HttpMethod.POST,
            tags = ("Short URLs"),
            summary = "Shorts a given URL",
            security = {
                    @OpenApiSecurity(name = "APP_USER"),
                    @OpenApiSecurity(name = "ADMIN")
            },
            requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = ShortUrlForm.class), required = true),
            responses = {
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = ShortUrlDto.class)),
                    @OpenApiResponse(status = "404", content = @OpenApiContent(from = ApiError.class)),
                    @OpenApiResponse(status = "400", content = @OpenApiContent(from = ApiError.class)),
                    @OpenApiResponse(status = "401", content = @OpenApiContent(from = ApiError.class)),
            }
    )
    private void shortUrl(Context ctx) {
        String token = jwtUtils.parseJwt(ctx);
        String username = jwtUtils.getUserFromToken(token);
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username " + username + " was not found."));
        ShortUrlForm form = ctx.bodyValidator(ShortUrlForm.class).get();
        ShortUrl shortUrl = shortUrlService.doShort(user, form.getUrl());
        // Now parse the short url
        Long shortUrlId = shortUrl.getId();
        UrlReport urlReport = urlStatisticsService.fetchReport(shortUrlId);
        UrlReportDto urlReportDto = modelMapper.map(urlReport, UrlReportDto.class);
        ShortUrlDto shortUrlDto = modelMapper.map(shortUrl, ShortUrlDto.class);
        shortUrlDto.setStatistics(urlReportDto);
        ctx.status(HttpStatus.OK_200).json(shortUrlDto);
    }

    private void handleInvalidUrlException(Exception ex, Context ctx) {
        ApiError apiError = new ApiError("Invalid Form", ex.getMessage());
        ctx.status(HttpStatus.BAD_REQUEST_400).json(apiError);
    }

    @Override
    public void applyEndpoints() {
        app.exception(InvalidUrlException.class, this::handleInvalidUrlException);
        app.post("/short-url", this::shortUrl, Set.of(RoleList.ADMIN, RoleList.APP_USER));
        app.get("/users/:username/urls", this::getUrlsByUser, Set.of(RoleList.ADMIN));
        app.get("/me/urls", this::getMyUrls, Set.of(RoleList.ADMIN, RoleList.APP_USER));
    }
}
