package edu.pucmm.eict.urls;

import edu.pucmm.eict.users.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ShortUrl represents a shortened url by an end user.
 * The code it possesses is the id saved as a base62 hash.
 */
@Entity
public class ShortUrl implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private String name;

    private String code;

    @Lob
    private String preview;

    @Column(length = 2048, nullable = false)
    private String url;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean active;

    public ShortUrl() {
    }

    public ShortUrl(User user, String name, String code, String url) {
        this.user = user;
        this.name = name;
        this.code = code;
        this.url = url;
    }

    public ShortUrl(User user, String name, String code, String url, String preview) {
        this.user = user;
        this.name = name;
        this.code = code;
        this.url = url;
        this.preview = preview;
    }

    @PrePersist
    public void beforeInsert() {
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }
}
