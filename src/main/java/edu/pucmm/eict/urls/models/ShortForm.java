package edu.pucmm.eict.urls.models;

import edu.pucmm.eict.common.validation.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ShortForm {

    @URL
    @NotBlank(message = "URL can't be empty")
    private String url;

    @Size(max = 255, message = "Name has to be less than 255 characters")
    private String name;

    public ShortForm(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}