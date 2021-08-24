package edu.pucmm.eict.soap.dto;

public class ShortUrlDto {
    private String longUrl;
    private String shortUrl;
    private String createdAt;
    private String user;
    private UrlReportDto statistics;

    public ShortUrlDto() {
    }

    public ShortUrlDto(String longUrl, String shortUrl, String createdAt, String user) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.createdAt = createdAt;
        this.user = user;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public UrlReportDto getStatistics() {
        return statistics;
    }

    public void setStatistics(UrlReportDto statistics) {
        this.statistics = statistics;
    }
}
