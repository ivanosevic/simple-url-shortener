package edu.pucmm.eict.reports;

import java.util.List;

public class UrlReport {
    private long clicks;
    private long uniqueClicks;
    private long clicksDuringDay;
    private String topCountryByClicks;
    private List<StringGroupByNum> clicksByBrowser;
    private List<StringGroupByNum> clicksByPlatform;
    private List<StringGroupByNum> clicksByCountry;
    private List<StringGroupByNum> clicksByOS;

    public UrlReport(long clicks, long uniqueClicks, long clicksDuringDay, String topCountryByClicks, List<StringGroupByNum> clicksByBrowser, List<StringGroupByNum> clicksByPlatform, List<StringGroupByNum> clicksByCountry, List<StringGroupByNum> clicksByOS) {
        this.clicks = clicks;
        this.uniqueClicks = uniqueClicks;
        this.clicksDuringDay = clicksDuringDay;
        this.topCountryByClicks = topCountryByClicks;
        this.clicksByBrowser = clicksByBrowser;
        this.clicksByPlatform = clicksByPlatform;
        this.clicksByCountry = clicksByCountry;
        this.clicksByOS = clicksByOS;
    }

    public long getClicks() {
        return clicks;
    }

    public void setClicks(long clicks) {
        this.clicks = clicks;
    }

    public long getUniqueClicks() {
        return uniqueClicks;
    }

    public void setUniqueClicks(long uniqueClicks) {
        this.uniqueClicks = uniqueClicks;
    }

    public long getClicksDuringDay() {
        return clicksDuringDay;
    }

    public void setClicksDuringDay(long clicksDuringDay) {
        this.clicksDuringDay = clicksDuringDay;
    }

    public String getTopCountryByClicks() {
        return topCountryByClicks;
    }

    public void setTopCountryByClicks(String topCountryByClicks) {
        this.topCountryByClicks = topCountryByClicks;
    }

    public List<StringGroupByNum> getClicksByBrowser() {
        return clicksByBrowser;
    }

    public void setClicksByBrowser(List<StringGroupByNum> clicksByBrowser) {
        this.clicksByBrowser = clicksByBrowser;
    }

    public List<StringGroupByNum> getClicksByPlatform() {
        return clicksByPlatform;
    }

    public void setClicksByPlatform(List<StringGroupByNum> clicksByPlatform) {
        this.clicksByPlatform = clicksByPlatform;
    }

    public List<StringGroupByNum> getClicksByCountry() {
        return clicksByCountry;
    }

    public void setClicksByCountry(List<StringGroupByNum> clicksByCountry) {
        this.clicksByCountry = clicksByCountry;
    }

    public List<StringGroupByNum> getClicksByOS() {
        return clicksByOS;
    }

    public void setClicksByOS(List<StringGroupByNum> clicksByOS) {
        this.clicksByOS = clicksByOS;
    }
}
