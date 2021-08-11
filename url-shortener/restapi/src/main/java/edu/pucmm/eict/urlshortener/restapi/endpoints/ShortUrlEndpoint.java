package edu.pucmm.eict.urlshortener.restapi.endpoints;

import edu.pucmm.eict.urlshortener.persistence.Page;
import edu.pucmm.eict.urlshortener.reports.UrlReport;
import edu.pucmm.eict.urlshortener.reports.UrlStatisticsService;
import edu.pucmm.eict.urlshortener.restapi.dtos.ShortUrlDto;
import edu.pucmm.eict.urlshortener.restapi.dtos.UrlReportDto;
import edu.pucmm.eict.urlshortener.urls.ShortUrl;
import edu.pucmm.eict.urlshortener.urls.ShortUrlService;
import edu.pucmm.eict.urlshortener.users.RoleList;
import edu.pucmm.eict.urlshortener.users.User;
import edu.pucmm.eict.urlshortener.users.UserDao;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShortUrlEndpoint extends BaseEndpoint {

    private final UserDao userDao;
    private final ShortUrlService shortUrlService;
    private final UrlStatisticsService urlStatisticsService;
    private final ModelMapper modelMapper;

    public ShortUrlEndpoint(Javalin app, UserDao userDao,
                            ShortUrlService shortUrlService,
                            UrlStatisticsService urlStatisticsService,
                            ModelMapper modelMapper) {
        super(app);
        this.userDao = userDao;
        this.shortUrlService = shortUrlService;
        this.urlStatisticsService = urlStatisticsService;
        this.modelMapper = modelMapper;
    }

    private void getUrlsByUser(Context ctx) {
        String username = ctx.pathParam("username", String.class).get();
        int page = ctx.queryParam("page", Integer.class, "1").get();
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
    
    @Override
    public void applyEndpoints() {
        app.get("/users/:username/urls", this::getUrlsByUser, Set.of(RoleList.ADMIN));
    }
}
