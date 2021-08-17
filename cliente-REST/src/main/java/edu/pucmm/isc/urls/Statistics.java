package edu.pucmm.isc.urls;

public class Statistics {

    private int clicks;
    private int uniqueClicks;
    private int clickLast24Hours;
    private OS groupedByOs;
    private Browser groupedByBrowser;
    private Platform groupedByPlatform;
    private Country clicksByCountry;

    public Statistics(int clicks, int uniqueClicks, int clickLast24Hours, OS groupedByOs, Browser groupedByBrowser, Platform groupedByPlatform, Country clicksByCountry) {
        this.clicks = clicks;
        this.uniqueClicks = uniqueClicks;
        this.clickLast24Hours = clickLast24Hours;
        this.groupedByOs = groupedByOs;
        this.groupedByBrowser = groupedByBrowser;
        this.groupedByPlatform = groupedByPlatform;
        this.clicksByCountry = clicksByCountry;
    }

    public int getClicks() {
        return clicks;
    }

    public int getUniqueClicks() {
        return uniqueClicks;
    }

    public int getClickLast24Hours() {
        return clickLast24Hours;
    }

    public OS getGroupedByOs() {
        return groupedByOs;
    }

    public Browser getGroupedByBrowser() {
        return groupedByBrowser;
    }

    public Platform getGroupedByPlatform() {
        return groupedByPlatform;
    }

    public Country getClicksByCountry() {
        return clicksByCountry;
    }
}
