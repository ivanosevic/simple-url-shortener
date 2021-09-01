package edu.pucmm.eict.webapp.sessionurls;

public class SessionUrl {
    private String temporaryCode;
    private String name;
    private String urlCode;
    private String url;
    private String shortUrl;
    private String qrCode;
    private String urlPreview;

    public SessionUrl(String temporaryCode, String alias, String urlCode, String url, String shortUrl, String qrCode) {
        this.temporaryCode = temporaryCode;
        this.name = alias;
        this.urlCode = urlCode;
        this.url = url;
        this.shortUrl = shortUrl;
        this.qrCode = qrCode;
    }

    public SessionUrl(String temporaryCode, String name, String urlCode, String url, String shortUrl, String qrCode, String urlPreview) {
        this.temporaryCode = temporaryCode;
        this.name = name;
        this.urlCode = urlCode;
        this.url = url;
        this.shortUrl = shortUrl;
        this.qrCode = qrCode;
        this.urlPreview = urlPreview;
    }

    public String getTemporaryCode() {
        return temporaryCode;
    }

    public void setTemporaryCode(String temporaryCode) {
        this.temporaryCode = temporaryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlCode() {
        return urlCode;
    }

    public void setUrlCode(String urlCode) {
        this.urlCode = urlCode;
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getUrlPreview() {
        return urlPreview;
    }

    public void setUrlPreview(String urlPreview) {
        this.urlPreview = urlPreview;
    }
}
