package edu.pucmm.eict.urls;

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
    private ShortenedUrl shortenedUrl;

    @Column(length = 15, nullable = false)
    private String accessIp;

    @Column(nullable = false)
    private String browser;

    @Column(nullable = false)
    private String platform;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private LocalDateTime clickedAt;

    public Referrer() {
    }

    public Referrer(String accessIp, String browser, String platform, String country) {
        this.accessIp = accessIp;
        this.browser = browser;
        this.platform = platform;
        this.country = country;
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

    public ShortenedUrl getShortenedUrl() {
        return shortenedUrl;
    }

    public void setShortenedUrl(ShortenedUrl shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
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

    public LocalDateTime getClickedAt() {
        return clickedAt;
    }

    public void setClickedAt(LocalDateTime clickedAt) {
        this.clickedAt = clickedAt;
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
        return id == referrer.id && Objects.equals(shortenedUrl, referrer.shortenedUrl) && Objects.equals(accessIp, referrer.accessIp) && Objects.equals(browser, referrer.browser) && Objects.equals(platform, referrer.platform) && Objects.equals(country, referrer.country) && Objects.equals(clickedAt, referrer.clickedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shortenedUrl, accessIp, browser, platform, country, clickedAt);
    }
}