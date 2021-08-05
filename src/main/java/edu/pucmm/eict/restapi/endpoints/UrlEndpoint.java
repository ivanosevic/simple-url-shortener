package edu.pucmm.eict.restapi.endpoints;

import edu.pucmm.eict.common.ApplicationProperties;
import edu.pucmm.eict.common.Page;
import edu.pucmm.eict.reports.ReportService;
import edu.pucmm.eict.reports.UrlReport;
import edu.pucmm.eict.restapi.apiresponses.ApiError;
import edu.pucmm.eict.restapi.apiresponses.SubError;
import edu.pucmm.eict.restapi.common.MyValidator;
import edu.pucmm.eict.restapi.dtos.ReportDto;
import edu.pucmm.eict.restapi.dtos.ShortUrlForm;
import edu.pucmm.eict.restapi.dtos.UrlDto;
import edu.pucmm.eict.restapi.mappers.DtoMapper;
import edu.pucmm.eict.restapi.security.JwtUtils;
import edu.pucmm.eict.urls.dao.ShortURLDao;
import edu.pucmm.eict.urls.models.ShortURL;
import edu.pucmm.eict.urls.services.ShortURLService;
import edu.pucmm.eict.users.Role;
import edu.pucmm.eict.users.User;
import edu.pucmm.eict.users.UserDao;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UrlEndpoint extends BaseEndPoint{

    private final JwtUtils jwtUtils;
    private final UserDao userDao;
    private final ShortURLDao shortURLDao;
    private final ShortURLService shortURLService;
    private final ReportService reportService;
    private final ModelMapper modelMapper;
    private final ApplicationProperties applicationProperties;
    private final Integer DEFAULT_USER_PAGE_SIZE = 10;

    public UrlEndpoint(Javalin app) {
        super(app);
        jwtUtils = JwtUtils.getInstance();
        userDao = UserDao.getInstance();
        shortURLService = ShortURLService.getInstance();
        reportService = ReportService.getInstance();
        shortURLDao = ShortURLDao.getInstance();
        modelMapper = DtoMapper.getInstance().getModelMapper();
        applicationProperties = ApplicationProperties.getInstance();
    }

    private String getShortUrl(String code) {
        return "http://" + applicationProperties.getDomain() + "/r/" + code;
    }

    private UrlDto transformToDto(ShortURL shortURL) {
        UrlDto urlDto = new UrlDto();
        urlDto.setUser(shortURL.getUser().getUsername());
        urlDto.setShortUrl(getShortUrl(shortURL.getCode()));
        urlDto.setCreatedAt(shortURL.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME));
        urlDto.setQrCode(shortURLService.getQrCodeBase64(shortURL.getCode()));
        UrlReport urlReport = reportService.getUrlReport(shortURL.getId());
        ReportDto reportDto = modelMapper.map(urlReport, ReportDto.class);
        urlDto.setReport(reportDto);
        urlDto.setName(shortURL.getName());
        return urlDto;
    }

    private Page<UrlDto> transformToPageDto(Page<ShortURL> shortURLPage) {
        List<UrlDto> urlDtos = shortURLPage.getResults().stream().map(this::transformToDto).collect(Collectors.toList());
        return new Page<>(shortURLPage.getTotalPages(), shortURLPage.getCurrentPage(), shortURLPage.isFirst(), shortURLPage.isLast(), urlDtos);
    }

    private void shortUrl(Context ctx) {
        ShortUrlForm shortUrlForm = ctx.bodyAsClass(ShortUrlForm.class);
        List<SubError> errors = MyValidator.getInstance().validate(shortUrlForm);
        if(!errors.isEmpty()) {
            ApiError apiError = new ApiError("Invalid Form", "The form is invalid. Please, check errors.", errors);
            ctx.json(apiError);
            ctx.status(HttpStatus.BAD_REQUEST_400);
            return;
        }

        // We need to get the user from context...
        String token = jwtUtils.parseJwt(ctx);
        String username = jwtUtils.getUsernameFromToken(token);
        User user = userDao.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User with username " + username + " couldn't be found."));
        ShortURL shortURL = shortURLService.cut(shortUrlForm.getUrl(), shortUrlForm.getName(), user);

        // Parse DTO...
        UrlDto urlDto = transformToDto(shortURL);
        ctx.status(HttpStatus.OK_200).json(urlDto);
    }

    private void listUrlsByUser(Context ctx) {
        String username = ctx.pathParam("user", String.class).get();
        Integer page = ctx.queryParam("page", Integer.class, "1").get();
        User user = userDao.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User with username " + username + " couldn't be found."));
        Page<ShortURL> shortURLPage = shortURLDao.findPagedById(user.getId(), page, DEFAULT_USER_PAGE_SIZE);
        Page<UrlDto> urlDtoPage = transformToPageDto(shortURLPage);
        ctx.status(HttpStatus.OK_200).json(urlDtoPage);
    }

    private void listMyUrls(Context ctx) {
        String token = jwtUtils.parseJwt(ctx);
        String username = jwtUtils.getUsernameFromToken(token);
        Integer page = ctx.queryParam("page", Integer.class, "1").get();
        User user = userDao.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User with username " + username + " couldn't be found."));
        Page<ShortURL> shortURLPage = shortURLDao.findPagedById(user.getId(), page, DEFAULT_USER_PAGE_SIZE);
        Page<UrlDto> urlDtoPage = transformToPageDto(shortURLPage);
        ctx.status(HttpStatus.OK_200).json(urlDtoPage);
    }

    @Override
    public void applyRoutes() {
        app.post("/short", this::shortUrl, Set.of(Role.ADMIN, Role.APP_USER));
        app.get("/user/:user/urls", this::listUrlsByUser, Set.of(Role.ADMIN));
        app.get("/me/urls", this::listMyUrls, Set.of(Role.ADMIN, Role.APP_USER));
    }
}
