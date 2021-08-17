package edu.pucmm.isc.urls;

public class URL_Info {

    private String longURL;
    private String shortURL;
    private String createdAt;
    private String user;
    private Statistics statistics;

    public URL_Info(String longURL, String shortURL, String createdAt, String user, Statistics statistics) {
        this.longURL = longURL;
        this.shortURL = shortURL;
        this.createdAt = createdAt;
        this.user = user;
        this.statistics = statistics;
    }

    public String getLongURL() {
        return longURL;
    }

    public void setLongURL(String longURL) {
        this.longURL = longURL;
    }

    public String getShortURL() {
        return shortURL;
    }

    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
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

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }
}


