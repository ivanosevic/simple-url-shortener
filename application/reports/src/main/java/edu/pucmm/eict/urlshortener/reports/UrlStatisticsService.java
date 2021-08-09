package edu.pucmm.eict.urlshortener.reports;

public class UrlStatisticsService {

    private final UrlStatisticsDao urlStatisticsDao;
    private final DataConverter dataConverter;

    public UrlStatisticsService(UrlStatisticsDao urlStatisticsDao, DataConverter dataConverter) {
        this.urlStatisticsDao = urlStatisticsDao;
        this.dataConverter = dataConverter;
    }

    public UrlReport fetchReport(Long shortUrlId) {
        Long clicks = urlStatisticsDao.getClicks(shortUrlId);
        Long uniqueClicks = urlStatisticsDao.getUniqueClicks(shortUrlId);
        Long clicksLast24Hours = urlStatisticsDao.getClicksLast24Hours(shortUrlId);
        ChartData<Long, String> groupedByPlatform = dataConverter.toChartData(urlStatisticsDao.groupedByPlatform(shortUrlId));
        ChartData<Long, String> groupedByOs = dataConverter.toChartData(urlStatisticsDao.groupedByOs(shortUrlId));
        ChartData<Long, String> groupedByBrowser = dataConverter.toChartData(urlStatisticsDao.groupedByBrowser(shortUrlId));
        ChartData<Long, String> clicksByCountry = dataConverter.toChartData(urlStatisticsDao.clicksByCountry(shortUrlId));
        return new UrlReport(clicks, uniqueClicks, clicksLast24Hours, groupedByOs, groupedByBrowser, groupedByPlatform, clicksByCountry);
    }
}
