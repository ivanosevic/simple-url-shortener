package edu.pucmm.eict.urlshortener.restapi.dtos;

import edu.pucmm.eict.urlshortener.reports.ChartData;

public class UrlReportDto {
    private Long clicks;
    private Long uniqueClicks;
    private Long clicksLast24Hours;
    private ChartData<Long, String> groupedByOs;
    private ChartData<Long, String> groupedByBrowser;
    private ChartData<Long, String> groupedByPlatform;
    private ChartData<Long, String> clicksByCountry;

    public UrlReportDto() {

    }

    public UrlReportDto(Long clicks,
                        Long uniqueClicks,
                        Long clicksLast24Hours,
                        ChartData<Long, String> groupedByOs,
                        ChartData<Long, String> groupedByBrowser,
                        ChartData<Long, String> groupedByPlatform,
                        ChartData<Long, String> clicksByCountry) {
        this.clicks = clicks;
        this.uniqueClicks = uniqueClicks;
        this.clicksLast24Hours = clicksLast24Hours;
        this.groupedByOs = groupedByOs;
        this.groupedByBrowser = groupedByBrowser;
        this.groupedByPlatform = groupedByPlatform;
        this.clicksByCountry = clicksByCountry;
    }

    public Long getClicks() {
        return clicks;
    }

    public void setClicks(Long clicks) {
        this.clicks = clicks;
    }

    public Long getUniqueClicks() {
        return uniqueClicks;
    }

    public void setUniqueClicks(Long uniqueClicks) {
        this.uniqueClicks = uniqueClicks;
    }

    public Long getClicksLast24Hours() {
        return clicksLast24Hours;
    }

    public void setClicksLast24Hours(Long clicksLast24Hours) {
        this.clicksLast24Hours = clicksLast24Hours;
    }

    public ChartData<Long, String> getGroupedByOs() {
        return groupedByOs;
    }

    public void setGroupedByOs(ChartData<Long, String> groupedByOs) {
        this.groupedByOs = groupedByOs;
    }

    public ChartData<Long, String> getGroupedByBrowser() {
        return groupedByBrowser;
    }

    public void setGroupedByBrowser(ChartData<Long, String> groupedByBrowser) {
        this.groupedByBrowser = groupedByBrowser;
    }

    public ChartData<Long, String> getGroupedByPlatform() {
        return groupedByPlatform;
    }

    public void setGroupedByPlatform(ChartData<Long, String> groupedByPlatform) {
        this.groupedByPlatform = groupedByPlatform;
    }

    public ChartData<Long, String> getClicksByCountry() {
        return clicksByCountry;
    }

    public void setClicksByCountry(ChartData<Long, String> clicksByCountry) {
        this.clicksByCountry = clicksByCountry;
    }
}
