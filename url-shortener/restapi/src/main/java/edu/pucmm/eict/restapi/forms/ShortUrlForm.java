package edu.pucmm.eict.restapi.forms;

public class ShortUrlForm {
    private String url;

    public ShortUrlForm(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}