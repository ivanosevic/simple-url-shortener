package edu.pucmm.eict.urlshortener.grpc;


import edu.pucmm.eict.urlshortener.grpc.generated.ShortUrlProto;
import edu.pucmm.eict.urlshortener.grpc.generated.ShortUrlServiceRnGrpc;
import edu.pucmm.eict.urlshortener.persistence.Page;
import edu.pucmm.eict.urlshortener.reports.ChartData;
import edu.pucmm.eict.urlshortener.reports.UrlReport;
import edu.pucmm.eict.urlshortener.reports.UrlStatisticsService;
import edu.pucmm.eict.urlshortener.urls.ShortUrl;
import edu.pucmm.eict.urlshortener.urls.ShortUrlDao;
import edu.pucmm.eict.urlshortener.urls.ShortUrlService;
import edu.pucmm.eict.urlshortener.users.User;
import edu.pucmm.eict.urlshortener.users.UserDao;
import io.grpc.stub.StreamObserver;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ShortUrlServicesGrpc extends ShortUrlServiceRnGrpc.ShortUrlServiceRnImplBase {

    private final UrlStatisticsService urlStatisticsService;
    private final ShortUrlService shortUrlService;
    private final ModelMapper modelMapper;
    private final ShortUrlDao shortUrlDao;
    private final UserDao userDao;

    public ShortUrlServicesGrpc(UrlStatisticsService urlStatisticsService, ShortUrlService shortUrlService, ModelMapper modelMapper, ShortUrlDao shortUrlDao, UserDao userDao) {
        this.urlStatisticsService = urlStatisticsService;
        this.shortUrlService = shortUrlService;
        this.modelMapper = modelMapper;
        this.shortUrlDao = shortUrlDao;
        this.userDao = userDao;
    }

    @Override
    public void getShortUrlsByUser(ShortUrlProto.RequestPageShortUrlWithUser request,
                                   StreamObserver<ShortUrlProto.PageShortUrlResponse> responseObserver) {
        final int page = request.getPage();
        final int size = 5;
        final String username = request.getUser();
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username = " + username + " was not found"));
        Page<ShortUrl> shortUrlPage = shortUrlDao.findPagedByUserId(user.getId(), page, size);
        ShortUrlProto.PageShortUrlResponse response = transformToPageResponse(shortUrlPage);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void shortUrl(ShortUrlProto.ShortUrlRequest request, StreamObserver<ShortUrlProto.ShortUrlResponse> responseObserver) {
        String url = request.getUrl();
        ShortUrl shortUrl = shortUrlService.doShort(null, url);
        ShortUrlProto.ShortUrlResponse response = transformToResponse(shortUrl);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private ShortUrlProto.PageShortUrlResponse transformToPageResponse(Page<ShortUrl> shortUrlPage) {
        List<ShortUrlProto.ShortUrlResponse> shortUrlResponses = new ArrayList<>();
        shortUrlPage.getResults().forEach(shortUrl -> {
            ShortUrlProto.ShortUrlResponse response = transformToResponse(shortUrl);
            shortUrlResponses.add(response);
        });
        return ShortUrlProto.PageShortUrlResponse.newBuilder()
                .setCurrentPage(shortUrlPage.getCurrentPage())
                .setIsLast(shortUrlPage.isLast())
                .setIsEmpty(shortUrlPage.isEmpty())
                .setIsFirst(shortUrlPage.isFirst())
                .setTotalPages(shortUrlPage.getTotalPages())
                .addAllResults(shortUrlResponses)
                .build();
    }

    private ShortUrlProto.ChartData transformToCharData(ChartData<Long, String> data) {
        return ShortUrlProto.ChartData.newBuilder()
                .addAllLabels(data.getLabels())
                .addAllSeries(data.getSeries())
                .build();
    }

    private ShortUrlProto.UrlStatistics transformToStatistics(ShortUrl shortUrl) {
        UrlReport urlReport = urlStatisticsService.fetchReport(shortUrl.getId());
        UrlReportDto urlReportDto = modelMapper.map(urlReport, UrlReportDto.class);
        return ShortUrlProto.UrlStatistics.newBuilder()
                .setClicks(urlReportDto.getClicks())
                .setUniqueClicks(urlReportDto.getUniqueClicks())
                .setClicksLast24Hours(urlReportDto.getClicksLast24Hours())
                .setGroupedByBrowser(transformToCharData(urlReportDto.getGroupedByBrowser()))
                .setClicksByCountry(transformToCharData(urlReportDto.getClicksByCountry()))
                .setGroupedByOs(transformToCharData(urlReportDto.getGroupedByOs()))
                .setGroupedByPlatform(transformToCharData(urlReportDto.getGroupedByPlatform()))
                .build();
    }

    private ShortUrlProto.ShortUrlResponse transformToResponse(ShortUrl shortUrl) {
        ShortUrlDto shortUrlDto = modelMapper.map(shortUrl, ShortUrlDto.class);
        return ShortUrlProto.ShortUrlResponse.newBuilder()
                .setCreatedAt(shortUrlDto.getCreatedAt())
                .setLongUrl(shortUrlDto.getLongUrl())
                .setShortUrl(shortUrlDto.getShortUrl())
                .setStatistics(transformToStatistics(shortUrl))
                .setUser(shortUrlDto.getUser())
                .build();
    }
}
