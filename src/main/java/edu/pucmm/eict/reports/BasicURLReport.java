package edu.pucmm.eict.reports;

public class BasicURLReport {
    private long clicks;
    private long uniqueClicks;
    private long clicksDuringDay;
    private String topCountryByClicks;
    private String clicksByBrowser;
    private String clicksByPlatform;
    private String clicksByCountry;
    private String clicksByOS;

    public BasicURLReport() {
    }

    public BasicURLReport(long clicks, long uniqueClicks, long clicksDuringDay, String topCountryByClicks, String clicksByBrowser, String clicksByPlatform, String clicksByCountry, String clicksByOS) {
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

    public String getClicksByBrowser() {
        return clicksByBrowser;
    }

    public void setClicksByBrowser(String clicksByBrowser) {
        this.clicksByBrowser = clicksByBrowser;
    }

    public String getClicksByPlatform() {
        return clicksByPlatform;
    }

    public void setClicksByPlatform(String clicksByPlatform) {
        this.clicksByPlatform = clicksByPlatform;
    }

    public String getClicksByCountry() {
        return clicksByCountry;
    }

    public void setClicksByCountry(String clicksByCountry) {
        this.clicksByCountry = clicksByCountry;
    }

    public String getClicksByOS() {
        return clicksByOS;
    }

    public void setClicksByOS(String clicksByOS) {
        this.clicksByOS = clicksByOS;
    }
}