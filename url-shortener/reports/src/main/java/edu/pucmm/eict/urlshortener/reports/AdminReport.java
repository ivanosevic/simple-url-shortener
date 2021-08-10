package edu.pucmm.eict.urlshortener.reports;

public class AdminReport {
    private Long totalClicks;
    private Long totalUsers;
    private Long totalLinks;
    private Long totalLinksLastDay;

    public AdminReport() {
    }

    public AdminReport(Long totalClicks, Long totalUsers, Long totalLinks, Long totalLinksLastDay) {
        this.totalClicks = totalClicks;
        this.totalUsers = totalUsers;
        this.totalLinks = totalLinks;
        this.totalLinksLastDay = totalLinksLastDay;
    }

    public Long getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(Long totalClicks) {
        this.totalClicks = totalClicks;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalLinks() {
        return totalLinks;
    }

    public void setTotalLinks(Long totalLinks) {
        this.totalLinks = totalLinks;
    }

    public Long getTotalLinksLastDay() {
        return totalLinksLastDay;
    }

    public void setTotalLinksLastDay(Long totalLinksLastDay) {
        this.totalLinksLastDay = totalLinksLastDay;
    }
}
