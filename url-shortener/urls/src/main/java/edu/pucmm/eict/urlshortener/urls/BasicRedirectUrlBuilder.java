package edu.pucmm.eict.urlshortener.urls;

public class BasicRedirectUrlBuilder implements RedirectUrlBuilder {
    private final String domain;

    public BasicRedirectUrlBuilder(String domain) {
        this.domain = domain;
    }

    @Override
    public String redirectUrl(String code) {
        return "http://" + domain + "/r/" + code;
    }
}
