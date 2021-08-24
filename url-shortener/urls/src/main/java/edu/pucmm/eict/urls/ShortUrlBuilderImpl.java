package edu.pucmm.eict.urls;

import javax.inject.Inject;
import javax.inject.Named;

public class ShortUrlBuilderImpl implements ShortUrlBuilder {

    private final String domain;

    @Inject
    public ShortUrlBuilderImpl(@Named("domain") String domain) {
        this.domain = domain;
    }

    @Override
    public String redirectUrl(String code) {
        return "http://" + domain + "/r/" + code;
    }
}
