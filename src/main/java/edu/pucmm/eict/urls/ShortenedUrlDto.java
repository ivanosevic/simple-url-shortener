package edu.pucmm.eict.urls;

public class ShortenedUrlDto {
    private Long id;
    private String name;
    private String code;
    private String toUrl;
    private String username;
    private String newUrl;

    public ShortenedUrlDto() {
    }

    public ShortenedUrlDto(String code, String toUrl, String username, String newUrl) {
        this.code = code;
        this.toUrl = toUrl;
        this.username = username;
        this.newUrl = newUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToUrl() {
        return toUrl;
    }

    public void setToUrl(String toUrl) {
        this.toUrl = toUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewUrl() {
        return newUrl;
    }

    public void setNewUrl(String newUrl) {
        this.newUrl = newUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
