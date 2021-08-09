package edu.pucmm.eict.urlshortener.urls;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This is the information that is store when an user clicks
 * at a shortened link. It covers useful information for
 * analytics and the time it was created.
 */
@Entity
public class Click implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private ShortUrl shortUrl;

    @Column(length = 15, nullable = false)
    private String ip;

    @Column(nullable = false)
    private String browser;

    @Column(nullable = false)
    private String platform;

    @Column(nullable = false)
    private String os;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String countryIso2;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Click() {
    }

    public Click(ShortUrl shortUrl, String ip, String browser, String platform, String os, String country, String countryIso2) {
        this.shortUrl = shortUrl;
        this.ip = ip;
        this.browser = browser;
        this.platform = platform;
        this.os = os;
        this.country = country;
        this.countryIso2 = countryIso2;
    }

    @PrePersist
    public void beforeInsert() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShortUrl getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(ShortUrl shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryIso2() {
        return countryIso2;
    }

    public void setCountryIso2(String countryIso2) {
        this.countryIso2 = countryIso2;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
