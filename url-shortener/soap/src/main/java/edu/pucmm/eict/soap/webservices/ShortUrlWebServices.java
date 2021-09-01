package edu.pucmm.eict.soap.webservices;

import edu.pucmm.eict.persistence.Page;
import edu.pucmm.eict.reports.UrlReport;
import edu.pucmm.eict.reports.UrlReportService;
import edu.pucmm.eict.soap.dto.ShortUrlDto;
import edu.pucmm.eict.soap.dto.UrlReportDto;
import edu.pucmm.eict.urls.ShortUrl;
import edu.pucmm.eict.urls.ShortUrlService;
import edu.pucmm.eict.users.User;
import edu.pucmm.eict.users.UserDao;
import org.modelmapper.ModelMapper;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@WebService
public class ShortUrlWebServices {

    @Inject
    private ShortUrlService shortUrlService;

    @Inject
    private UrlReportService urlReportService;

    @Inject
    private UserDao userDao;

    @Inject
    private ModelMapper modelMapper;

    @WebMethod
    public ShortUrlDto shortUrl(String url) {
        ShortUrl shortUrl = shortUrlService.doShort(null, url);
        Long shortUrlId = shortUrl.getId();
        UrlReport urlReport = urlReportService.fetchReport(shortUrlId);
        UrlReportDto urlReportDto = modelMapper.map(urlReport, UrlReportDto.class);
        ShortUrlDto shortUrlDto = modelMapper.map(shortUrl, ShortUrlDto.class);
        shortUrlDto.setStatistics(urlReportDto);
        return shortUrlDto;
    }

    @WebMethod
    public Page<ShortUrlDto> getPageByUser(String username, Integer page) {
        final int pageSize = 5;
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username " + username + " was not found."));
        Long userId = user.getId();
        Page<ShortUrl> shortUrlPage = shortUrlService.findPagedByUserId(userId, page, pageSize);

        // We must map the entities in the endpoint...
        List<ShortUrlDto> shortUrlDtoList = new ArrayList<>();
        shortUrlPage.getResults().forEach(shortUrl -> {
            Long shortUrlId = shortUrl.getId();
            UrlReport urlReport = urlReportService.fetchReport(shortUrlId);
            UrlReportDto urlReportDto = modelMapper.map(urlReport, UrlReportDto.class);
            ShortUrlDto shortUrlDto = modelMapper.map(shortUrl, ShortUrlDto.class);
            shortUrlDto.setStatistics(urlReportDto);
            shortUrlDtoList.add(shortUrlDto);
        });

        // We create our own page and return it
        Page<ShortUrlDto> shortUrlDtoPage = new Page<>(shortUrlPage.getTotalPages(), shortUrlPage.getCurrentPage(), shortUrlPage.isFirst(),
                shortUrlPage.isLast(), shortUrlDtoList);
        return shortUrlDtoPage;
    }
}
