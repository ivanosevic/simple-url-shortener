package edu.pucmm.eict.reports;

public class AdminReportService {
    private final AdminReportDao adminReportDao;

    public AdminReportService(AdminReportDao adminReportDao) {
        this.adminReportDao = adminReportDao;
    }

    public AdminReport fetchReport() {
        Long totalClicks = adminReportDao.totalClicks().orElse(0L);
        Long totalUsers = adminReportDao.totalUsers().orElse(0L);
        Long totalLinks = adminReportDao.totalLinks().orElse(0L);
        Long totalLinksLastDay = adminReportDao.totalLinksLastDay().orElse(0L);
        return new AdminReport(totalClicks, totalUsers, totalLinks, totalLinksLastDay);
    }
}
