package edu.pucmm.eict.urls.models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Referrer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "shortened_url_id")
    private ShortURL shortURL;

    @Column(length = 15, nullable = false)
    private String accessIp;

    @Column(nullable = false)
    private String browser;

    @Column(nullable = false)
    private String platform;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String countryIso2;

    @Column(nullable = false)
    private String operatingSystem;

    @Column(nullable = false)
    private LocalDateTime clickedAt;

    public Referrer() {
    }

    public Referrer(String accessIp, String browser, String platform, String operatingSystem, String country) {
        this.accessIp = accessIp;
        this.browser = browser;
        this.platform = platform;
        this.country = country;
        this.operatingSystem = operatingSystem;
    }

    @PrePersist
    public void beforeInsert() {
        this.clickedAt = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ShortURL getShortURL() {
        return shortURL;
    }

    public void setShortURL(ShortURL shortURL) {
        this.shortURL = shortURL;
    }

    public String getAccessIp() {
        return accessIp;
    }

    public void setAccessIp(String accessIp) {
        this.accessIp = accessIp;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public LocalDateTime getClickedAt() {
        return clickedAt;
    }

    public void setClickedAt(LocalDateTime clickedAt) {
        this.clickedAt = clickedAt;
    }

    public String getCountryIso2() {
        return countryIso2;
    }

    public void setCountryIso2(String countryIso2) {
        this.countryIso2 = countryIso2;
    }

    @Override
    public String toString() {
        return "Referrer{" +
                "id=" + id +
                ", accessIp='" + accessIp + '\'' +
                ", browser='" + browser + '\'' +
                ", platform='" + platform + '\'' +
                ", country='" + country + '\'' +
                ", clickedAt=" + clickedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Referrer referrer = (Referrer) o;
        return id == referrer.id && Objects.equals(shortURL, referrer.shortURL) && Objects.equals(accessIp, referrer.accessIp) && Objects.equals(browser, referrer.browser) && Objects.equals(platform, referrer.platform) && Objects.equals(country, referrer.country) && Objects.equals(clickedAt, referrer.clickedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shortURL, accessIp, browser, platform, country, clickedAt);
    }
}