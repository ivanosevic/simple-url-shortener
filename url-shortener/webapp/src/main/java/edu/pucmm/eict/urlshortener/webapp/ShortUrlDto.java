package edu.pucmm.eict.urlshortener.webapp;

public class ShortUrlDto {
    private String name;
    private String code;
    private String qrCode;
    private String url;
    private String shortUrl;
    private String createdAt;

    public ShortUrlDto() {
    }

    public ShortUrlDto(String name, String code, String qrCode, String url, String shortUrl, String createdAt) {
        this.name = name;
        this.code = code;
        this.qrCode = qrCode;
        this.url = url;
        this.shortUrl = shortUrl;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
