package edu.pucmm.eict.urls;

public class ShortUrlBuilderImpl implements ShortUrlBuilder {

    private final String domain;

    public ShortUrlBuilderImpl(String domain) {
        this.domain = domain;
    }

    @Override
    public String redirectUrl(String code) {
        return "http://" + domain + "/r/" + code;
    }
}
