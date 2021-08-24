package edu.pucmm.eict.reports;

import javax.inject.Inject;

public class UrlReportService {

    private final UrlReportDao urlReportDao;
    private final DataConverter dataConverter;

    @Inject
    public UrlReportService(UrlReportDao urlStatisticsDao, DataConverter dataConverter) {
        this.urlReportDao = urlStatisticsDao;
        this.dataConverter = dataConverter;
    }

    public UrlReport fetchReport(Long shortUrlId) {
        Long clicks = urlReportDao.getClicks(shortUrlId);
        Long uniqueClicks = urlReportDao.getUniqueClicks(shortUrlId);
        Long clicksLast24Hours = urlReportDao.getClicksLast24Hours(shortUrlId);
        ChartData<Long, String> groupedByPlatform = dataConverter.toChartData(urlReportDao.groupedByPlatform(shortUrlId));
        ChartData<Long, String> groupedByOs = dataConverter.toChartData(urlReportDao.groupedByOs(shortUrlId));
        ChartData<Long, String> groupedByBrowser = dataConverter.toChartData(urlReportDao.groupedByBrowser(shortUrlId));
        ChartData<Long, String> clicksByCountry = dataConverter.toChartData(urlReportDao.clicksByCountry(shortUrlId));
        return new UrlReport(clicks, uniqueClicks, clicksLast24Hours, groupedByOs, groupedByBrowser, groupedByPlatform, clicksByCountry);
    }
}

